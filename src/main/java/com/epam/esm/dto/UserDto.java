package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;

}
