package com.example.shortUrl.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class URLService {

    private final Map<String, String> urlMap = new HashMap<>();
    private final AtomicLong counter = new AtomicLong(1);
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String shorten(String longUrl) {
        String shortUrl = encodeBase62(counter.getAndIncrement());
        urlMap.put(shortUrl, longUrl);
        return shortUrl;
    }

    public String getLongUrl(String longUrl){

        return null;
    }


    private String encodeBase62(long id) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(BASE62.charAt((int) (id % 62)));
            id /= 62;
        } while (id > 0);
        return sb.reverse().toString();
    }
}
