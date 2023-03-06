package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.hateoas.GiftCertificateHateoas;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.service.GiftCertificateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("certificate")
public class GiftCertificateController {
    @Autowired
    private final GiftCertificateService giftCertificateService;

    private final GiftCertificateMapper giftCertificateMapper = new GiftCertificateMapper();
    /**
     * Controller GET method that return all got gift certificates,
     * by calling a method of service layer
     * @see GiftCertificateService#getAllGiftCertificates(Integer, Integer)
     * @param page page number to be viewed (default value = 1)
     * @param pageSize number of objects that are going to be view in one page
     *                 (default value = 10)
     *
     * @return list of gift certificates got from service layer
     * */
    @GetMapping
    public List<GiftCertificateDto> getAllGiftCertificates(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "page-size", defaultValue = "10") Integer pageSize){
        List<GiftCertificate> giftCertificateList =
                giftCertificateService.getAllGiftCertificates(page, pageSize);
        List<GiftCertificateDto> giftCertificateDTOList =
                giftCertificateList.
                        stream().
                        map(giftCertificateMapper::toDto).
                        toList();

        return GiftCertificateHateoas.
                linksForGettingGiftCertificates(giftCertificateDTOList);
    }

    /**
     * Controller GET method that return sorted gift certificates,
     * by calling a method of service layer. Method will sort by first parameter
     * with sorting attribute.
     * @see GiftCertificateService#getSortedGiftCertificates(String, String, Integer, Integer)
     * @param nameDirection asc or desc value for name sorting attribute
     * @param createDateDirection asc or desc value for create date sorting attribute
     * @param priceDirection asc or desc value for price sorting attribute
     * @param page page number to be viewed (default value = 1)
     * @param pageSize number of objects that are going to be view in one page
     *                 (default value = 10)
     *
     * @return list of sorted gift certificates got from service layer
     * */
    @GetMapping("sort")
    public List<GiftCertificateDto> getSortedGiftCertificates(@Nullable @RequestParam("name") String nameDirection,
                                                              @Nullable @RequestParam("create-date") String createDateDirection,
                                                              @Nullable @RequestParam("price") String priceDirection,
                                                              @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                              @RequestParam(value = "page-size", defaultValue = "10") Integer pageSize){
        List<GiftCertificate> giftCertificateList;
        if(nameDirection!=null){
            giftCertificateList =  giftCertificateService.
                    getSortedGiftCertificates(nameDirection, "name", page, pageSize);
        } else if (createDateDirection!=null) {
            giftCertificateList =  giftCertificateService.
                    getSortedGiftCertificates(createDateDirection, "createDate", page, pageSize);
        } else if (priceDirection!=null) {
            giftCertificateList =  giftCertificateService.
                    getSortedGiftCertificates(priceDirection, "price", page, pageSize);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        List<GiftCertificateDto> giftCertificateDTOList =
                giftCertificateList.
                        stream().
                        map(giftCertificateMapper::toDto).
                        toList();

        return GiftCertificateHateoas.
                linksForSortingGiftCertificates(giftCertificateDTOList);
    }


    /**
     * Controller GET method that return sorted gift certificates,
     * by calling a method of service layer. Method will sort by two parameter
     * <strong>two</strong> parameters at once.
     * @see GiftCertificateService#getSortedGiftCertificatesByNameAndCreateDate(String, String, Integer, Integer)
     * @param nameDirection asc or desc value for name sorting attribute
     * @param createDateDirection asc or desc value for create date sorting attribute
     * @param page page number to be viewed (default value = 1)
     * @param pageSize number of objects that are going to be view in one page
     *                 (default value = 10)
     *
     * @return list of sorted gift certificates got from service layer
     * */
    @GetMapping("double-sort")
    public List<GiftCertificateDto> getSortedGiftCertificatesByNameAndCreateDate(
            @RequestParam(value = "name", defaultValue = "asc") String nameDirection,
            @RequestParam(value = "create-date", defaultValue = "asc") String createDateDirection,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page-size", defaultValue = "10") Integer pageSize){
        List<GiftCertificateDto> giftCertificateDTOList =
                giftCertificateService.
                        getSortedGiftCertificatesByNameAndCreateDate(
                                nameDirection,
                                createDateDirection,
                                page,
                                pageSize).
                        stream().
                        map(giftCertificateMapper::toDto).
                        toList();
        return GiftCertificateHateoas.linksForSortingGiftCertificates(giftCertificateDTOList);
    }

    /**
     * Controller GET method that return gift certificates by part of name,
     * by calling a method of service layer.
     * @see GiftCertificateService#getGiftCertificatesByName(String, Integer, Integer)
     * @param name name or part of name of gift certificate that we are going to find
     * @param page page number to be viewed (default value = 1)
     * @param pageSize number of objects that are going to be view in one page
     *                 (default value = 10)
     *
     * @return list of sorted gift certificates got by name from service layer
     * */
    @GetMapping("find-by-name")
    public List<GiftCertificateDto> getGiftCertificatesByName(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page-size", defaultValue = "10") Integer pageSize){
        List<GiftCertificateDto> giftCertificateDTOList =
                giftCertificateService.
                        getGiftCertificatesByName(
                                name,
                                page,
                                pageSize).
                        stream().
                        map(giftCertificateMapper::toDto).
                        toList();
        return GiftCertificateHateoas.
                linksForGettingGiftCertificatesByNameAndByDescription(giftCertificateDTOList);
    }

    /**
     * Controller GET method that return gift certificates by part of description,
     * by calling a method of service layer.
     * @see GiftCertificateService#getGiftCertificatesByDescription(String, Integer, Integer)
     * @param description description or part of description of gift certificate that we are going to find
     * @param page page number to be viewed (default value = 1)
     * @param pageSize number of objects that are going to be view in one page
     *                 (default value = 10)
     *
     * @return list of gift certificates got by description from service layer
     * */
    @GetMapping("find-by-description")
    public List<GiftCertificateDto> getGiftCertificatesByDescription(
            @RequestParam(value = "description", defaultValue = "") String description,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page-size", defaultValue = "10") Integer pageSize){
        List<GiftCertificateDto> giftCertificateDTOList =
                giftCertificateService.
                        getGiftCertificatesByDescription(
                                description,
                                page,
                                pageSize).
                        stream().
                        map(giftCertificateMapper::toDto).
                        toList();
        return GiftCertificateHateoas.
                linksForGettingGiftCertificatesByNameAndByDescription(giftCertificateDTOList);
    }

    /**
     * Controller GET method that return gift certificates by set of tags,
     * by calling a method of service layer.
     * @see GiftCertificateService#getGiftCertificateBySeveralTags(Set, Integer, Integer)
     * @param tags set of tags of gift certificates that we are going to find
     * @param page page number to be viewed (default value = 1)
     * @param pageSize number of objects that are going to be view in one page
     *                 (default value = 10)
     *
     * @return list of gift certificates got by tags got from service layer
     * */
    @GetMapping("find-by-several-tags")
    public List<GiftCertificateDto> getGiftCertificateBySeveralTags(
            @RequestParam("tags") String[] tags,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page-size", defaultValue = "10") Integer pageSize){
        List<GiftCertificateDto> giftCertificateDTOList =
                giftCertificateService.
                        getGiftCertificateBySeveralTags(
                                new HashSet<>(Arrays.asList(tags)),
                                page,
                                pageSize).
                        stream().map(giftCertificateMapper::toDto).
                        toList();
        return GiftCertificateHateoas.
                linksForGettingCertificatesBySeveralTags(
                        giftCertificateDTOList
                );
    }

    /**
     * Controller POST method that add a new gift certificate,
     * by calling a method of service layer.
     * @see GiftCertificateService#addNewGiftCertificate(GiftCertificate)
     * @param giftCertificateDTO object of Gift certificate that are going to be added in DB
     *
     * @return object that was added in DB
     * */
    @PostMapping
    public GiftCertificateDto addNewGiftCertificate(
            @RequestBody GiftCertificateDto giftCertificateDTO){
        GiftCertificate certificate =
                giftCertificateService.
                        addNewGiftCertificate(giftCertificateMapper.toGiftCertificate(giftCertificateDTO));
        return GiftCertificateHateoas.
                linksForAddingNewGiftCertificate(giftCertificateMapper.toDto(certificate));
    }

    /**
     * Controller PATCH method that updating a gift certificate by id,
     * by calling a method of service layer.
     * @see GiftCertificateService#updateGiftCertificate(Long, GiftCertificate)
     * @param giftCertificateDTO object of Gift certificate that are going to be updated in DB
     * @param id an id of object that are going to be updated
     *
     * @return object that was updated in DB
     * */
    @PatchMapping("{id}")
    public GiftCertificateDto updateGiftCertificate(
            @PathVariable("id") Long id,
            @RequestBody GiftCertificateDto giftCertificateDTO){
        GiftCertificate updatedGiftCertificate =
                giftCertificateService.
                        updateGiftCertificate(
                                id,
                                giftCertificateMapper.toGiftCertificate(giftCertificateDTO));
        return GiftCertificateHateoas.
                linksForUpdateGiftCertificate(giftCertificateMapper.
                                toDto(updatedGiftCertificate),
                        id);
    }

    /**
     * Controller PATCH method that updating a gift certificate by id and also
     * by some fields,
     * by calling a method of service layer.
     * @see GiftCertificateService#updateGiftCertificateBySomeFields(Long, String, String, BigDecimal, Integer)
     * @param id an id of object that are going to be updated
     * @param name new name value for gift certificate
     * @param description new description value for gift certificate
     * @param duration new duration value for gift certificate
     * @param price new price value for gift certificate
     *
     * @return object that was updated in DB
     * */
    @PatchMapping("update-field/{id}")
    public GiftCertificateDto updateGiftCertificateBySomeFields(@PathVariable Long id,
                                                                @Nullable @RequestParam("name") String name,
                                                                @Nullable @RequestParam("description") String description,
                                                                @Nullable @RequestParam("price") BigDecimal price,
                                                                @Nullable @RequestParam("duration") Integer duration){
        GiftCertificate giftCertificate = giftCertificateService.
                updateGiftCertificateBySomeFields(id,
                        name,
                        description,
                        price,
                        duration);
        return GiftCertificateHateoas.
                linksForUpdateGiftCertificate(
                        giftCertificateMapper.toDto(giftCertificate),
                        id);
    }

    /**
     * Controller DELETE method that delete a gift certificate by id,
     * by calling a method of service layer.
     * @see GiftCertificateService#deleteGiftCertificate(Long)
     * @param id an id of object that are going to be deleted
     * */
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteGiftCertificate(@PathVariable("id") Long id){
        giftCertificateService.deleteGiftCertificate(id);
        return ResponseEntity.of(Optional.of(Map.of("Status", String.format("Deleted gift certificate with id = %d",id))));
    }
}
