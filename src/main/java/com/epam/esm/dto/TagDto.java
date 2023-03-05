package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagDto extends RepresentationModel<TagDto> {
    private Long id;
    private String name;

    public TagDto(String name) {
        this.name = name;
    }
}
