package com.epam.esm.dto;

import com.epam.esm.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {
    private Long id;
    private Long userId;
    private Long giftCertificateId;
    private BigDecimal cost;
    private LocalDateTime timeOfPurchase;
}
