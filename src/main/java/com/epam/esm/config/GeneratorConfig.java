package com.epam.esm.config;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.generator.GiftCertificateGenerator;
import com.epam.esm.generator.UserGenerator;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class GeneratorConfig {

    private static final int NUMBER_OF_CERTIFICATES = 500;
    private static final int NUMBER_OF_USERS = 100;
    private static final int NUMBER_OF_ORDERS = 1000;
    private static final int NUMBER_OF_TAGS = 1000;
    @Autowired
    GiftCertificateRepository giftCertificateRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    OrderService orderService;

    @Bean
    public CommandLineRunner generateData() {
        return args -> {
            if(!isGeneratorDisabled()){
                generateTags();
                generateAndSaveGiftCertificates();
                generateAndSaveUsers();
                generateOrders();
            }
        };
    }

    private void generateAndSaveGiftCertificates() throws InterruptedException {
        for (int i = 1; i <= GeneratorConfig.NUMBER_OF_CERTIFICATES; i++) {
            giftCertificateRepository.
                    save(getRandomGiftCertificate());
        }
    }

    private GiftCertificate getRandomGiftCertificate() throws InterruptedException {
        GiftCertificateGenerator generator = new GiftCertificateGenerator();
        Thread thread = new Thread(generator);
        thread.start();
        thread.join();
        return generator.getGiftCertificate();
    }

    private void generateAndSaveUsers(){
        for (int i = 1; i <= GeneratorConfig.NUMBER_OF_USERS; i++) {
            userRepository.save(getRandomGeneratedUser(i));
        }
    }

    private User getRandomGeneratedUser(int numberOfUser){
        return UserGenerator.generateRandomUser(numberOfUser);
    }

    private void generateTags(){
        List<Tag> tags = new ArrayList<>();
        for (int i = 1; i <= NUMBER_OF_TAGS; i++) {
            Tag tag = new Tag(String.format("tag%d", i));
            tags.add(tag);
        }
        tagRepository.saveAll(tags);
    }

    private void generateOrders(){
        for (int i = 1; i <= NUMBER_OF_ORDERS; i++) {
            Random random = new Random();
            orderService.addNewOrder(
                    random.nextLong(1, NUMBER_OF_USERS),
                    random.nextLong(1, NUMBER_OF_CERTIFICATES)
            );
        }
    }

    private boolean isGeneratorDisabled(){
        return true;
    }
}
