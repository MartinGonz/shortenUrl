package com.example.shortUrl.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

@Table(value = "url_mapping" )
public class UrlMapping {

    @Id
    @PrimaryKey
    private String shortUrl;

    private String longUrl;

    private String userId;

    private Date createdDate;

    public UrlMapping(String shortUrl, String longUrl, String userId) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.userId = userId;
        this.createdDate = new Date();
    }

    public UrlMapping() {
    }

    public String getLongUrl() {
        return longUrl;
    }
    public String getShortUrl() {
        return shortUrl;
    }
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}