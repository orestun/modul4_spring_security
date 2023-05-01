package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.HibernateValidationException;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.exception.ObjectAlreadyExistsException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TagServiceTest {

    @Mock
    TagRepository tagRepository;

    @InjectMocks
    TagService tagService;

    @BeforeEach
    public void setUp(){
        tagService = new TagService(tagRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "1, tag1",
            "2, tag2",
            "3, tag3"
    })
    public void successAddingNewTagTest(Long id, String name){
        Tag tag = new Tag(id, name);
        when(tagRepository.existsByName(name)).thenReturn(false);
        when(tagRepository.save(tag)).thenReturn(tag);
        assertEquals(tagService.addNewTag(tag),tag);
    }

    @ParameterizedTest
    @CsvSource({
            "1, ",
            "2,     "
    })
    public void nullNameFieldWhileAddingNewTagTest(Long id, String name){
        Tag tag = new Tag(id, name);
        when(tagRepository.existsByName(name)).thenReturn(false);
        when(tagRepository.save(tag)).thenReturn(tag);
        HibernateValidationException exception =
                assertThrows(HibernateValidationException.class,
                        ()->tagService.addNewTag(tag));
        assertTrue(exception.getMessage().
                        contains("Name field for tag can`t be null; "));
    }

    @ParameterizedTest
    @CsvSource({
            "1, tag_name_tag_name_tag_name_tag_name",
            "2, too_high_length_for_tag_name_field"
    })
    public void tooHighSizeForNameFieldWhileAddingNewTagTest(Long id, String name){
        Tag tag = new Tag(id, name);
        when(tagRepository.existsByName(name)).thenReturn(false);
        when(tagRepository.save(tag)).thenReturn(tag);
        HibernateValidationException exception =
                assertThrows(HibernateValidationException.class,
                        ()->tagService.addNewTag(tag));
        assertTrue(exception.getMessage().
                contains("Name field length can`t be more than 30 chars; "));
    }

    @ParameterizedTest
    @CsvSource({
            "1, tag1",
            "2, tag2"
    })
    public void haveAlreadyExistentTagForNAddingNewTagTest(Long id, String name){
        Tag tag = new Tag(id, name);
        when(tagRepository.existsByName(name)).thenReturn(true);
        ObjectAlreadyExistsException exception =
                assertThrows(ObjectAlreadyExistsException.class,
                        ()->tagService.addNewTag(tag));
        assertEquals(exception.getMessage(),
                String.format("Tag with name '%s' has already exists",tag.getName()));
    }

    @Test
    public void getAllTagsTest(){
        Page<Tag> tags = new PageImpl<>(
                List.of(
                        new Tag(),
                        new Tag(),
                        new Tag()));
        when(tagRepository.findAll(PageRequest.of(0,10))).thenReturn(tags);
        assertEquals(tags.stream().toList(),
                tagService.getAllTags(1,10));
    }

    @Test
    public void getTheMostWidelyTagOfUserWithTheHighestTotalOrderCostTest(){
        Tag tag = new Tag();
        when(tagRepository.getTheMostWidelyTagOfUserWithTheHighestTotalOrderCost()).thenReturn(tag);
        assertEquals(
                tagService.getTheMostWidelyTagOfUserWithTheHighestTotalOrderCost(),
                tag);
    }

    @ParameterizedTest
    @CsvSource({
            "3",
            "43",
            "432"
    })
    public void successDeletingTagById(Long id){
        when(tagRepository.existsById(id)).thenReturn(true);
        assertEquals(
                id,
                tagService.deleteTag(id));
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "131",
            "-44"
    })
    public void nonExistentTagForDeletingTagById(Long id){
        when(tagRepository.existsById(id)).thenReturn(false);
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                ()->tagService.deleteTag(id));
        assertEquals(
                String.format("There is not tag with (id=%d)",id),
                exception.getMessage());
    }
}
