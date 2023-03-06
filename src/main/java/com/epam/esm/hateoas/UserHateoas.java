package com.epam.esm.hateoas;

import com.epam.esm.dto.UserDto;
import org.springframework.hateoas.Link;

/**
 * @author orest uzhytchak
 * Hateoas class that add links to user object
 * */
public class UserHateoas {
    private static final Link getAllUsersLink =
            Link.of("http://localhost:8080/user").
                    withRel("Get all users").
                    withType("GET");

    private static Link getOrdersForUserByIdLink(Long userId){
        return Link.of(String.format("http://localhost:8080/user/%d/orders",userId)).
                withRel(String.format("Get all orders for user with (id = %d)", userId)).
                withType("GET");
    }


    /**
     * Hateoas method that add links to Tag objects got as
     * result of method {@link com.epam.esm.controller.UserController#addNewUser(UserDto)}
     *
     * @param user user object that was added in DB
     *
     * @return user object with links
     * */
    static public UserDto linksForAddingNewUser(UserDto user){
        user.add(
                getAllUsersLink,
                getOrdersForUserByIdLink(user.getId()));
        return user;
    }
}
