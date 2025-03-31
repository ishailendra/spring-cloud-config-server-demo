package dev.techsphere.ms.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

    @Configuration
    @EnableJpaRepositories(
            basePackages = "dev.techsphere",
            entityManagerFactoryRef = "entityManager",
            transactionManagerRef = "transactionManager"
    )
    public class DatabaseConfig {

        @Autowired
        private VaultPropertiesLoader vaultProps;

        @Autowired
        private Environment env;

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManager() {
            final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(dataSource());
            em.setPackagesToScan("dev.techsphere");

            final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            em.setJpaVendorAdapter(vendorAdapter);
            final HashMap<String, Object> properties = new HashMap<>();
            properties.put("hibernate.hbm2ddl.auto", "create-drop");
            properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            properties.put("hibernate.show_sql", true);
            properties.put("hibernate.format_sql", true);
            em.setJpaPropertyMap(properties);

            return em;
        }

        @Bean
        public DataSource dataSource() {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl(env.getProperty("spring.datasource.url"));
            config.setUsername(env.getProperty("spring.datasource.username"));
            config.setPassword(String.valueOf(vaultProps.getProperty("spring.datasource.password")));
            config.setMaximumPoolSize(Integer.parseInt("10"));
            config.setMaxLifetime(Long.parseLong("1800000"));
            config.setPoolName("HikariPool");
            config.setConnectionTimeout(Long.parseLong("30000"));
            config.setMinimumIdle(Integer.parseInt("10"));
            config.setIdleTimeout(Long.parseLong("600000"));

            return new HikariDataSource(config);
        }

        @Bean
        public PlatformTransactionManager transactionManager() {
            final JpaTransactionManager transactionManager = new JpaTransactionManager();
            transactionManager.setEntityManagerFactory(entityManager().getObject());
            return transactionManager;
        }
    }

