package com.epam.esm.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GiftCertificateMapperTest {
    @InjectMocks
    GiftCertificateMapper giftCertificateMapper;
    @Mock
    TagMapper tagMapper;

    @Test
    void giftCertificateToDtoTest(){
        LocalDate date = LocalDate.of(2000, 12, 10);
        GiftCertificate giftCertificate =
                new GiftCertificate("gc", "gc to shop", 30, new BigDecimal(100));
        giftCertificate.setCreateDate(date);
        giftCertificate.setUpdateDate(date);
        giftCertificate.setTags(Set.of(new Tag()));
        GiftCertificateDto giftCertificateDto =
                new GiftCertificateDto("gc", "gc to shop", 30, new BigDecimal(100), date, date);
        assertEquals(giftCertificateDto, giftCertificateMapper.toDto(giftCertificate));
    }

    @Test
    void giftCertificateFromDtTest(){
        LocalDate date = LocalDate.of(2000, 12, 10);
        GiftCertificateDto giftCertificateDto =
                new GiftCertificateDto("gc", "gc to shop", 30, new BigDecimal(100), date, date);
        GiftCertificate giftCertificate =
                new GiftCertificate("gc", "gc to shop", 30, new BigDecimal(100));
        giftCertificateDto.setTags(Set.of(new TagDto()));
        giftCertificate.setTags(Set.of(new Tag()));
        assertEquals(giftCertificate, giftCertificateMapper.toGiftCertificate(giftCertificateDto));
    }

    @Test
    void giftCertificateFromDtoWithoutTagsTest(){
        LocalDate date = LocalDate.of(2000, 12, 10);
        GiftCertificateDto giftCertificateDto =
                new GiftCertificateDto("gc", "gc to shop", 30, new BigDecimal(100), date, date);
        GiftCertificate giftCertificate =
                new GiftCertificate("gc", "gc to shop", 30, new BigDecimal(100));
        giftCertificate.setTags(new HashSet<>());
        assertEquals(giftCertificate, giftCertificateMapper.toGiftCertificate(giftCertificateDto));
    }
}
