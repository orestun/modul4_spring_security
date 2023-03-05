package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByName(String name);
    boolean existsByName(String name);

    @Query(value = "SELECT t.id, t.name " +
            "FROM tag AS t " +
            "JOIN certificate_tag AS gct " +
            "JOIN gift_certificate AS gc " +
            "JOIN orders AS o " +
            "JOIN user AS u " +
            "ON t.id = gct.tag_id " +
            "AND gc.id = gct.certificate_id " +
            "AND o.gift_certificateid = gc.id " +
            "AND o.userid = u.id " +
            "WHERE u.id = (" +
            "SELECT user.id " +
            "FROM orders " +
            "JOIN user " +
            "ON userid = user.id " +
            "GROUP BY user.id " +
            "ORDER BY SUM(orders.cost) DESC " +
            "LIMIT 0,1) " +
            "GROUP BY t.id " +
            "ORDER BY COUNT(t.id) DESC\n" +
            "LIMIT 0,1;", nativeQuery = true)
    Tag getTheMostWidelyTagOfUserWithTheHighestTotalOrderCost();
}
