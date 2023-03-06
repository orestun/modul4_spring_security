package com.epam.esm.hateoas;

import com.epam.esm.dto.TagDto;
import org.springframework.hateoas.Link;

import java.util.List;

/**
 * @author orest uzhytchak
 * Hateoas class that add links to tag object
 * */
public class TagHateoas {

    private static final Link getAllTagsLink =
            Link.of("http://localhost:8080/tag").
                    withRel("Get all tags").
                    withType("GET");

    private static Link deleteTagByIdLink(Long id) {
        return  Link.of(String.format("http://localhost:8080/tag/%d",id)).
                withRel("Delete tag by id").
                withType("DELETE");
    }

    private static final Link getMostWidelyUsedTag =
            Link.of("http://localhost:8080/tag/most-widely-used-tag").
                    withRel("Get most widely tag for user with the highest cost of all orders").
                    withType("GET");

    /**
     * Hateoas method that add links to Tag objects got as
     * result of method {@link TagController#getAllTags(Integer, Integer)}
     *
     * @param tags list of tags that was get from DB
     *
     * @return tags list with links
     * */
    public static List<TagDto> linksForGettingAllTags(
            List<TagDto> tags){
        for(TagDto tagDTO: tags){
            tagDTO.add(deleteTagByIdLink(tagDTO.getId()));
            tagDTO.add(getMostWidelyUsedTag);
        }
        return tags;
    }

    /**
     * Hateoas method that add links to Tag objects got as
     * result of method {@link TagController#addNewTag(TagDTO)}
     *
     * @param tagDTO tag that was added to DB
     *
     * @return tag with links
     * */
    public static TagDto linksForAddingNewTag(
            TagDto tagDTO){
        tagDTO.add(getAllTagsLink);
        tagDTO.add(deleteTagByIdLink(tagDTO.getId()));
        tagDTO.add(getMostWidelyUsedTag);
        return tagDTO;
    }
}
