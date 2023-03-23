package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationBody {
    private String username;
    private String password;
}
