package com.epam.esm.service;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.HibernateValidationException;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.exception.ObjectAlreadyExistsException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.utils.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    UserService userService;

    @BeforeEach
    public void setup(){
        userService = new UserService(new BCryptPasswordEncoder(), userRepository, orderRepository);
    }

    @Test
    public void getAllGiftCertificateTest(){
        Page<User> users = new PageImpl<>(
                List.of(new User(),
                        new User(),
                        new User()));
        when(userRepository.
                findAll(PageRequest.of(0, 10))).thenReturn(users);
        assertEquals(users.stream().toList(),
                userService.getAllUsers(1,10));
    }

    @ParameterizedTest
    @CsvSource({
            "1, u_name, u_surname, e@gmail.com, password",
            "2, u_name2, u_surname2, e1@gmail.com, secret001",
    })
    public void successAddingNewUserTest(Long id, String name, String surname, String email, String password){
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(user,
                userService.addNewUser(user));
    }

    @ParameterizedTest
    @CsvSource({
            "1, , u_surname, e@gmail.com",
            "2, u_name2, , e1@gmail.com",
            "3, u_name, u_surname, e_gmail.com"
    })
    public void badDataWhileAddingNewUserTest(Long id, String name, String surname, String email){
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        HibernateValidationException exception = assertThrows(HibernateValidationException.class,
                ()->userService.addNewUser(user));
        assertEquals(HibernateValidationException.class,
                exception.getClass());
    }

    @ParameterizedTest
    @CsvSource({
            "1, name, u_surname, e@gmail.com",
            "2, name2, surname, e1@gmail.com",
            "3, u_name, u_surname, e@gmail.com"
    })
    public void alreadyExistentUserWithSuchEmailWhileAddingNewUserTest(Long id, String name, String surname, String email){
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        when(userRepository.existsByEmail(email)).thenReturn(true);
        ObjectAlreadyExistsException exception = assertThrows(ObjectAlreadyExistsException.class,
                ()->userService.addNewUser(user));
        assertEquals(exception.getMessage(),
                String.format("There is user with - '%s' email",user.getEmail()));
    }

    @ParameterizedTest
    @CsvSource({
            "1, u_name, u_surname, e@gmail.com,",
            "2, u_name2, u_surname2, e1@gmail.com,",
    })
    void addingNewUserWithNullPasswordFieldTest(Long id, String name, String surname, String email, String password){
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        HibernateValidationException exception =
                assertThrows(HibernateValidationException.class,
                        ()-> userService.addNewUser(user));
        assertEquals(exception.getMessage(), "Password can`t be null");
    }


    @ParameterizedTest
    @CsvSource({
            "1, u_name, u_surname, e@gmail.com, abc",
            "2, u_name2, u_surname2, e1@gmail.com, 12345",
    })
    void addingNewUserWithTooShortPasswordFieldTest(Long id, String name, String surname, String email, String password){
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        HibernateValidationException exception =
                assertThrows(HibernateValidationException.class,
                        ()-> userService.addNewUser(user));
        assertEquals(exception.getMessage(), "Password length of chars should be more than 8");
    }

    @ParameterizedTest
    @CsvSource({
            "1, u_name, u_surname, e@gmail.com, abcdefgh",
            "2, u_name2, u_surname2, e1@gmail.com, 12345678",
    })
    void successAddingNewUserForOAuth2AuthenticatedUserTest(Long id, String name, String surname, String email, String password){
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(List.of(new Role(Roles.ROLE_USER)));
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(user, userService.addNewUserForOAuth2AuthenticatedUser(user));
    }

    @ParameterizedTest
    @CsvSource({
            "43",
            "90"
    })
    void successGettingOrdersByUserIdTest(long id){
        List<Order> orders =
                List.of(new Order(id, 3L),
                        new Order(id, 76L));
        when(userRepository.existsById(id)).thenReturn(true);
        when(orderRepository.
                findByUserID(
                        id,
                        PageRequest.of(0, 10))).
                thenReturn(orders);
        assertEquals(orders, userService.getOrdersByUserId(id, 1, 10));
    }

    @ParameterizedTest
    @CsvSource({
            "150",
            "3251"
    })
    void unExistedUserWhileGettingOrdersByUserIdTest(long id){
        when(userRepository.existsById(id)).thenReturn(false);
        ItemNotFoundException exception =
                assertThrows(
                        ItemNotFoundException.class,
                        ()->userService.getOrdersByUserId(id, 1, 10));
        assertEquals(exception.getMessage(), String.format("There is not such user with (id = %d)",id));
    }

    @ParameterizedTest
    @CsvSource({
            "12, 31",
            "13, 1"
    })
    void successGettingOrderByOrderIdForUserByUserIdTest(long userId, long orderId){
        Order order = new Order(userId, 54L);
        when(userRepository.existsById(userId)).thenReturn(true);
        when(orderRepository.existsById(orderId)).thenReturn(true);
        when(orderRepository.findOrderByOrderIdForUserByUserId(userId, orderId)).thenReturn(order);
        assertEquals(order, userService.getOrderByIdForUserId(orderId, userId));
    }

    @ParameterizedTest
    @CsvSource({
            "12, 31",
            "13, 1"
    })
    void unExistedUserGettingOrderByOrderIdForUserByUserIdTest(long userId, long orderId){
        when(userRepository.existsById(userId)).thenReturn(false);
        ItemNotFoundException exception =
                assertThrows(
                        ItemNotFoundException.class,
                        ()->userService.getOrderByIdForUserId(orderId, userId));
        assertEquals(exception.getMessage(), String.format("There is not such user with (id = %d)",userId));
    }

    @ParameterizedTest
    @CsvSource({
            "12, 31",
            "13, 1"
    })
    void unExistedOrderGettingOrderByOrderIdForUserByUserIdTest(long userId, long orderId){
        when(userRepository.existsById(userId)).thenReturn(true);
        when(orderRepository.existsById(orderId)).thenReturn(false);
        ItemNotFoundException exception =
                assertThrows(
                        ItemNotFoundException.class,
                        ()->userService.getOrderByIdForUserId(orderId, userId));
        assertEquals(exception.getMessage(), String.format("There is not such order with (id = %d)", orderId));
    }

}
