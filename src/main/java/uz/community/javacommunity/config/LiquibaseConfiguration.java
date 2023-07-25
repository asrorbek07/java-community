package uz.community.javacommunity.config;


import com.simba.cassandra.cassandra.core.CDBJDBCDriver;
import com.simba.cassandra.dsi.core.impl.DSIDriverFactory;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(value = {
        LiquibaseProperties.class,
        CassandraProperties.class
})
public class LiquibaseConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "spring", name = "liquibase.enabled", matchIfMissing = true)
    public SpringLiquibase liquibase(LiquibaseProperties liquibaseProperties,
                                     CassandraProperties cassandraProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
        liquibase.setDataSource(dataSource(cassandraProperties));
        return liquibase;
    }



    private DataSource dataSource(CassandraProperties cassandraProperties) {
        DSIDriverFactory.setDriverClassName(CDBJDBCDriver.class.getName());
        com.simba.cassandra.jdbc42.DataSource dataSource = new com.simba.cassandra.jdbc42.DataSource();
        String contactPoint = cassandraProperties.getContactPoints().get(0);
        int port = cassandraProperties.getPort();
        String keyspaceName = cassandraProperties.getKeyspaceName();
        dataSource.setURL(String.format("jdbc:cassandra://%s:%d/%s;DefaultKeyspace=%s", contactPoint, port, keyspaceName, keyspaceName));
        return dataSource;
    }
}