package com.example.shortUrl.configuration;

import com.datastax.oss.driver.api.core.CqlSession;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.SessionFactory;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.core.cql.session.DefaultSessionFactory;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.net.InetSocketAddress;

@Configuration
@EnableCassandraRepositories(basePackages = "com.example.shortUrl.repository")
public class CassandraConfig  extends AbstractCassandraConfiguration {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CassandraConfig.class);

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspaceName;

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port}")
    private int port;

    @Value("${spring.data.cassandra.local-datacenter}")
    private String localDatacenter;

    @Bean
    @Primary
    public CqlSession cqlSession() {
        logger.info("========================================================================================");
        logger.info("========================================================================================");
        logger.info("Creating CqlSession with contact points: {}, port: {}, local datacenter: {}, keyspace: {}",
                contactPoints, port, localDatacenter, keyspaceName);
        logger.info("========================================================================================");
        logger.info("========================================================================================");
        CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(contactPoints, port))
                .withLocalDatacenter(localDatacenter)
                .withKeyspace((keyspaceName))
                .build();
        logger.info("==============================RETURNING ==========================================================");

        return session;
    }

    @Bean
    public DefaultSessionFactory sessionFactory(CqlSession cqlSession) {
        return new DefaultSessionFactory(cqlSession);
    }

    @Bean
    public CassandraAdminTemplate cassandraTemplate() {
        return new CassandraAdminTemplate(cqlSession());
    }

    @Override
    protected String getKeyspaceName() {
        return "shorten_urls";
    }
    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.NONE;
    }
    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"com.example.shortUrl.model"};
    }
}
