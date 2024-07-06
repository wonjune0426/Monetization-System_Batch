package com.example.SpringBatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({JpaProperties.class, HibernateProperties.class})
public class TransactionManagerConfig {
    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;
    private final ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProviders;
    private final EntityManagerFactoryBuilder entityManagerFactoryBuilder;


    // Batch meta transaction manager
    @Bean(name = "BATCH_TRANSACTION_MANAGER")
    public PlatformTransactionManager batchTransactionManager(@Qualifier("BATCH_DATASOURCE") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "SERVICE_ENTITY_MANAGER_FACTORY")
    public LocalContainerEntityManagerFactoryBean serviceEntityManagerFactory(@Qualifier("SERVICE_DATASOURCE") DataSource dataSource) {
        return EntityManagerFactoryCreator.builder()
                .properties(jpaProperties)
                .hibernateProperties(hibernateProperties)
                .metadataProviders(metadataProviders)
                .entityManagerFactoryBuilder(entityManagerFactoryBuilder)
                .dataSource(dataSource)
                .packages("com.example.SpringBatch.entity")
                .persistenceUnit("serviceUnit")
                .build()
                .create();
    }


    @Bean(name = "SERVICE_TRANSACTION_MANAGER")
    public PlatformTransactionManager serviceTransactionManager(@Qualifier("SERVICE_ENTITY_MANAGER_FACTORY") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
    }

    @Configuration
    @EnableJpaRepositories(
            basePackages = "com.example.SpringBatch.repository"
            , entityManagerFactoryRef = "SERVICE_ENTITY_MANAGER_FACTORY"
            , transactionManagerRef = "SERVICE_TRANSACTION_MANAGER"
    )
    public static class ServiceJpaConfiguration {
    }


}
