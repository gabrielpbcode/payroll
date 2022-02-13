package com.gabriel.payroll.security;

import com.gabriel.payroll.security.filter.JwtAccessDeniedHandler;
import com.gabriel.payroll.security.filter.JwtAuthenticationEntryPoint;
import com.gabriel.payroll.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // add security with method level
@RequiredArgsConstructor
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

  private final PayrollCorsConfiguration payrollCorsConfiguration;
  private final JwtAuthorizationFilter jwtAuthorizationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final UserDetailsService userDetailsService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .cors().configurationSource(payrollCorsConfiguration)
      .and()
        .sessionManagement().sessionCreationPolicy(STATELESS) // the server will not keep track from who is loged in. We have a token for that
      .and()
        .authorizeRequests()
        .antMatchers(SecurityConstants.PUBLIC_URLS).permitAll()
        .anyRequest()
        .authenticated()
      .and()
        .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
      .and()
        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
