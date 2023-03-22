package com.epam.esm.config;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserDetailsServiceImpl;
import com.epam.esm.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig{
    @Autowired
    UserRepository userRepository;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable().
                authorizeHttpRequests().
                requestMatchers(HttpMethod.GET, "/certificate", "/tag", "/user", "/order").
                permitAll().
                requestMatchers(HttpMethod.GET,"/user/**", "/certificate/**").
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
                httpBasic().
                and().
                build();
    }

    @Bean
    public UserDetailsServiceImpl userDetailsService(){
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(encoderBCrypt());
        return authProvider;
    }

    @Bean
    PasswordEncoder encoderBCrypt(){
        return new BCryptPasswordEncoder();
    }
}
