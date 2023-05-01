package com.epam.esm.hateoas;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.GiftCertificateDto;
import org.springframework.hateoas.Link;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author orest uzhytchak
 * A hateoas class that add links to gift certificate object
 * */
public class GiftCertificateHateoas {

    static private Link updateGiftCertificateById(Long id, GiftCertificateDto giftCertificateDTO){
        return linkTo(methodOn(GiftCertificateController.class).updateGiftCertificate(
                id,
                giftCertificateDTO)).
                withRel("Update gift certificate by id").
                withType("PATCH");
    }

    static private Link deleteGiftCertificateById(Long id){
        return linkTo(methodOn(GiftCertificateController.class).
                deleteGiftCertificate(id)).
                withRel("Delete gift certificate by id").
                withType("DELETE");
    }

    static final private Link getAllGiftCertificatesLink =
            linkTo(methodOn(GiftCertificateController.class).
                    getAllGiftCertificates(1,10)).
                    withRel("Get all gift certificates").
                    withType("GET");

    static final private Link sortGiftCertificateByCreateDateLink = Link.of("http://localhost:8080/certificate/sort?create-date=asc").
            withRel("Sort gift certificates by create date (asc)").
            withType("GET");
    static final private Link sortGiftCertificateByNameLink = Link.of("http://localhost:8080/certificate/sort?name=asc").
            withRel("Sort gift certificates by name (asc)").
            withType("GET");
    static final private Link sortGiftCertificateByPriceLink = Link.of("http://localhost:8080/certificate/sort?price=asc").
            withRel("Sort gift certificates by price (asc)").
            withType("GET");
    static final private Link findGiftCertificateByNameLink = Link.of("http://localhost:8080/certificate/find-by-name?name=your-input-name").
            withRel("Find gift certificate by name").
            withType("GET");
    static final private Link findGiftCertificateByDescriptionLink = Link.of("http://localhost:8080/certificate/find-by-description?description=your-input-description").
            withRel("Find gift certificate by description").
            withType("GET");
    static final private Link findGiftCertificateBySeveralTagsLink = Link.of("http://localhost:8080/certificate/find-by-several-tags/your-tag-1, your-tag2, your-tag-3").
            withRel("Find gift certificate by several tags").
            withType("GET");

    /**
     * Hateoas method that add links to GiftCertificate objects got as
     * result of method {@link GiftCertificateController#getAllGiftCertificates(Integer, Integer)}
     *
     * @param giftCertificateDTOList list of gift certificates that was get from DB
     *
     * @return gift certificates list with links
     * */
    static public List<GiftCertificateDto> linksForGettingGiftCertificates(
            List<GiftCertificateDto> giftCertificateDTOList){
        for(GiftCertificateDto certificate: giftCertificateDTOList){
            Long id = certificate.getId();
            certificate.add(updateGiftCertificateById(id,certificate))
                    .add(deleteGiftCertificateById(id))
                    .add(sortGiftCertificateByCreateDateLink)
                    .add(sortGiftCertificateByNameLink)
                    .add(sortGiftCertificateByPriceLink)
                    .add(findGiftCertificateByNameLink)
                    .add(findGiftCertificateByDescriptionLink)
                    .add(findGiftCertificateBySeveralTagsLink)
                    .add(findGiftCertificateByNameLink);
        }
        return giftCertificateDTOList;
    }

    /**
     * Hateoas method that add links to GiftCertificate objects got as
     * result of method {@link GiftCertificateController#getSortedGiftCertificates(String, String, String, Integer, Integer)},
     * and {@link GiftCertificateController#getSortedGiftCertificatesByNameAndCreateDate(String, String, Integer, Integer)}
     *
     * @param giftCertificateDTOList list of gift certificates that was get from DB
     *
     * @return gift certificates list with links
     * */
     static public List<GiftCertificateDto> linksForSortingGiftCertificates(
             List<GiftCertificateDto> giftCertificateDTOList){
         for(GiftCertificateDto certificate: giftCertificateDTOList) {
             Long id = certificate.getId();
             certificate.add(updateGiftCertificateById(id,certificate))
                     .add(deleteGiftCertificateById(id))
                     .add(findGiftCertificateByNameLink)
                     .add(findGiftCertificateByDescriptionLink)
                     .add(findGiftCertificateBySeveralTagsLink)
                     .add(findGiftCertificateByNameLink);
         }
         return giftCertificateDTOList;
     }

