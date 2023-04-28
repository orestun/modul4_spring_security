package com.epam.esm.generator;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.utils.Roles;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Random;

/**
 * @author orest uzhytchak
 * A generator class that generate a new random User object
 * */
public class UserGenerator {

    private static final Random random = new Random();

    static final List<String> names = List.of(
            "Tom",
            "Mike",
            "Alex",
            "Den",
            "Bob",
            "Steve",
            "Kate",
            "Ann",
            "Jasper",
            "Victoria",
            "Luna",
            "Tom");

    static final List<String> surnames = List.of(
            "Smith",
            "Johnson",
            "Williams",
            "Brown",
            "Jones",
            "Garcia",
            "Davis",
            "Thomas",
            "Taylor",
            "Clark");


    static public User generateRandomUser(int number){
        User user = new User();
        user.setName(getRandomName());
        user.setSurname(getRandomSurname());
        user.setEmail(generateEmail(user, number));
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setRoles(List.of(new Role(Roles.ROLE_USER)));
        return user;
    }

    private static String getRandomName(){
        return names.
                get(random.
                        nextInt(0, names.size()));
    }

    private static String getRandomSurname(){
        return surnames.
                get(random.
                        nextInt(0, surnames.size()));
    }

    private static String generateEmail(User user, int numberOfUser){
        return String.format("%s%s%d@gmail.com",
                user.getName(),
                user.getSurname(),
                numberOfUser);
    }
}
