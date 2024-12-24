package com.example.shortUrl.configuration;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;

import java.net.InetSocketAddress;

@Configuration
public class CassandraConfig  extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspaceName;

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port}")
    private int port;

    @Value("${spring.data.cassandra.local-datacenter}")
    private String localDatacenter;

    @Bean
    public CqlSession cqlSession() {
        CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(contactPoints, port))
                .withLocalDatacenter(localDatacenter)
                .withKeyspace((keyspaceName))
                .build();

        String createKeyspaceQuery = String.format(
                "CREATE KEYSPACE IF NOT EXISTS %s WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};",
                keyspaceName
        );

        String createTableQuery = "CREATE TABLE IF NOT EXISTS url_mapping (" +
                "shortUrl TEXT PRIMARY KEY, " +
                "longUrl TEXT, " +
                "userId TEXT, " +
                "createdDate TIMESTAMP);";


        session.execute(createKeyspaceQuery);
        session.execute(createTableQuery);

        return session;
    }

    @Override
    protected String getKeyspaceName() {
        return "shorten_urls";
    }
    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }
    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"com.example.shortUrl.model"};
    }
}
