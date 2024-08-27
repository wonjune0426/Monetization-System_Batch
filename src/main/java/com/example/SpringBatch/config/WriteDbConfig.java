package com.example.springbatch.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({JpaProperties.class, HibernateProperties.class})
@EnableJpaRepositories(
        basePackages = "com.example.springbatch.repository.write",
        entityManagerFactoryRef = "writeEntityManagerFactory",
        transactionManagerRef = "writeTransactionManager"
)
public class WriteDbConfig {
    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.write")
    public HikariConfig writeConfig(){return new HikariConfig();}

    @Bean(name="writeDataSource")
    public DataSource writeDataSource(){
        return new LazyConnectionDataSourceProxy(new HikariDataSource(writeConfig()));
    }

    @Bean(name="writeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean writeEntityManagerFactory(@Qualifier("writeDataSource") DataSource writeDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(writeDataSource);
        em.setJpaPropertyMap(
                hibernateProperties.determineHibernateProperties(
                        jpaProperties.getProperties(), new HibernateSettings())
        );
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPackagesToScan("com.example.springbatch.entity");
        em.setPersistenceUnitName("write");
        return em;
    }

    @Bean(name="writeTransactionManager")
    public PlatformTransactionManager writeTransactionManager(@Qualifier("writeEntityManagerFactory") EntityManagerFactory writeEntityManagerFactory) {
        return new JpaTransactionManager(writeEntityManagerFactory);
    }

}
