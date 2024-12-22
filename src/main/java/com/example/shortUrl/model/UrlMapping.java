package com.example.shortUrl.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

@Table
@Slf4j
public class UrlMapping {

    @PrimaryKey
    private String shortUrl;

    private String longUrl;

    private String userId;

    private Date createdDate;


}