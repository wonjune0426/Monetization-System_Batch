package com.example.SpringBatch.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    // Batch meta DataSource config
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.batch")
    public HikariConfig batchConfig() {
        return new HikariConfig();
    }


    @Bean("BATCH_DATASOURCE")
    @Primary
    public DataSource batchDataSource() {
        return new LazyConnectionDataSourceProxy(new HikariDataSource(batchConfig()));
    }


    // Service DataSource config
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.service")
    public HikariConfig serviceConfig() {
        return new HikariConfig();
    }

    @Bean(name = "SERVICE_DATASOURCE")
    public DataSource serviceDataSource() {
        return new LazyConnectionDataSourceProxy(new HikariDataSource(serviceConfig()));
    }
}
