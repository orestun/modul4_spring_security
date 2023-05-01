package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserID(Long userID, Pageable pageable);


    @Query("select o from Order o where o.id = ?1 and o.userID=?2")
    Order findOrderByOrderIdForUserByUserId(Long orderId, Long userId);
}
