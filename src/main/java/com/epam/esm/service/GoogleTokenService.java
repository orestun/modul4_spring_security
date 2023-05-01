package com.epam.esm.service;

import com.epam.esm.feignClient.CustomFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
public class GoogleTokenService {
    private final CustomFeignClient feignClient;
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "given_name";
    private static final String SECOND_NAME = "family_name";
    private static final String AUD = "aud";
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    private static final String ID_TOKEN = "id_token";

    public GoogleTokenService(CustomFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    public String extractEmail(String token){
        return feignClient
                .verifyTokenAndGetMapOfClaims(getParamForIdToken(token))
                .get(EMAIL);
    }
    public String extractFirthName(String token){
        return feignClient.
                verifyTokenAndGetMapOfClaims(getParamForIdToken(token)).
                get(FIRST_NAME);
    }

    public String extractSecondName(String token){
        return feignClient.
                verifyTokenAndGetMapOfClaims(getParamForIdToken(token)).
                get(SECOND_NAME);
    }

    public boolean isTokenValid(String token){
        return feignClient.
                verifyTokenAndGetMapOfClaims(getParamForIdToken(token)).
                get(AUD).equals(CLIENT_ID);
    }

    private Map<String, String> getParamForIdToken(String token){
        return Map.of(ID_TOKEN, token);
    }
}
