package com.graccasoft.paxful.repository;

import com.graccasoft.paxful.model.Offer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OfferRepositoryTest {

    @Autowired
    private OfferRepository underTest;

    @Test
    void getOffers() {
        List<Offer> offers = underTest.getOffers();
        Assertions.assertNotNull(offers);
    }
}