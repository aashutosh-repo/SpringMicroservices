package com.spring.batch.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@PropertySource(value = "classpath:datasource.properties")
public class MultiDataSourceConfig {

    @Value("${common.datasource.username}")
    private String username;

    @Value("${common.datasource.password}")
    private String password;

    @Value("${common.datasource.driver-class-name}")
    private String driverClassName;

    // ========== DEMOGRAPHY ==========
    @Bean(name = "demographyDataSource")
    public DataSource demographyDataSource(
            @Value("${demography.datasource.url}") String url) {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

    @Bean(name = "demographyEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean demographyEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("demographyDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.spring.batch.demoentity")
                .persistenceUnit("demography")
                .build();
    }

    @Bean(name = "demographyTransactionManager")
    public PlatformTransactionManager demographyTransactionManager(
            @Qualifier("demographyEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    // ========== SECURITY ==========
//    @Bean(name = "securityDataSource")
//    public DataSource securityDataSource(
//            @Value("${security.datasource.url}") String url) {
//        return DataSourceBuilder.create()
//                .url(url)
//                .username(username)
//                .password(password)
//                .driverClassName(driverClassName)
//                .build();
//    }
//
//    @Bean(name = "securityEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean securityEntityManagerFactory(
//            EntityManagerFactoryBuilder builder,
//            @Qualifier("securityDataSource") DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("com.yourcompany.security.entities")
//                .persistenceUnit("security")
//                .build();
//    }
//
//    @Bean(name = "securityTransactionManager")
//    public PlatformTransactionManager securityTransactionManager(
//            @Qualifier("securityEntityManagerFactory") EntityManagerFactory emf) {
//        return new JpaTransactionManager(emf);
//    }
}
