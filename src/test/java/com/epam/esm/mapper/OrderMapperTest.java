package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderMapperTest {

    @InjectMocks
    OrderMapper orderMapper;

    @Test
    void toOrderDtoTest(){
        LocalDateTime localDateTime = LocalDateTime.now();
        Order order = new Order(1L, 1L, 1L, new BigDecimal(100), localDateTime);
        OrderDto orderDto = new OrderDto(1L, 1L, 1L, new BigDecimal(100), localDateTime);
        assertEquals(orderDto, orderMapper.toOrderDto(order));
    }
}
