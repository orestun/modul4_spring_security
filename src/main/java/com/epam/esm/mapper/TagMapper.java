package com.epam.esm.mapper;


import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;

public class TagMapper {
    public TagDto toDto(Tag tag){
        TagDto tagDTO = new TagDto(tag.getName());
        tagDTO.setId(tag.getId());
        return tagDTO;
    }

    public Tag toTag(TagDto tagDTO){
        return new Tag(tagDTO.getName());
    }
}
