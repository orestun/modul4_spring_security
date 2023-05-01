package com.epam.esm.repository;

import com.epam.esm.SpringSecurityApplication;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(classes = SpringSecurityApplication.class)
public class GiftCertificateRepositoryTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    public void setup(){
        List<GiftCertificate> giftCertificateList =
                List.of(
                        new GiftCertificate(1,"car", "gc for car", 30, new BigDecimal(100)),
                        new GiftCertificate(2,"course", "gc for course", 150, new BigDecimal(100)),
                        new GiftCertificate(3,"car", "gc for car", 50, new BigDecimal(200)),
                        new GiftCertificate(4,"computer", "gc for computer", 30, new BigDecimal(100)),
                        new GiftCertificate(5,"books", "gc for books", 90, new BigDecimal(100)));
        giftCertificateRepository.saveAll(giftCertificateList);
    }


    @ParameterizedTest
    @CsvSource({
            "1, true",
            "2, true",
            "7, false",
            "5, true"
    })
    public void isExistentGiftCertificateById(Long id, boolean expected){
        this.setup();
        assertEquals(expected,
            giftCertificateRepository.existsById(id));
    }

    @Test
    public void findGiftCertificatesByName(){
        this.setup();
        List<GiftCertificate> actualGiftCertificateList =
                giftCertificateRepository.findByPartOfName(
                        "car",
                        PageRequest.of(0,10)).stream().toList();
        for(GiftCertificate gc: actualGiftCertificateList){
            gc.setTags(null);
            gc.setCreateDate(null);
            gc.setUpdateDate(null);
        }
        List<GiftCertificate> expectedGiftCertificateList =
                List.of(
                        new GiftCertificate(1,"car", "gc for car", 30, new BigDecimal("100.00")),
                        new GiftCertificate(3,"car", "gc for car", 50, new BigDecimal("200.00")));
        assertEquals(expectedGiftCertificateList, actualGiftCertificateList);
    }

    @Test
    public void findGiftCertificatesByDescription(){
        this.setup();
        List<GiftCertificate> actualGiftCertificateList =
                giftCertificateRepository.findByPartOfDescription(
                        "gc for books",
                        PageRequest.of(0,10)).stream().toList();
        actualGiftCertificateList.get(0).setTags(null);
        actualGiftCertificateList.get(0).setCreateDate(null);
        actualGiftCertificateList.get(0).setUpdateDate(null);
        List<GiftCertificate> expectedGiftCertificateList =
                List.of(
                        new GiftCertificate(5,"books", "gc for books", 90, new BigDecimal("100.00")));

        assertEquals(expectedGiftCertificateList, actualGiftCertificateList);
    }
}
