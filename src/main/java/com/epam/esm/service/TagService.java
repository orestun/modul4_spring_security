package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DataValidationHandler;
import com.epam.esm.exception.HibernateValidationException;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.exception.ObjectAlreadyExistsException;
import com.epam.esm.repository.TagRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagService {
    @Autowired
    private final TagRepository tagRepository;

    /**
     * Service layer method that add a new tag,
     * by calling a method of repository
     * @param tag tag that are going to be added in DB
     *
     * @return tag that was added in DB
     * */
    public Tag addNewTag(@Valid Tag tag){
        DataValidationHandler<Tag> dataValidationHandler
                = new DataValidationHandler<>();
        String errors = dataValidationHandler.errorsRepresentation(tag);
        if(!errors.isEmpty()){
            throw new HibernateValidationException(
                    errors,
                    40001L);
        }
        if(tagRepository.existsByName(tag.getName())){
            throw new ObjectAlreadyExistsException(
                    String.format("Tag with name '%s' has already exists",tag.getName()),
                    40901L);
        }
        return tagRepository.save(tag);
    }

    /**
     * Service layer method that that return list of all tags,
     * by calling a method of repository
     * @param page page number to be viewed
     * @param pageSize number of objects that are going to be view in one page
     *
     * @return list of tags got from repository
     * */
    public List<Tag> getAllTags(Integer page,
                                Integer pageSize){
        page-=1;

        return tagRepository.findAll(PageRequest.of(page, pageSize))
                .get()
                .collect(Collectors.toList());
    }

    public Tag getTheMostWidelyTagOfUserWithTheHighestTotalOrderCost(){
        return tagRepository.
                getTheMostWidelyTagOfUserWithTheHighestTotalOrderCost();
    }

    /**
     * Service layer method that delete tag by id,
     * by calling a method of repository
     * @param id id of tag that are going to be deleted
     *
     * @return id of tag that was deleted
     * */
    public Long deleteTag(Long id){
        if(!tagRepository.existsById(id)){
            throw new ItemNotFoundException(
                    String.format("There is not tag with (id=%d)",id),
                    40401L);
        }
        tagRepository.deleteById(id);
        return id;
    }
}
