package com.epam.esm.service;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserDetailsImpl;
import com.epam.esm.exception.ItemNotFoundException;
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
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserDetailsServiceTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;
    @Mock
    UserRepository userRepository;

    @ParameterizedTest
    @CsvSource({
            "Mikey, Mouse, mm@disney.com, MMOfficial",
            "Donald, DUCK, dd@disney.com, DDsecret"
    })
    void successLoadingUserByUsername(String name, String surname, String email, String password){
    User user = new User(name, surname, email, password);
    user.setRoles(List.of(new Role(Roles.ROLE_USER)));
    UserDetails userDetails = new UserDetailsImpl(user);
    when(userRepository.findByEmail(email)).thenReturn(user);
    assertEquals(userDetails, userDetailsService.loadUserByUsername(email));
    }

    @ParameterizedTest
    @CsvSource({
            "Mikey, Mouse, mm@disney.com, MMOfficial",
            "Donald, DUCK, dd@disney.com, DDsecret"
    })
    void userAreNotFoundLoadingUserByUsername(String name, String surname, String email, String password){
        User user = new User(name, surname, email, password);
        user.setRoles(List.of(new Role(Roles.ROLE_USER)));
        when(userRepository.findByEmail(email)).thenReturn(null);
        ItemNotFoundException exception =
                assertThrows(
                        ItemNotFoundException.class,
                        ()->userDetailsService.loadUserByUsername(email));
        assertEquals(String.format("User with email - '%s' are NOT FOUND", email), exception.getMessage());
    }
}
