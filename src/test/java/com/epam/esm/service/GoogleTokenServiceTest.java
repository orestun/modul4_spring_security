package com.epam.esm.service;

import com.epam.esm.feignClient.CustomFeignClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GoogleTokenServiceTest {

    @InjectMocks
    GoogleTokenService googleTokenService;

    @Mock
    CustomFeignClient feignClient;
    private final String googleToken =  "Google_token";

    private final Map<String, String> properties =
            Map.of("email", "user@gmail.com",
                    "given_name", "Name",
                    "family_name", "Surname",
                    "aud", "bad-client-secret");

    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "given_name";
    private static final String SECOND_NAME = "family_name";
    private static final String AUD = "aud";
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    private static final String ID_TOKEN = "id_token";
    @Test
    void extractEmailTest(){
        String email = "user@gmail.com";
        when(feignClient
                .verifyTokenAndGetMapOfClaims(Map.of(ID_TOKEN, googleToken))).thenReturn(properties);
        assertEquals(email,
                googleTokenService.extractEmail(googleToken));
    }

    @Test
    void extractFirthNameTest(){
        String firthName = "Name";
        when(feignClient
                .verifyTokenAndGetMapOfClaims(Map.of(ID_TOKEN, googleToken))).thenReturn(properties);
        assertEquals(firthName,
                googleTokenService.extractFirthName(googleToken));
    }

    @Test
    void extractSecondNameTest(){
        String secondName = "Surname";
        when(feignClient
                .verifyTokenAndGetMapOfClaims(Map.of(ID_TOKEN, googleToken))).thenReturn(properties);
        assertEquals(secondName,
                googleTokenService.extractSecondName(googleToken));
    }

    @Test
    void badGoogleTokenValidation(){
        when(feignClient
                .verifyTokenAndGetMapOfClaims(Map.of(ID_TOKEN, googleToken))).thenReturn(properties);
        assertFalse(googleTokenService.isTokenValid(googleToken));
    }
}
