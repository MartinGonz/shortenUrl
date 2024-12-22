package com.example.shortUrl.controller;

import com.example.shortUrl.service.URLService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Slf4j
public class URLShortener {

    private final URLService urlService;

    public URLShortener(URLService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten-url")
    public ResponseEntity<?> shortenUrl(@RequestParam String longUrl){
        try {
            String shortenUrl = urlService.shorten(longUrl);

            return ResponseEntity.ok(shortenUrl);
        } catch (Exception e) {
            log.error("Failed to shorten URL, Error -> ",e);

           return ResponseEntity.internalServerError().body("Failed to shorten URL");
        }

    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> getLongUrl(@PathVariable String shortUrl){
        try {
            String longUrl = urlService.getLongUrl(shortUrl);

            return ResponseEntity.status(HttpStatus.SC_MOVED_PERMANENTLY).location(URI.create(longUrl)).build();
        } catch (Exception e) {
            log.error("Failed to shorten URL, Error -> ",e);

            return ResponseEntity.internalServerError().body("Failed to shorten URL");
        }

    }
}
