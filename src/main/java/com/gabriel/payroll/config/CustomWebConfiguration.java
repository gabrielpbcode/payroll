package com.gabriel.payroll.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableCaching
@EntityScan(basePackages = { "com.gabriel.payroll.model" })
@EnableJpaRepositories(basePackages = { "com.gabriel.payroll.repository" })
@ComponentScan(basePackages = {
  "com.gabriel.payroll.controller",
  "com.gabriel.payroll.service",
  "com.gabriel.payroll.security",
  "com.gabriel.payroll.listener",
})
public class CustomWebConfiguration implements WebMvcConfigurer {
}
