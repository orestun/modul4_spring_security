package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;

public class OrderMapper {

    public OrderDto toOrderDto(Order order){
        return new OrderDto(
                order.getId(),
                order.getUserID(),
                order.getGiftCertificateID(),
                order.getCost(),
                order.getTimeOfPurchase());
    }
}
