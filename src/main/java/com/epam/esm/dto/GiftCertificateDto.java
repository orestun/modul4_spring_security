package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {
    private Long id;
    private String name;
    private String description;
    private Integer duration;
    private BigDecimal price;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;
    private Set<TagDto> tags;

    public GiftCertificateDto(String name,
                              String description,
                              Integer duration,
                              BigDecimal price,
                              LocalDate createDate,
                              LocalDate lastUpdateDate) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.price = price;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public void setTags(Set<TagDto> tags){
        this.tags = tags;
    }
}
