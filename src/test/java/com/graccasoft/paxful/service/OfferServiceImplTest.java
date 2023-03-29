package com.graccasoft.paxful.service;

import com.graccasoft.paxful.model.Offer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OfferServiceImplTest {

    @Autowired
    private OfferServiceImpl offerService;

    @Test
    void shouldUpdateMyOffer(){

        Assertions.assertDoesNotThrow(()-> offerService.calculateMyOfferNewRate() );
    }
}