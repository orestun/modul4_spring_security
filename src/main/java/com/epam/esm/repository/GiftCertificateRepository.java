package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {
    @Query("select c from GiftCertificate c where c.name like ?1")
    Page<GiftCertificate> findByPartOfName(String name, Pageable pageable);



    @Query("select c from GiftCertificate c where c.description like ?1")
    Page<GiftCertificate> findByPartOfDescription(String description,Pageable pageable);


    @Query(value = "SELECT DISTINCT (gc.id), gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.update_date " +
            "FROM gift_certificate gc " +
            "JOIN certificate_tag gct " +
            "JOIN tag t " +
            "ON gc.id = gct.certificate_id " +
            "AND t.id = gct.tag_id " +
            "AND t.name IN (:tags) " +
            "LIMIT :pageFrom,:pageTo", nativeQuery = true)
    List<GiftCertificate> getGiftCertificateBySeveralTags(
            @Param(value = "tags") Set<String> tags,
            @Param(value = "pageFrom") Integer pageFrom,
            @Param(value = "pageTo") Integer pageTo);

    Optional<GiftCertificate> findById(Long aLong);

    boolean existsById(Long id);
}
