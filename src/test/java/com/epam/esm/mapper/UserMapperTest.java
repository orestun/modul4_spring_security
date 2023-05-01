package com.epam.esm.mapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.utils.Roles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserMapperTest {

    @InjectMocks
    UserMapper userMapper;

    @Test
    void toUserDtoTest(){
        User user = new User(1L, "Sponge", "Bob", "sbs@gmail.com", "", List.of(new Role(Roles.ROLE_USER)));
        UserDto userDto = new UserDto(1L, "Sponge", "Bob", "sbs@gmail.com", List.of(Roles.ROLE_USER.name()));
        assertEquals(userDto, userMapper.toDto(user));
    }
}
