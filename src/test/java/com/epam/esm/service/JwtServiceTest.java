package com.epam.esm.service;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserDetailsImpl;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.utils.Roles;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Key;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class JwtServiceTest {

    ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
    private final String SECRET_KEY = resourceBundle.getString("jwt.secret-key");
    @InjectMocks
    JwtService jwtService = new JwtService(SECRET_KEY);

    public Key reflectionDecodingSecretKeyToBase64(String secretKey) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> reflectionClass = jwtService.getClass();
        Method method = reflectionClass.getDeclaredMethod("decodeSecretKeyToBase64", String.class);
        method.setAccessible(true);
        return (Key) method.invoke(jwtService, secretKey);
    }

    @ParameterizedTest
    @CsvSource({
            "Mikey, Mouse, mm@disney.com, MMOfficial, mm@disney.com",
            "Donald, DUCK, dd@disney.com, DDsecret, dd@disney.com"
    })
    void successJwtTokenValidationTest(String name, String surname, String email, String password, String expectedEmail) throws Throwable {
        User user = new User(name, surname, email, password);
        user.setRoles(List.of(new Role(Roles.ROLE_USER)));
        UserDetails userDetails = new UserDetailsImpl(user);
        String token = jwtService.generateToken(expectedEmail);
        assertTrue(jwtService.validateToken(token, userDetails));
    }


    @ParameterizedTest
    @CsvSource({
            "Mikey, Mouse, mm@disney.com, MMOfficial, mikey@disney.com",
            "Donald, DUCK, dd@disney.com, DDsecret, md@disney.com"
    })
    void badUsernameJwtTokenValidationTest(String name, String surname, String email, String password, String expectedEmail) throws Throwable {
        User user = new User(name, surname, email, password);
        user.setRoles(List.of(new Role(Roles.ROLE_USER)));
        UserDetails userDetails = new UserDetailsImpl(user);
        String token = jwtService.generateToken(expectedEmail);
        assertFalse(jwtService.validateToken(token, userDetails));
    }

    @ParameterizedTest
    @CsvSource({
            "Mikey, Mouse, mm@disney.com, MMOfficial, mm@disney.com"
    })
    void tokenExpiredJwtTokenValidationTest(String name, String surname, String email, String password, String expectedEmail) {
        User user = new User(name, surname, email, password);
        user.setRoles(List.of(new Role(Roles.ROLE_USER)));
        UserDetails userDetails = new UserDetailsImpl(user);
        String token = jwtService.generateToken(expectedEmail);
        MockedStatic<Jwts> utilities = Mockito.mockStatic(Jwts.class);
        utilities.when(() -> Jwts.
                parserBuilder().
                setSigningKey(reflectionDecodingSecretKeyToBase64(SECRET_KEY)).
                build().parseClaimsJws(token).
                getBody()).thenThrow(ExpiredJwtException.class);
        ItemNotFoundException exception =
                assertThrows(
                        ItemNotFoundException.class,
                        () -> jwtService.validateToken(token, userDetails));
        assertEquals("Your jwt token is expired, refresh it", exception.getMessage());
    }
}

