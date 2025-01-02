package com.example.shortUrl.service;

import com.example.shortUrl.model.UrlMapping;
import com.example.shortUrl.repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;


@Service
public class URLService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private RedisTemplate<String, UrlMapping> redisTemplate;

    private static final Logger log = LoggerFactory.getLogger(URLService.class);


    private static final String CACHE_PREFIX = "urlCache::";
    private final Random counter = new Random();
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String shorten(String longUrl,String userId, Boolean isEnabled) {
        String shortUrl = encodeBase62(counter.nextInt());
        UrlMapping urlMapping = new UrlMapping(shortUrl, longUrl, userId, isEnabled);

        //check if shorten URL already exists, if it does, generate a new one
        while(isShortUrlAvailable(shortUrl, urlMapping) == false){
            shortUrl = encodeBase62(counter.nextInt());
        };

        urlRepository.save(urlMapping);
        return shortUrl;
    }

    private boolean isShortUrlAvailable(String shortUrl, UrlMapping urlMapping) {

        if (urlRepository.findById(shortUrl).isPresent()){
            urlMapping.setShortUrl(encodeBase62(counter.nextInt()));
        }

        return true;
    }

    public String getLongUrl(String shortUrl) throws FileNotFoundException {
        // Try to get from cache
        try {
            log.debug("about to obtain Short URL: " + shortUrl + "from cache");
            UrlMapping urlMapping = redisTemplate.opsForValue().get(CACHE_PREFIX + shortUrl);
            log.debug("URL Mapping from cache: " + urlMapping);

            if (urlMapping == null) {
                Optional<UrlMapping> optionalUrlMapping = urlRepository.findById(shortUrl);
                log.debug("URL Mapping from repository present = " + optionalUrlMapping.isPresent());

                if (optionalUrlMapping.isPresent() && optionalUrlMapping.get().getEnabled()) {
                    urlMapping =  optionalUrlMapping.get();
                    log.debug("URL Mapping from repository: " + urlMapping);

                    redisTemplate.opsForValue().set(CACHE_PREFIX + shortUrl, urlMapping);
                } else {
                    throw new FileNotFoundException("URL not found or disabled");
                }
            }
            return urlMapping.getLongUrl();
        } catch (Exception e) {
            log.error("Failed to get long URL", e);
            throw new RuntimeException(e);
        }
    }

    public UrlMapping updateUrlMapping(String longUrl, String userId, String shortUrl, Boolean isEnabled) {
        //get existing data and check for userId
        Optional<UrlMapping> optionalUrlMapping = urlRepository.findById(shortUrl);
        if (!optionalUrlMapping.isPresent() || !optionalUrlMapping.get().getUserId().equals(userId)) {
            throw new AuthorizationDeniedException("User not authorized to update the URL mapping");
        }
        UrlMapping updatedUrlMapping = new UrlMapping(shortUrl, longUrl, userId, isEnabled);
        updatedUrlMapping.setUpdatedDate(new Date());
        return urlRepository.save(updatedUrlMapping);
    }


    private String encodeBase62(long id) {
        id = Math.abs(id);
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(BASE62.charAt((int) (id % 62)));
            id /= 62;
        } while (id > 0);

        // Ensure the short URL has at least 7 characters
        while (sb.length() < 7) {
            sb.append(BASE62.charAt(counter.nextInt(62)));
        }

        return sb.reverse().toString();
    }

    public void deleteUrlMapping(String shortUrl) {
        // Delete from database
        urlRepository.deleteById(shortUrl);

        // Invalidate cache
        redisTemplate.delete(CACHE_PREFIX + shortUrl);
    }
}

