package com.epam.esm.controller;

import com.epam.esm.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("order")
public class OrderController {
    @Autowired
    private final OrderRepository orderRepository;
}
