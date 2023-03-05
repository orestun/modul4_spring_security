package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.*;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GiftCertificateService {

    @Autowired
    private final TagRepository tagRepository;
    @Autowired
    private final GiftCertificateRepository giftCertificateRepository;

    /**
     * Service layer method that return all got gift certificates,
     * by calling a method of repository layer
     * @param page page number to be viewed
     * @param pageSize number of objects that are going to be view in one page
     *
     * @return list of gift certificates got from repository
     * */
    public List<GiftCertificate> getAllGiftCertificates(Integer page,
                                                        Integer pageSize){
        page-=1;

        return giftCertificateRepository.
                findAll(PageRequest.of(page, pageSize))
                .get()
                .collect(Collectors.toList());
    }

    /**
     * Service layer method that add a new gift certificate,
     * by calling a method of repository. Object have to pass validation
     * to be added in DB
     * @param giftCertificate object of Gift certificate that are going to be added in DB
     *
     * @return object that was added in DB
     * */
    public GiftCertificate addNewGiftCertificate(@Valid GiftCertificate giftCertificate){
        DataValidationHandler<GiftCertificate> dataValidationHandler
                = new DataValidationHandler<>();
        String errors = dataValidationHandler.errorsRepresentation(giftCertificate);
        if(!errors.isEmpty()){
            throw new HibernateValidationException(
                    errors,
                    40001L);
        }
        if(!giftCertificate.getTags().isEmpty()){
            for(Tag tag: giftCertificate.getTags()){
                if(!tagRepository.existsByName(tag.getName())){
                    tagRepository.save(tag);
                }else{
                    tag.setId(tagRepository.
                            findByName(tag.getName()).
                            getId());
                }
            }
        }
        return giftCertificateRepository.save(giftCertificate);
    }

    /**
     * Controller GET method that return sorted gift certificates,
     * by calling a method of repository layer.
     * @param direction asc or desc value for defining sorting direction
     * @param sortingAttribute sorting attribute that mean field for sorting
     * @param page page number to be viewed (default value = 1)
     * @param pageSize number of objects that are going to be view in one page
     *                 (default value = 10)
     *
     * @return list of sorted gift certificates got from repository
     * */
    public List<GiftCertificate> getSortedGiftCertificates(
            String direction,
            String sortingAttribute,
            Integer page,
            Integer pageSize){

        page-=1;

        if (direction.equalsIgnoreCase("asc")
                ||direction.equalsIgnoreCase("desc")){
            PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.valueOf(
                    direction.toUpperCase()), sortingAttribute));
            return giftCertificateRepository.
                    findAll(pageRequest).
                    get().
                    collect(Collectors.toList());
        }else{
            throw new NotAllowedParameterException(
                    "Only ASC or DESC attributes are allowed",
                    40002L);
        }
    }

    /**
     * Service layer method that return sorted gift certificates,
     * by calling a method of repository.
     * @param nameDirection asc or desc value for name sorting attribute
     * @param createDateDirection asc or desc value for create date sorting attribute
     * @param page page number to be viewed
     * @param pageSize number of objects that are going to be view in one page
     *
     * @return list of sorted gift certificates got from repository
     * */
    public List<GiftCertificate> getSortedGiftCertificatesByNameAndCreateDate(
            String nameDirection,
            String createDateDirection,
            Integer page,
            Integer pageSize){

        page-=1;

        if((nameDirection.equalsIgnoreCase("asc") ||nameDirection.equalsIgnoreCase("desc"))
                &&(createDateDirection.equalsIgnoreCase("asc")||createDateDirection.equalsIgnoreCase("desc"))){
            PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.valueOf(nameDirection.toUpperCase()),"name").
                    and(Sort.by(Sort.Direction.valueOf(createDateDirection.toUpperCase()), "createDate")));
            return giftCertificateRepository.
                    findAll(pageRequest).
                    get().
                    collect(Collectors.toList());
        }else{
            throw new NotAllowedParameterException(
                    "Only ASC or DESC attributes are allowed",
                    40002L);
        }
    }

    /**
     * Service layer method that return gift certificates by part of name,
     * by calling a method of repository.
     * @see GiftCertificateRepository#findByPartOfName(String, Pageable)
     * @param name name or part of name of gift certificate that we are going to find
     * @param page page number to be viewed
     * @param pageSize number of objects that are going to be view in one page
     *
     * @return list of sorted gift certificates got by name from repository
     * */
    public List<GiftCertificate> getGiftCertificatesByName(
            String name,
            Integer page,
            Integer pageSize){
        page-=1;
        PageRequest pageRequest = PageRequest.of(page,pageSize);

        return giftCertificateRepository.
                findByPartOfName("%"+name+"%", pageRequest).
                get().
                collect(Collectors.toList());
    }

    /**
     * Service layer method that return gift certificates by part of description,
     * by calling a method of repository.
     * @see GiftCertificateRepository#findByPartOfDescription(String, Pageable)
     * @param description description or part of description of gift certificate that we are going to find
     * @param page page number to be viewed
     * @param pageSize number of objects that are going to be view in one page
     *
     * @return list of sorted gift certificates got by description from repository
     * */
    public List<GiftCertificate> getGiftCertificatesByDescription(
            String description,
            Integer page,
            Integer pageSize){
        page-=1;
        PageRequest pageRequest = PageRequest.of(page,pageSize);

        return giftCertificateRepository.
                findByPartOfDescription("%"+description+"%", pageRequest).
                get().
                collect(Collectors.toList());
    }

    /**
     * Service layer method that return gift certificates by set of tags,
     * by calling a method of repository.
     * @see GiftCertificateRepository#getGiftCertificateBySeveralTags(Set, Integer, Integer)
     * @param tags set of tags of gift certificates that we are going to find
     * @param page page number to be viewed
     * @param pageSize number of objects that are going to be view in one page
     *
     * @return list of gift certificates got by tags got from repository
     * */
    public List<GiftCertificate> getGiftCertificateBySeveralTags(
            Set<String> tags,
            Integer page,
            Integer pageSize){
        return giftCertificateRepository.getGiftCertificateBySeveralTags(
                tags,
                (page*pageSize)-pageSize,
                page*pageSize);
    }

    /**
     * Service layer method that updating a gift certificate by id,
     * by calling a method of repository.
     * @param giftCertificate object of Gift certificate that are going to be updated in DB
     * @param id an id of object that are going to be updated
     *
     * @return object that was updated in DB
     * */
    public GiftCertificate updateGiftCertificate(Long id, @Valid GiftCertificate giftCertificate){
        DataValidationHandler<GiftCertificate> dataValidationHandler
                = new DataValidationHandler<>();
        String errors = dataValidationHandler.errorsRepresentation(giftCertificate);
        if(!errors.isEmpty()){
            throw new HibernateValidationException(
                    errors,
                    40001L);
        }

        if(!giftCertificateRepository.existsById(id)){
            throw new ItemNotFoundException(
                    String.format("There is not gift certificate with (id=%d)",id),
                    40401L);
        }

        giftCertificate.setId(id);
        Optional<GiftCertificate> currentGiftCertificate = giftCertificateRepository.findById(id);
        if(currentGiftCertificate.isPresent()){
            if(!currentGiftCertificate.get().equals(giftCertificate)){
                giftCertificate.setCreateDate(currentGiftCertificate.get().getCreateDate());
                giftCertificate.setTags(currentGiftCertificate.get().getTags());
                return giftCertificateRepository.save(giftCertificate);
            }
        }
        throw new ObjectAlreadyExistsException(
                String.format("You did not input any new field for gift certificate with (id=%d)",id),
                40401L);
    }

    /**
     * Service layer method that updating a gift certificate by id and also
     * by some fields,
     * by calling a method of repository.
     * @param id an id of object that are going to be updated
     * @param name new name value for gift certificate
     * @param description new description value for gift certificate
     * @param duration new duration value for gift certificate
     * @param price new price value for gift certificate
     *
     * @return object that was updated in DB
     * */
    public GiftCertificate updateGiftCertificateBySomeFields(Long id,
                                                             String name,
                                                             String description,
                                                             BigDecimal price,
                                                             Integer duration){
        if(!giftCertificateRepository.existsById(id)){
            throw new ItemNotFoundException(
                    String.format("There is not gift certificate with (id=%d)",id),
                    40401L);
        }

        GiftCertificate giftCertificateFromRepository = giftCertificateRepository.
                findById(id).
                orElse(new GiftCertificate());

        GiftCertificate giftCertificate = new GiftCertificate(
                giftCertificateFromRepository.getId(),
                giftCertificateFromRepository.getName(),
                giftCertificateFromRepository.getDescription(),
                giftCertificateFromRepository.getDuration(),
                giftCertificateFromRepository.getPrice(),
                giftCertificateFromRepository.getCreateDate(),
                giftCertificateFromRepository.getUpdateDate(),
                giftCertificateFromRepository.getTags());

        if (name != null && !name.equals(giftCertificate.getName())){
            giftCertificate.setName(name);
        }
        if (description != null && !description.equals(giftCertificate.getDescription())){
            giftCertificate.setDescription(description);
        }
        if(price != null && !price.equals(giftCertificate.getPrice())){
            giftCertificate.setPrice(price);
        }
        if(duration != null && !duration.equals(giftCertificate.getDuration())){
            giftCertificate.setDuration(duration);
        }

        return giftCertificateRepository.save(giftCertificate);
    }

    /**
     * Service layer method that delete a gift certificate by id,
     * by calling a method of repository.
     * @param id an id of object that are going to be deleted
     * */
    public Long deleteGiftCertificate(Long id){
        if(!giftCertificateRepository.existsById(id)){
            throw new ItemNotFoundException(
                    String.format("There is not gift certificate with (id=%d)",id),
                    40401L);
        }
        giftCertificateRepository.deleteById(id);
        return id;
    }

}
