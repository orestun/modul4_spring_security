package com.epam.esm.generator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author orest uzhytchak
 * A generator class that generate a new random GiftCertificate object
 * */
public class GiftCertificateGenerator implements Runnable {

    private GiftCertificate giftCertificate;

    private final static Random random = new Random();
    final static List<String> names = List.of(
            "cars shop",
            "restaurant",
            "McDonald`s",
            "adidas",
            "amazon",
            "toys shop",
            "english online course",
            "spanish online course",
            "books store",
            "jewelry",
            "disneyland");

    final static List<String> descriptions = List.of(
            "Gift certificate for buying a car",
            "Gift certificate to the restaurant",
            "Gift certificate to the McDonald`s",
            "Gift certificate sport equipment in adidas",
            "Gift certificate amazon",
            "Gift certificate in toys shop",
            "Gift certificate for english online course",
            "Gift certificate for spanish online course",
            "Gift certificate to the books store",
            "Gift certificate for jewelry",
            "Gift certificate to the disneyland");

    final static List<Integer> durations = List.of(15, 30, 45, 60, 75, 90, 180, 270, 360, 720);

    private static LocalDate generateRandomLocaleDate(){
        long startDate = LocalDate.of(2023,1,1).toEpochDay();
        long endDate = LocalDate.now().toEpochDay();
        long randomDay = ThreadLocalRandom
                .current()
                .nextLong(startDate, endDate);

        return LocalDate.ofEpochDay(randomDay);
    }
    private static Set<Tag> generateRandomTags(){
        Set<Tag> tags = new HashSet<>();
        for(int i = 0; i<=random.nextInt(0, 3); i++){
            long randomTagId = random.nextLong(1,1000);
            Tag tag = generateTag(randomTagId);
            tags.add(tag);

        }
        return tags;
    }

    @Override
    public void run() {
        GiftCertificate giftCertificate = new GiftCertificate();

        giftCertificate.setName(generateRandomName());
        giftCertificate.setDescription(getRandomDescription());
        giftCertificate.setDuration(generateRandomDuration());
        giftCertificate.setPrice(generateRandomPrice());
        giftCertificate.setCreateDate(GiftCertificateGenerator.generateRandomLocaleDate());
        giftCertificate.setUpdateDate(giftCertificate.getCreateDate());
        giftCertificate.setTags(GiftCertificateGenerator.generateRandomTags());
        this.giftCertificate = giftCertificate;
    }

    private static String generateRandomName(){
        return names.get(random.
                        nextInt(0, names.size()));
    }

    private static String getRandomDescription(){
        return descriptions.
                get(random.
                        nextInt(0,descriptions.size()));
    }

    private static Integer generateRandomDuration(){
        return durations.get(random.
                        nextInt(0,durations.size()));
    }

    private static BigDecimal generateRandomPrice(){
        return BigDecimal.valueOf(random.nextInt(10000));
    }

    private static Tag generateTag(Long id){
        return new Tag(id, String.format("tag%d", id));
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }
}
