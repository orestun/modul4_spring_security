package com.epam.esm.mapper;


import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public UserDto toDto(User user){
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                getUserRoles(user.getRoles()));
    }

    private List<String> getUserRoles(List<Role> roles){
        ArrayList<String> rolesForDto = new ArrayList<>();
        for (Role role: roles) {
            rolesForDto.add(role.getRole());
        }
        return rolesForDto;
    }

    public User toUser(UserDto userDTO){
        return new User(
                userDTO.getName(),
                userDTO.getSurname(),
                userDTO.getEmail(),
                userDTO.getPassword());
    }
}
