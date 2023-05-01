package com.epam.esm.service;

import com.epam.esm.entity.Order;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    GiftCertificateRepository giftCertificateRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    OrderService orderService;

    @Test
    public void getAllOrdersTest(){
        Page<Order> orders = new PageImpl<>(
                List.of(new Order(),
                        new Order()));
        when(orderRepository.findAll(PageRequest.of(0,10))).thenReturn(orders);
        assertEquals(orders.stream().toList(),
                orderService.getAllOrders(1,10));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "4, 1",
            "1, 3"
    })
    public void successAddingOrderTest(Long userId, Long giftCertificateId){
        Order order = new Order(userId, giftCertificateId);
        when(userRepository.existsById(userId)).thenReturn(true);
        when(giftCertificateRepository.existsById(giftCertificateId)).thenReturn(true);
        when(orderRepository.save(order)).thenReturn(order);
        assertEquals(order,
                orderService.addNewOrder(userId, giftCertificateId));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "4, 1",
            "1, 3"
    })
    public void nonExistentUserIdWhileAddingOrderTest(Long userId, Long giftCertificateId){
        when(userRepository.existsById(userId)).thenReturn(false);
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                ()->orderService.addNewOrder(userId, giftCertificateId));

        assertEquals(
                String.format("There is not user with such (id=%d)",userId),
                exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "4, 1",
            "1, 3"
    })
    public void nonExistentGiftCertificateIdWhileAddingOrderTest(Long userId, Long giftCertificateId){
        when(userRepository.existsById(userId)).thenReturn(true);
        when(giftCertificateRepository.existsById(giftCertificateId)).thenReturn(false);
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                ()->orderService.addNewOrder(userId, giftCertificateId));

        assertEquals(
                String.format("There is not gift certificate with such (id=%d)",giftCertificateId),
                exception.getMessage());
    }

}
