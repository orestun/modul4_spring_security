package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "orders")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonView
    private Long userID;
    @JsonView
    private Long giftCertificateID;
    @JsonView
    @Digits(integer=8, fraction=2)
    private BigDecimal cost;
    @JsonView
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime timeOfPurchase;

    public Order(Long userID, Long giftCertificateID) {
        this.userID = userID;
        this.giftCertificateID = giftCertificateID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return userID.equals(order.userID) && giftCertificateID.equals(order.giftCertificateID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, giftCertificateID);
    }
}
