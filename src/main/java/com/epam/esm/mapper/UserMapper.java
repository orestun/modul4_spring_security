package com.epam.esm.mapper;


import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;

public class UserMapper {

    public UserDto toDto(User user){
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPassword());
    }
    public User toUser(UserDto userDTO){
        return new User(
                userDTO.getName(),
                userDTO.getSurname(),
                userDTO.getEmail());
    }
}
