package com.cypros.comparator.api.security.config;

import com.cypros.comparator.api.services.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class mobileSecurityConfig extends WebSecurityConfigurerAdapter {
  private final String[] PUBLIC_ENDPOINTS= {
      "/api/v*/**"
  };

  private final UserServices userServices;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(
        SessionCreationPolicy.STATELESS).and().
    authorizeRequests()

        .antMatchers(PUBLIC_ENDPOINTS).permitAll()
        .anyRequest().authenticated().and().httpBasic();

  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(bCryptPasswordEncoder);
    provider.setUserDetailsService(userServices);
    return provider;


  }
}
