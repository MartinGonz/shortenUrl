package com.example.shortUrl.repository;

import com.example.shortUrl.model.UrlMapping;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends CassandraRepository<UrlMapping, String> {
}