package com.epam.esm.service;

import com.epam.esm.dto.AuthenticationBody;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.BadAuthenticationData;
import com.epam.esm.exception.ObjectAlreadyExistsException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.utils.Roles;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final GoogleTokenService googleTokenService;
    private final AuthenticationManager authManager;
    private final UserService userService;
    private final UserMapper userMapper = new UserMapper();
    public AuthenticationService(JwtService jwtService, PasswordEncoder passwordEncoder, UserRepository userRepository, GoogleTokenService googleTokenService, AuthenticationManager authManager, UserService userService) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.googleTokenService = googleTokenService;
        this.authManager = authManager;
        this.userService = userService;
    }

    public String authenticate(AuthenticationBody authenticationBody){
        String username = authenticationBody.getUsername();
        Authentication authentication =
                getAndTryAuthentication(authenticationBody);
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(username);
        }else{
            throw new BadAuthenticationData("Bad authentication input data!",40101L);
        }
    }

    private Authentication getAndTryAuthentication(AuthenticationBody authBody){
        try{
            return authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authBody.getUsername(), authBody.getPassword()));
        }catch (BadCredentialsException e){
            throw new BadAuthenticationData("Bad input credentials!",40101L);
        }
    }

    public String authenticateGoogle(String googleToken){
        if(googleTokenService.isTokenValid(googleToken)){
            String email = googleTokenService.extractEmail(googleToken);
            String firthName = googleTokenService.extractFirthName(googleToken);
            String secondName = googleTokenService.extractSecondName(googleToken);
            if(!existUserByUsername(email)){
                User user = createNewUserByGoogleOauth2Data(email, firthName, secondName);
                userService.addNewUserForOAuth2AuthenticatedUser(user);
            }
            return jwtService.generateToken(email);
        }
        else{
            throw new BadAuthenticationData("Google token is not valid", 40101L);
        }
    }

    private boolean existUserByUsername(String username){
        return userRepository.existsByEmail(username);
    }

    private User createNewUserByGoogleOauth2Data(String email, String firthName, String secondName){
        User user = new User(firthName, secondName, email);
        user.setRoles(
                List.of(
                        new Role(Roles.ROLE_USER)));
        return user;
    }

    public User registrateUser(UserDto userDto){
        User user = userMapper.toUser(userDto);
        String password = user.getPassword();
        String repeatPassword = userDto.getRepeatPassword();
        String email = user.getEmail();
        if(isUserWithSuchEmail(email)){
            return registrationForUserWithSuchEmailInDataBase(email, password, repeatPassword);
        }else{
            return userService.addNewUser(user);
        }
    }

    private boolean isUserWithSuchEmail(String email){
        return userRepository.existsByEmail(email);
    }

    private boolean isAuthorizedWithGoogle(String email){
        User user = userRepository.findByEmail(email);
        return user.getPassword() == null;
    }

    private boolean isPasswordEqualToRepeatPassword(String password, String repeatPassword){
        return password.equals(repeatPassword);
    }

    private User registerPasswordToGoogleAuthorizedUser(String email, String password){
        User user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    private User registerUserAuthorizedWithGoogle(String email, String password, String repeatPassword){
        if(isPasswordEqualToRepeatPassword(password, repeatPassword)){
            return registerPasswordToGoogleAuthorizedUser(email, password);
        }else{
            throw new BadAuthenticationData(
                    "Repeat password differ from password",
                    40101L);
        }
    }

    private User registrationForUserWithSuchEmailInDataBase(String email, String password, String repeatPassword){
        if(isAuthorizedWithGoogle(email)){
            return registerUserAuthorizedWithGoogle(email, password, repeatPassword);
        }else {
            throw new ObjectAlreadyExistsException(
                    String.format("There is object with such email - '%s'",email),
                    40901L
            );
        }
    }
}
