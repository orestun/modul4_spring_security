package com.epam.esm.service;

import com.epam.esm.dto.AuthenticationBody;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.BadAuthenticationData;
import com.epam.esm.exception.ObjectAlreadyExistsException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.utils.Roles;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthenticationServiceTest {

    @InjectMocks
    AuthenticationService authenticationService;
    @Mock
    JwtService jwtService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    GoogleTokenService googleTokenService;
    @Mock
    UserRepository userRepository;
    @Mock
    UserService userService;
    @Mock
    PasswordEncoder passwordEncoder;

    UserMapper userMapper = new UserMapper();

    @ParameterizedTest
    @CsvSource({
            "user1987, password123",
            "user2133, supersecure"
    })
    void successAuthenticationTest(String username, String password){
        AuthenticationBody authBody = new AuthenticationBody(username, password);
        Authentication testingAuthentication = new TestingAuthenticationToken(username, password);
        String jwtToken = "jwt-generated-testing-token";
        testingAuthentication.setAuthenticated(true);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authBody.getUsername(), authBody.getPassword())))
                .thenReturn(
                        testingAuthentication);
        when(jwtService.generateToken(authBody.getUsername()))
                .thenReturn(jwtToken);
        assertEquals(
                jwtToken,
                authenticationService.authenticate(authBody));
    }

    @ParameterizedTest
    @CsvSource({
            "user1987, password123",
            "user2133, supersecure"
    })
    void badCredentialsWhileAuthenticationTest(String username, String password){
        AuthenticationBody authBody = new AuthenticationBody(username, password);
        Authentication testingAuthentication = new TestingAuthenticationToken(username, password);
        String jwtToken = "jwt-generated-testing-token";
        testingAuthentication.setAuthenticated(false);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authBody.getUsername(), authBody.getPassword())))
                .thenReturn(
                        testingAuthentication);
        when(jwtService.generateToken(authBody.getUsername()))
                .thenReturn(jwtToken);
        BadAuthenticationData exception =
                assertThrows(
                        BadAuthenticationData.class,
                        ()->authenticationService.authenticate(authBody));
        assertEquals("Bad authentication input data!", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "user1987, password123",
            "user2133, supersecure"
    })
    void badCredentialsWhileTryingToAuthenticateTest(String username, String password){
        AuthenticationBody authBody = new AuthenticationBody(username, password);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authBody.getUsername(), authBody.getPassword())))
                .thenThrow(BadCredentialsException.class);
        BadAuthenticationData exception =
                assertThrows(
                        BadAuthenticationData.class,
                        ()->authenticationService.authenticate(authBody));
        assertEquals("Bad input credentials!", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "Google_token41245, user1@gmail.com, Ben, Cross",
            "Google_token123423, user2@gmail.com, Ann, Bread"
    })
    void successGoogleAuthenticationWithoutRegistrationTest(String token, String email, String firthName, String secondName){
        String jwtToken = "jwt-secret-token-98327538956";
        when(googleTokenService.isTokenValid(token)).thenReturn(true);
        when(googleTokenService.extractEmail(token)).thenReturn(email);
        when(googleTokenService.extractFirthName(token)).thenReturn(firthName);
        when(googleTokenService.extractSecondName(token)).thenReturn(secondName);
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(jwtService.generateToken(email)).thenReturn(jwtToken);

        assertEquals(jwtToken, authenticationService.authenticateGoogle(token));
    }

    @ParameterizedTest
    @CsvSource({
            "Google_token4432245, user1@gmail.com, Den, Brown",
            "Google_token1432342, user2@gmail.com, Kate, As"
    })
    void successGoogleAuthenticationWithRegistrationTest(String token, String email, String firthName, String secondName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String jwtToken = "jwt-secret-token-98327538956";
        when(googleTokenService.isTokenValid(token)).thenReturn(true);
        when(googleTokenService.extractEmail(token)).thenReturn(email);
        when(googleTokenService.extractFirthName(token)).thenReturn(firthName);
        when(googleTokenService.extractSecondName(token)).thenReturn(secondName);
        when(userRepository.existsByEmail(email)).thenReturn(false);

        Class<?> c = authenticationService.getClass();
        Method method = c.getDeclaredMethod("createNewUserByGoogleOauth2Data", String.class, String.class, String.class);
        method.setAccessible(true);
        User user = (User) method.invoke(authenticationService, email, firthName, secondName);

        when(userService.addNewUserForOAuth2AuthenticatedUser(user)).thenReturn(user);
        when(jwtService.generateToken(email)).thenReturn(jwtToken);

        assertEquals(jwtToken, authenticationService.authenticateGoogle(token));
    }

    @ParameterizedTest
    @CsvSource({
            "Google_token4432245, user1@gmail.com, Den, Brown",
            "Google_token1432342, user2@gmail.com, Kate, As"
    })
    void badGoogleTokenWhileAuthenticateWithGoogleTest(String token){
        when(googleTokenService.isTokenValid(token)).thenReturn(false);
        BadAuthenticationData exception =
                assertThrows(
                        BadAuthenticationData.class,
                        ()->authenticationService.authenticateGoogle(token));
        assertEquals("Google token is not valid", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "1, Tom, Cat, tom@gmail.com, secretPass",
            "2, Jerry, Mouse, jerry@gmail.com, 19872197"
    })
    void successRegisterUserWithNewEmailTest(long id, String name, String surname, String email, String password){
        UserDto userDto = new UserDto(id, name, surname, email, List.of(Roles.ROLE_USER.name()));
        userDto.setPassword(password);
        userDto.setRepeatPassword(password);
        User user = userMapper.toUser(userDto);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userService.addNewUser(user)).thenReturn(user);
        assertEquals(user, authenticationService.registrateUser(userDto));
    }

    @ParameterizedTest
    @CsvSource({
            "1, Tom, Cat, tom@gmail.com, secretPass",
            "2, Jerry, Mouse, jerry@gmail.com, 19872197"
    })
    void successRegisterUserWithExistedEmailInDBTest(long id, String name, String surname, String email, String password){
        UserDto userDto = new UserDto(id, name, surname, email, List.of(Roles.ROLE_USER.name()));
        User user = userMapper.toUser(userDto);
        userDto.setPassword(password);
        userDto.setRepeatPassword(password);
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(password)).thenReturn(null);
        when(userService.addNewUser(user)).thenReturn(user);

        assertEquals(user, authenticationService.registrateUser(userDto));
    }

    @ParameterizedTest
    @CsvSource({
            "1, Tom, Cat, tom@gmail.com, secretPass, seCretpas",
            "2, Jerry, Mouse, jerry@gmail.com, 19872197, 19871763"
    })
    void passwordDifferRegisterUserWithExistedEmailInDBTest(long id, String name, String surname, String email, String password, String repeatedPassword){
        UserDto userDto = new UserDto(id, name, surname, email, List.of(Roles.ROLE_USER.name()));
        User user = userMapper.toUser(userDto);
        userDto.setPassword(password);
        userDto.setRepeatPassword(repeatedPassword);
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(password)).thenReturn(null);
        when(userService.addNewUser(user)).thenReturn(user);
        BadAuthenticationData exception =
                assertThrows(BadAuthenticationData.class,
                        ()->authenticationService.registrateUser(userDto));
        assertEquals("Repeat password differ from password", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "1, Tom, Cat, tom@gmail.com, secretPass",
            "2, Jerry, Mouse, jerry@gmail.com, 19872197"
    })
    void userWithSuchEmailAlreadyExistRegisterTest(long id, String name, String surname, String email, String password){
        UserDto userDto = new UserDto(id, name, surname, email, List.of(Roles.ROLE_USER.name()));
        userDto.setPassword(password);
        userDto.setRepeatPassword(password);

        User user = userMapper.toUser(userDto);

        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(password)).thenReturn(null);
        when(userService.addNewUser(user)).thenReturn(user);

        ObjectAlreadyExistsException exception =
                assertThrows(
                        ObjectAlreadyExistsException.class,
                        ()->authenticationService.registrateUser(userDto));

        assertEquals(String.format("There is object with such email - '%s'",email), exception.getMessage());
    }
}
