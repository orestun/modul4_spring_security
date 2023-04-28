package com.epam.esm.controller;

import com.epam.esm.dto.AuthenticationBody;
import com.epam.esm.dto.UserDto;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper = new UserMapper();

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationBody authBody){
        return ResponseEntity.ok(Map.of("token", authenticationService.authenticate(authBody)));
    }


    @PostMapping("authenticate-google")
    public ResponseEntity<?> authenticateGoogle(@RequestParam("id-token") String token){
        return ResponseEntity.ok(
                Map.of("token",
                        authenticationService.authenticateGoogle(token)));
    }

    @PostMapping("sign-up-user")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){
        UserDto user =
                userMapper.
                        toDto(authenticationService.registrateUser(userDto));
        String email = user.getEmail();
        String jwtToken =
                authenticationService.
                        authenticate(
                                new AuthenticationBody(
                                        email,
                                        userDto.getPassword()));
        return ResponseEntity.ok(
                        Map.of(
                                "user", user,
                                "jwt_token", jwtToken));
    }
}
