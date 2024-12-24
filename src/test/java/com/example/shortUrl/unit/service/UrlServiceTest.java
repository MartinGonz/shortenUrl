package com.example.shortUrl.unit.service;

import com.example.shortUrl.model.UrlMapping;
import com.example.shortUrl.repository.UrlRepository;

import com.example.shortUrl.service.URLService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private URLService urlService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShorten() {
        String longUrl = "http://example.com";
        String userId = "user123";
        String shortUrl = "abc123";

        when(urlRepository.save(any(UrlMapping.class))).thenReturn(null);

        String result = urlService.shorten(longUrl, userId);

        assertEquals(7, result.length());
        verify(urlRepository, times(1)).save(any(UrlMapping.class));
    }

    @Test
    public void testGetLongUrl() {
        String shortUrl = "abc123";
        String longUrl = "http://example.com";
        UrlMapping urlMapping = new UrlMapping(shortUrl, longUrl, "user123");

        when(urlRepository.findById(shortUrl)).thenReturn(Optional.of(urlMapping));

        String result = urlService.getLongUrl(shortUrl);

        assertEquals(longUrl, result);
        verify(urlRepository, times(1)).findById(shortUrl);
    }
}

