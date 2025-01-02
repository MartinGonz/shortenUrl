package com.example.shortUrl.configuration;


import com.datastax.oss.driver.api.core.CqlSession;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;

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
        logger.debug("========================================================================================");
        logger.debug("========================================================================================");
        logger.debug("Creating CqlSession with contact points: {}, port: {}, local datacenter: {}, keyspace: {}",
                contactPoints, port, localDatacenter, keyspaceName);
        logger.debug("========================================================================================");
        logger.debug("========================================================================================");

        CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(contactPoints, port))
                .withLocalDatacenter(localDatacenter)
                .withKeyspace((keyspaceName))
                .build();

        logger.debug("==================================created==============================================");

        return session;
    }

//    @PostConstruct
//    private void init(CqlSession session) {
//        logger.debug("========================================================================================");
//        logger.debug("============= about to create keypsace and table if not exists =========================");
//        //create keyspace if not exists
//        session.execute("CREATE KEY SPACE IF NOT EXISTS shorten_urls WITH REPLICATION = {'class':'SimpleStrategy', 'replication_factor':1};");
//        //create table if not exists
//        session.execute("CREATE TABLE IF NOT EXISTS shorten_urls.url_mapping (shortUrl text PRIMARY KEY,longUrl text,userId text,createdDate timestamp,updatedDate timestamp,isEnabled boolean);");
//        logger.debug("========================================================================================");
//        logger.debug("==================================created==============================================");
//    }

    @Bean
    public CqlSessionFactoryBean sessionFactory() {
        CqlSessionFactoryBean factory = new CqlSessionFactoryBean();
        factory.setContactPoints(contactPoints);
        factory.setPort(port);
        factory.setLocalDatacenter(localDatacenter);
        factory.setKeyspaceName(keyspaceName);

        return factory;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }
    @Override
    protected String getLocalDataCenter() {
        return localDatacenter;
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Bean
    public CassandraAdminTemplate cassandraAdminTemplate() {
        return new CassandraAdminTemplate(cqlSession());
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
