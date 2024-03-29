package com.epam.esm.config;

import com.epam.esm.feignClient.CustomFeignClient;
import com.epam.esm.feignClient.FeignClientDecoderFromJsonToMap;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserDetailsServiceImpl;
import com.google.gson.Gson;
import feign.Feign;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    @Autowired
    UserRepository userRepository;
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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CustomFeignClient customFeignClient(){
        return Feign.
                    builder().
                    decoder(new FeignClientDecoderFromJsonToMap(new Gson())).
                    encoder(new JacksonEncoder()).
                    target(CustomFeignClient.class, "https://oauth2.googleapis.com/tokeninfo");
    }
}
