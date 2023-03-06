package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.hateoas.UserHateoas;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("user")
public class UserController {
    @Autowired
    private final UserService userService;

    private final UserMapper userMapper = new UserMapper();
    private final OrderMapper orderMapper = new OrderMapper();

    /**
     * Controller GET method that return all got users,
     * by calling a method of service layer
     * @see UserService#getAllUsers(Integer, Integer)
     * @param page page number to be viewed (default value = 1)
     * @param pageSize number of objects that are going to be view in one page
     *                 (default value = 10)
     *
     * @return list of users got from service layer
     * */
    @GetMapping
    public List<UserDto> getAllUsers(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page-size", defaultValue = "10") Integer pageSize
    ){
        return userService.
                getAllUsers(page, pageSize).
                stream().map(userMapper::toDto).
                toList();
    }

    /**
     * Controller POST method that add a new user,
     * by calling a method of service layer
     * @see UserService#addNewUser(User)
     * @param user user object that are going to be added in DB
     *
     * @return user object that was added in DB
     * */
    @PostMapping
    public UserDto addNewUser(@RequestBody UserDto user){
        User createdUser = userService.
                addNewUser(userMapper.toUser(user));
        return UserHateoas.
                linksForAddingNewUser(userMapper.
                        toDto(createdUser));
    }

    @GetMapping("{user-id}/orders")
    public List<OrderDto> getAllOrderByUserId(
            @PathVariable(value = "user-id") Long userId,
            @Nullable @RequestParam(value = "page",defaultValue = "1") Integer page,
            @Nullable @RequestParam(value = "page-size",defaultValue = "10") Integer pageSize){
        return userService.
                getOrdersByUserId(
                        userId,
                        page,
                        pageSize).
                stream().
                map(orderMapper::toOrderDto).
                toList();
    }

    @GetMapping("{user-id}/order/{order-id}")
    public OrderDto getOrderByIdForUserByUserId(
            @PathVariable(value = "user-id") Long userId,
            @PathVariable(value = "order-id") Long orderId){
        return orderMapper.
                toOrderDto(userService.
                        getOrderByIdForUserId(orderId,userId));
    }
}
