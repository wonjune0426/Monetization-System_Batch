package com.example.springbatch.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class BatchDbConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.batch")
    public HikariConfig batchConfig() {
        return new HikariConfig();
    }

    @Bean
    @Primary
    public DataSource batchDataSource() {
        return new LazyConnectionDataSourceProxy(new HikariDataSource(batchConfig()));
    }

    @Bean
    @Primary
    public PlatformTransactionManager batchTransactionManager(DataSource batchDataSource) {
        return new DataSourceTransactionManager(batchDataSource);
    }
}
