package com.example.shortUrl.controller;

import com.example.shortUrl.model.UrlMapping;
import com.example.shortUrl.service.URLService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class URLShortener {

    private final URLService urlService;

    private static final Logger log = LoggerFactory.getLogger(URLShortener.class);

    @Autowired
    public URLShortener(URLService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten-url")
    public ResponseEntity<?> shortenUrl(@RequestParam(required = true) String longUrl,
                                        @RequestParam(required = false,defaultValue = "true") boolean isEnabled){
        HttpHeaders headers = new HttpHeaders();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            String shortenUrl = urlService.shorten(longUrl, userId, isEnabled);
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String fullShortUrl = baseUrl + "/" + shortenUrl;
            return new ResponseEntity<>(fullShortUrl, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to shorten URL, Error -> ",e);

            return new ResponseEntity<>("Failed to shorten URL", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{short_url}")
    public ResponseEntity<?> getLongUrl(@PathVariable("short_url") String shortUrl){

        HttpHeaders headers = new HttpHeaders();
        try {
            //log for debugging
            log.info("Short URL -> " + shortUrl);
            String longUrl = urlService.getLongUrl(shortUrl);
            log.info("Long URL -> " + longUrl);
            URI uri = URI.create(longUrl);
            headers.setLocation(uri);
            return new ResponseEntity<>(null, headers, HttpStatus.FOUND);
        } catch (Exception e) {
            log.error("URL not found  -> " , e);

            return new ResponseEntity<>("URL not found", headers, HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/edit-url/{shortUrl}")
    public ResponseEntity<?> editUrl(@PathVariable String shortUrl, @RequestBody UrlMapping urlMapping){
        HttpHeaders headers = new HttpHeaders();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            UrlMapping updateUrlMapping = urlService.updateUrlMapping(
                    urlMapping.getLongUrl(),
                    userId,
                    shortUrl,
                    urlMapping.getEnabled());
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String fullShortUrl = baseUrl + "/" + updateUrlMapping.getShortUrl();
            return new ResponseEntity<>(fullShortUrl, headers, HttpStatus.ACCEPTED);
        }
        catch (AuthorizationDeniedException e) {
            return new ResponseEntity<>("User not authorized to update the URL mapping", headers, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            log.error("Failed to shorten URL, Error -> ", e);

            return new ResponseEntity<>("Failed to shorten URL", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
