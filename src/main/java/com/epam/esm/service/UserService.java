package com.epam.esm.service;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DataValidationHandler;
import com.epam.esm.exception.HibernateValidationException;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.exception.ObjectAlreadyExistsException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final OrderRepository orderRepository;

    /**
     * Service layer method that return all got tags,
     * by calling a method of repository
     * @param page page number to be viewed
     * @param pageSize number of objects that are going to be view in one page
     *
     * @return list of tags got from repository
     * */
    public List<User> getAllUsers(Integer page, Integer pageSize){

        page-=1;

        return userRepository.
                findAll(PageRequest.of(page, pageSize)).
                get().
                collect(Collectors.toList());
    }

    /**
     * Service layer method that return all got tags,
     * by calling a method of repository
     * @param user user object that are going to be added in DB
     *
     * @return user object that was added in DB
     * */
    public User addNewUser(@Valid User user){
        DataValidationHandler<User> dataValidationHandler
                = new DataValidationHandler<>();
        String errors = dataValidationHandler.errorsRepresentation(user);
        if(!errors.isEmpty()){
            throw new HibernateValidationException(
                    errors,
                    40001L);
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw new ObjectAlreadyExistsException(
                    String.format("There is user with - '%s' email",user.getEmail()),
                    40901L);
        }
        return userRepository.save(user);
    }

    public List<Order> getOrdersByUserId(Long userId,
                                         Integer page,
                                         Integer pageSize){
        page-=1;
        if(!userRepository.existsById(userId)){
            throw new ItemNotFoundException(
                    String.format("There is not such user with (id = %d)",userId),
                    40401L);
        }

        return orderRepository.
                findByUserID(
                        userId,
                        PageRequest.of(page, pageSize)).
                stream().
                toList();
    }

    public Order getOrderByIdForUserId(Long orderId,
                                             Long userId
                                         ){
        if(!userRepository.existsById(userId)){
            throw new ItemNotFoundException(
                    String.format("There is not such user with (id = %d)",userId),
                    40401L);
        }

        if(!orderRepository.existsById(orderId)){
            throw new ItemNotFoundException(
                    String.format("There is not such order with (id = %d)",userId),
                    40401L);
        }

        return orderRepository.findOrderByOrderIdForUserByUserId(userId, orderId);
    }
}