    /**
     * Hateoas method that add links to GiftCertificate objects got as
     * result of method {@link GiftCertificateController#getGiftCertificatesByName(String, Integer, Integer)}
     * and {@link GiftCertificateController#getGiftCertificatesByDescription(String, Integer, Integer)}
     *
     * @param giftCertificateDTOList list of gift certificates that was get from DB
     *
     * @return gift certificates list with links
     * */
     static public List<GiftCertificateDto> linksForGettingGiftCertificatesByNameAndByDescription(
             List<GiftCertificateDto> giftCertificateDTOList){
        for (GiftCertificateDto certificate:giftCertificateDTOList){
            Long id = certificate.getId();
            certificate.add(updateGiftCertificateById(id,certificate))
                    .add(deleteGiftCertificateById(id))
                    .add(sortGiftCertificateByCreateDateLink)
                    .add(sortGiftCertificateByNameLink)
                    .add(sortGiftCertificateByPriceLink)
                    .add(findGiftCertificateBySeveralTagsLink)
                    .add(findGiftCertificateByNameLink);
        }
        return giftCertificateDTOList;
     }

    /**
     * Hateoas method that add links to GiftCertificate objects got as
     * result of method {@link GiftCertificateController#getGiftCertificateBySeveralTags(String[], Integer, Integer)}
     *
     * @param giftCertificateDTOList list of gift certificates that was get from DB
     *
     * @return gift certificates list with links
     * */
     static public List<GiftCertificateDto> linksForGettingCertificatesBySeveralTags(
             List<GiftCertificateDto> giftCertificateDTOList
     ){
         for(GiftCertificateDto certificate:giftCertificateDTOList){
             Long id = certificate.getId();
             certificate.add(updateGiftCertificateById(id,certificate))
                     .add(deleteGiftCertificateById(id))
                     .add(sortGiftCertificateByCreateDateLink)
                     .add(sortGiftCertificateByNameLink)
                     .add(sortGiftCertificateByPriceLink)
                     .add(findGiftCertificateByNameLink)
                     .add(findGiftCertificateByDescriptionLink)
                     .add(findGiftCertificateByNameLink);
         }
         return giftCertificateDTOList;
     }

    /**
     * Hateoas method that add links to GiftCertificate objects got as
     * result of method {@link GiftCertificateController#updateGiftCertificate(Long, GiftCertificateDto)}
     * and {@link GiftCertificateController#updateGiftCertificateBySomeFields(Long, String, String, BigDecimal, Integer)}
     *
     * @param certificate gift certificate that was updated
     *
     * @return gift certificate with links
     * */
     static public GiftCertificateDto linksForUpdateGiftCertificate(
             GiftCertificateDto certificate,
             Long id){
         certificate.add(deleteGiftCertificateById(id))
                 .add(getAllGiftCertificatesLink);
         return certificate;
     }

    /**
     * Hateoas method that add links to GiftCertificate object got as
     * result of method {@link GiftCertificateController#addNewGiftCertificate(GiftCertificateDto)}
     *
     * @param certificate gift certificate that was added in DB
     *
     * @return gift certificate with links
     * */
    static public GiftCertificateDto linksForAddingNewGiftCertificate(
            GiftCertificateDto certificate){
        Long id = certificate.getId();
        certificate.add(updateGiftCertificateById(id, certificate))
                .add(deleteGiftCertificateById(id))
                .add(getAllGiftCertificatesLink);
        return certificate;
    }
}
