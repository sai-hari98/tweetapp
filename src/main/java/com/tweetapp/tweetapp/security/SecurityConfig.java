package com.tweetapp.tweetapp.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable().httpBasic().and().authorizeRequests()
                .antMatchers("/swagger*/**").permitAll()
                .antMatchers("/api/v1.0/tweets/login").permitAll()
                .antMatchers("/api/v1.0/tweets/register").permitAll()
                .antMatchers("/api/v1.0/tweets/forgot").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .anyRequest().authenticated().and()
                .addFilter(new JwtAuthorizationFilter(authenticationManager()));
    }
}
