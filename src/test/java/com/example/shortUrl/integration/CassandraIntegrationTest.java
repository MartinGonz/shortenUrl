package com.example.shortUrl.integration;

import com.datastax.oss.driver.api.core.CqlSession;
import com.example.shortUrl.model.UrlMapping;
import com.example.shortUrl.repository.UrlRepository;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnit;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@CassandraUnit
@CassandraDataSet(value = "cassandra-dataset.cql", keyspace = "shorten_urls")
public class CassandraIntegrationTest {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private CassandraTemplate cassandraTemplate;

    @Autowired
    private CassandraAdminTemplate cassandraAdminTemplate;

    private static final Integer DB_PORT = 9142;
    private static final String CONTACT_POINTS = "localhost";
    private static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE shorten_url WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };";
    private static final String KEYSPACE_ACTIVATE_QUERY = "USE shorten_url;";
    private static final String TABLE_CREATION_QUERY = "CREATE TABLE IF NOT EXISTS url_mapping (" +
            "shortUrl TEXT PRIMARY KEY, " +
            "longUrl TEXT, " +
            "userId TEXT, " +
            "createdDate TIMESTAMP);";
    private static final String DATA_TABLE_NAME = "url_mapping";

    @BeforeAll
    public static void startCassandraEmbedded() throws InterruptedException, TTransportException, ConfigurationException, IOException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra();
        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(CONTACT_POINTS, DB_PORT))
                .withLocalDatacenter("datacenter1")
                .build()) {
            session.execute(KEYSPACE_CREATION_QUERY);
            session.execute(KEYSPACE_ACTIVATE_QUERY);
            session.execute(TABLE_CREATION_QUERY);
        }
    }



    @Test
    public void testCassandraConnection() {
        assertNotNull(cassandraTemplate);
        assertNotNull(cassandraAdminTemplate);
    }

    @Test
    public void testKeyspaceExists() {
        boolean keyspaceExists = cassandraAdminTemplate.getCqlOperations()
                .queryForObject("SELECT keyspace_name FROM system_schema.keyspaces WHERE keyspace_name = 'shorten_urls';", Boolean.class);
        assertTrue(keyspaceExists, "Keyspace 'shorten_urls' should exist");
    }
    @Test
    public void testSaveAndFind() {
        String shortUrl = "test123";
        String longUrl = "http://example.com";
        String userId = "user123";
        UrlMapping urlMapping = new UrlMapping(shortUrl, longUrl, userId);

        // Save to repository
        urlRepository.save(urlMapping);

        // Find
        Optional<UrlMapping> retrievedUrlMapping = urlRepository.findById(shortUrl);

        // Asserts
        assertTrue(retrievedUrlMapping.isPresent());
        assertEquals(longUrl, retrievedUrlMapping.get().getLongUrl());
        assertEquals(userId, retrievedUrlMapping.get().getUserId());
    }
}
