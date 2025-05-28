package com.spring.batch.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.spring.batch.repositories.payment",
        entityManagerFactoryRef = "paymentsEntityManagerFactory",
        transactionManagerRef = "paymentsTransactionManager"
)
public class PaymentsRepoConfig {}
