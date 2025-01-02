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

    private Date updatedDate;

    private boolean isEnabled;

    public UrlMapping(String shortUrl, String longUrl, String userId, boolean isEnabled) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.userId = userId;
        this.createdDate = new Date();
        this.isEnabled = isEnabled;
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

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}