package com.ugur.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {
  @Bean
  public DataSource datasource() {
    return DataSourceBuilder.create().driverClassName("org.h2.Driver").url("jdbc:h2:mem:testdb").username("n11")
        .password("n11").build();
  }
}
