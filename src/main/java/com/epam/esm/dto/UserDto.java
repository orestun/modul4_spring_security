package com.epam.esm.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
public class UserDto extends RepresentationModel<UserDto> {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String repeatPassword;
    private List<String> roles;

    public UserDto(Long id, String name, String surname, String email, List<String> roles) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.roles = roles;
    }
}
