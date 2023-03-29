package com.epam.esm.config;

import com.epam.esm.filter.JwtFilter;
import com.epam.esm.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable().
                authorizeHttpRequests().
                requestMatchers(HttpMethod.GET, "/certificate", "/tag", "/user", "/order").
                permitAll().
                requestMatchers(HttpMethod.POST, "/authenticate", "/authenticate-google", "/sign-up-user").
                permitAll().
                requestMatchers(HttpMethod.GET,"/user/**", "/certificate/**", "/tag/**").
                hasAnyAuthority(Roles.ROLE_ADMIN.name(), Roles.ROLE_USER.name()).
                requestMatchers(HttpMethod.POST, "/order").
                hasAnyAuthority(Roles.ROLE_ADMIN.name(), Roles.ROLE_USER.name()).
                requestMatchers(HttpMethod.POST,"/user", "/certificate", "/tag").
                hasAuthority(Roles.ROLE_ADMIN.name()).
                requestMatchers(HttpMethod.DELETE, "/certificate/**", "/tag/**").
                hasAuthority(Roles.ROLE_ADMIN.name()).
                requestMatchers(HttpMethod.PATCH, "/certificate/**").
                hasAuthority(Roles.ROLE_ADMIN.name()).
                and().
                sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authenticationProvider(authenticationProvider).
                addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).
                build();
    }
}
