package com.epam.esm.mapper;


import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GiftCertificateMapper {

    private static final TagMapper tagMapper = new TagMapper();

    public GiftCertificateDto toDto(GiftCertificate giftCertificate){
        GiftCertificateDto giftCertificateDTO =
                new GiftCertificateDto(
                        giftCertificate.getName(),
                        giftCertificate.getDescription(),
                        giftCertificate.getDuration(),
                        giftCertificate.getPrice(),
                        giftCertificate.getCreateDate(),
                        giftCertificate.getUpdateDate());
        giftCertificateDTO.
                setTags(
                        toTagsDtoSet(giftCertificate.getTags()));
        giftCertificateDTO.setId(giftCertificate.getId());
        return giftCertificateDTO;
    }

    public GiftCertificate toGiftCertificate(GiftCertificateDto giftCertificateDTO){
        GiftCertificate giftCertificate =
                new GiftCertificate(
                        giftCertificateDTO.getName(),
                        giftCertificateDTO.getDescription(),
                        giftCertificateDTO.getDuration(),
                        giftCertificateDTO.getPrice());
        if(giftCertificateDTO.getTags()!=null){
            giftCertificate.
                    setTags(
                            toTagsSet(giftCertificateDTO.getTags()));
        }else{
            giftCertificate.setTags(new HashSet<>());
        }

        return giftCertificate;
    }

    private static Set<TagDto> toTagsDtoSet(Set<Tag> tagSet){
        Set<TagDto> tagsDTOSet = new HashSet<>();
        for(Tag tag: tagSet){
            tagsDTOSet.add(tagMapper.toDto(tag));
        }
        return tagsDTOSet;
    }

    private static Set<Tag> toTagsSet(Set<TagDto> tagDTOSet){
        return tagDTOSet.
                stream().
                map(tagMapper::toTag).
                collect(Collectors.toSet());
    }
}
