package com.gabriel.payroll.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Configuration
public class PayrollCorsConfiguration implements CorsConfigurationSource {

  @Override
  public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of("localhost:3000"));
    configuration.setAllowedMethods(List.of("*"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setExposedHeaders(List.of("Authorization"));
    configuration.setMaxAge(3600L);

    return configuration;
  }
}
