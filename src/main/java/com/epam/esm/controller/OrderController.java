package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("order")
public class OrderController {
    @Autowired
    private final OrderService orderService;
    private final OrderMapper orderMapper = new OrderMapper();

    /**
     * Controller GET method that return all got orders,
     * by calling a method of service layer
     * @see OrderService#getAllOrders(Integer, Integer)
     * @param page page number to be viewed (default value = 1)
     * @param pageSize number of objects that are going to be view in one page
     *                 (default value = 10)
     *
     * @return list of orders got from service layer
     * */
    @GetMapping
    public List<OrderDto> getAllOrders(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page-size", defaultValue = "10") Integer pageSize){
        return orderService.
                getAllOrders(page, pageSize).
                stream().
                map(orderMapper::toOrderDto).
                toList();
    }

    /**
     * Controller POST method that add a new order,
     * by calling a method of service layer
     * @see OrderService#addNewOrder(Long, Long)
     * @param userID id of user that making an order
     * @param giftCertificateID id of gift certificate that adding to the order
     *
     * @return the object of order that was added
     * */
    @PostMapping
    public OrderDto addNewOrder(@RequestParam("user-ID") Long userID, @RequestParam("gift-certificate-ID") Long giftCertificateID){
        return orderMapper.
                toOrderDto(orderService.addNewOrder(userID, giftCertificateID));
    }
}
