package com.example.shortUrl.controller;

import com.example.shortUrl.service.URLService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> shortenUrl(@RequestParam String longUrl){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            String shortenUrl = urlService.shorten(longUrl, userId);
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String fullShortUrl = baseUrl + "/" + shortenUrl;
             log.info("shorten url "+fullShortUrl + "<-----------------------------------");
            return ResponseEntity.ok(fullShortUrl);
        } catch (Exception e) {
            log.error("Failed to shorten URL, Error -> ",e);

           return ResponseEntity.internalServerError().body("Failed to shorten URL");
        }

    }

    @GetMapping("/{short_url}")
    public ResponseEntity<?> getLongUrl(@PathVariable("short_url") String shortUrl){
        try {
            String longUrl = urlService.getLongUrl(shortUrl);

            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrl)).build();
        } catch (Exception e) {
            log.error("Failed to shorten URL, Error -> ",e);

            return ResponseEntity.internalServerError().body("Failed to shorten URL");
        }

    }
}
