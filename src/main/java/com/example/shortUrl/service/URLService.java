package com.example.shortUrl.service;

import com.example.shortUrl.model.UrlMapping;
import com.example.shortUrl.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;


@Service
public class URLService {

    @Autowired
    private UrlRepository urlRepository;

    private final Random counter = new Random();
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String shorten(String longUrl,String userId) {
        String shortUrl = encodeBase62(counter.nextInt());
        UrlMapping urlMapping = new UrlMapping(shortUrl, longUrl, userId);
        urlRepository.save(urlMapping);
        return shortUrl;
    }

    public String getLongUrl(String shortUrl) {
        Optional<UrlMapping> optionalUrlMapping = urlRepository.findById(shortUrl);
        if (optionalUrlMapping.isPresent()) {
            return optionalUrlMapping.get().getLongUrl();
        } else {
            return null;
        }
    }

    private String encodeBase62(long id) {
        id = Math.abs(id);
        StringBuilder sb = new StringBuilder(7);
        do {
            sb.append(BASE62.charAt((int) (id % 62)));
            id /= 62;
        } while (id > 0);
        return sb.reverse().toString();
    }
}

