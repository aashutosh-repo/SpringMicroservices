//package com.spring.core.configuration;
//
//import jakarta.persistence.EntityManagerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableTransactionManagement
//public class DataSourceConfig {
//
//    // ========= SCHEMA 1 =========
//    @Bean(name = "schema1DataSource")
//    @ConfigurationProperties(prefix = "schema1.datasource")
//    public DataSource schema1DataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "schema1EntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean schema1EntityManagerFactory(
//            EntityManagerFactoryBuilder builder,
//            @Qualifier("schema1DataSource") DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("com.yourcompany.schema1.entities")
//                .persistenceUnit("schema1")
//                .build();
//    }
//
//    @Bean(name = "schema1TransactionManager")
//    public PlatformTransactionManager schema1TransactionManager(
//            @Qualifier("schema1EntityManagerFactory") EntityManagerFactory emf) {
//        return new JpaTransactionManager(emf);
//    }
//
//    // ========= SCHEMA 2 =========
//    @Bean(name = "schema2DataSource")
//    @ConfigurationProperties(prefix = "schema2.datasource")
//    public DataSource schema2DataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "schema2EntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean schema2EntityManagerFactory(
//            EntityManagerFactoryBuilder builder,
//            @Qualifier("schema2DataSource") DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("com.yourcompany.schema2.entities")
//                .persistenceUnit("schema2")
//                .build();
//    }
//
//    @Bean(name = "schema2TransactionManager")
//    public PlatformTransactionManager schema2TransactionManager(
//            @Qualifier("schema2EntityManagerFactory") EntityManagerFactory emf) {
//        return new JpaTransactionManager(emf);
//    }
//
//    // ========= SCHEMA 3 =========
//    @Bean(name = "schema3DataSource")
//    @ConfigurationProperties(prefix = "schema3.datasource")
//    public DataSource schema3DataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "schema3EntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean schema3EntityManagerFactory(
//            EntityManagerFactoryBuilder builder,
//            @Qualifier("schema3DataSource") DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("com.yourcompany.schema3.entities")
//                .persistenceUnit("schema3")
//                .build();
//    }
//
//    @Bean(name = "schema3TransactionManager")
//    public PlatformTransactionManager schema3TransactionManager(
//            @Qualifier("schema3EntityManagerFactory") EntityManagerFactory emf) {
//        return new JpaTransactionManager(emf);
//    }
//}
