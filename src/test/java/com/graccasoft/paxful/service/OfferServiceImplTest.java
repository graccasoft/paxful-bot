package com.graccasoft.paxful.service;

import com.graccasoft.paxful.model.BotOption;
import com.graccasoft.paxful.model.Offer;
import com.graccasoft.paxful.model.UpdateOfferRequest;
import com.graccasoft.paxful.repository.OfferRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

/**
 * Offers to test with
 * Offer[id=sBqtB7oK5bv, crypto_currency=BTC, fiat_currency_code=ZAR, offer_type=buy, margin=0.7],
 * Offer[id=Bn7SzybDrwC, crypto_currency=BTC, fiat_currency_code=ZAR, offer_type=buy, margin=1.52],
 * Offer[id=AS9he7Rxwwf, crypto_currency=BTC, fiat_currency_code=ZAR, offer_type=buy, margin=1.4],
 * Offer[id=fRcgPsn8RzF, crypto_currency=BTC, fiat_currency_code=ZAR, offer_type=buy, margin=1.43]]
 */
class OfferServiceImplTest {

    @Mock private OfferRepository offerRepository;
    @Mock private BotOptionService botOptionService;

    private OfferServiceImpl underTest;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new OfferServiceImpl(
                botOptionService,
                offerRepository);
    }

    @Test
    void canFetchAllCompetitionOffersInOrder() {
        //Given
        given(botOptionService.getOption("competition"))
                .willReturn(new BotOption("competition","sBqtB7oK5bv,Bn7SzybDrwC,AS9he7Rxwwf,fRcgPsn8RzF"));

        given(offerRepository.getOffer("sBqtB7oK5bv"))
                .willReturn(new Offer(
                        "sBqtB7oK5bv",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(0.7)
                ));
        given(offerRepository.getOffer("Bn7SzybDrwC"))
                .willReturn(new Offer(
                        "Bn7SzybDrwC",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(1.52)
                ));
        given(offerRepository.getOffer("AS9he7Rxwwf"))
                .willReturn(new Offer(
                        "AS9he7Rxwwf",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(1.4)
                ));
        given(offerRepository.getOffer("fRcgPsn8RzF"))
                .willReturn(new Offer(
                        "fRcgPsn8RzF",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(1.43)
                ));
        //When
        List<Offer> sortedOffers = underTest.fetchAllCompetitionOffersInOrder();

        //Then
        Assertions.assertEquals(BigDecimal.valueOf(1.52), sortedOffers.get(sortedOffers.size()-1 ).margin());
    }

    @Test
    void canFetchMyOffer() {
        //Given
        given(botOptionService.getOption("my_add"))
                .willReturn(new BotOption("my_add","VGer81ZPnjD"));

        given(offerRepository.getOffer("VGer81ZPnjD"))
                .willReturn(new Offer(
                        "VGer81ZPnjD",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.ZERO
                ));

        //When
        Offer myOffer = underTest.fetchMyOffer();

        //Then
        Assertions.assertEquals(myOffer.id(), "VGer81ZPnjD");
    }

    @Test
    void canCalculateMyOfferNewRate() {
        //Given
        //my add
        given(botOptionService.getOption("my_add"))
                .willReturn(new BotOption("my_add","VGer81ZPnjD"));

        given(offerRepository.getOffer("VGer81ZPnjD"))
                .willReturn(new Offer(
                        "VGer81ZPnjD",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.ZERO
                ));

        //increase rate option
        given(botOptionService.getOption("increase_rate"))
                .willReturn(new BotOption("increase_rate","0.1"));

        //competition adds/offers
        given(botOptionService.getOption("competition"))
                .willReturn(new BotOption("competition","sBqtB7oK5bv,Bn7SzybDrwC,AS9he7Rxwwf,fRcgPsn8RzF"));



        given(offerRepository.getOffer("sBqtB7oK5bv"))
                .willReturn(new Offer(
                        "sBqtB7oK5bv",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(0.7)
                ));
        given(offerRepository.getOffer("Bn7SzybDrwC"))
                .willReturn(new Offer(
                        "Bn7SzybDrwC",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(1.52)
                ));
        given(offerRepository.getOffer("AS9he7Rxwwf"))
                .willReturn(new Offer(
                        "AS9he7Rxwwf",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(1.4)
                ));
        given(offerRepository.getOffer("fRcgPsn8RzF"))
                .willReturn(new Offer(
                        "fRcgPsn8RzF",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(1.43)
                ));
        //When
        UpdateOfferRequest offerRequest = underTest.calculateMyOfferNewRate();

        /* 1.52 * (1+ ( 0.1/100 ) )
            should return 1.52152
         */

        //Then
        Assertions.assertEquals(0, offerRequest.margin().compareTo(BigDecimal.valueOf(1.52152)) );
    }

    @Test
    void canReduceMarginIfVeryHigh() {
        //Given
        //my add
        given(botOptionService.getOption("my_add"))
                .willReturn(new BotOption("my_add","VGer81ZPnjD"));

        given(offerRepository.getOffer("VGer81ZPnjD"))
                .willReturn(new Offer(
                        "VGer81ZPnjD",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(10)
                ));

        //increase rate option
        given(botOptionService.getOption("increase_rate"))
                .willReturn(new BotOption("increase_rate","0.1"));

        //competition adds/offers
        given(botOptionService.getOption("competition"))
                .willReturn(new BotOption("competition","sBqtB7oK5bv,Bn7SzybDrwC"));



        given(offerRepository.getOffer("sBqtB7oK5bv"))
                .willReturn(new Offer(
                        "sBqtB7oK5bv",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(7)
                ));
        given(offerRepository.getOffer("Bn7SzybDrwC"))
                .willReturn(new Offer(
                        "Bn7SzybDrwC",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(9)
                ));

        //When
        UpdateOfferRequest offerRequest = underTest.calculateMyOfferNewRate();

        /* 9 * (1+ ( 0.1/100 ) )
            should return 9.009
         */

        //Then
        Assertions.assertEquals(0, offerRequest.margin().compareTo(BigDecimal.valueOf(9.009)) );
    }

    @Test
    void canIgnoreUpdateMarginIfNotVeryHigh() {
        //Given
        //my add
        given(botOptionService.getOption("my_add"))
                .willReturn(new BotOption("my_add","VGer81ZPnjD"));

        given(offerRepository.getOffer("VGer81ZPnjD"))
                .willReturn(new Offer(
                        "VGer81ZPnjD",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(10)
                ));

        //increase rate option
        given(botOptionService.getOption("increase_rate"))
                .willReturn(new BotOption("increase_rate","0.1"));

        //competition adds/offers
        given(botOptionService.getOption("competition"))
                .willReturn(new BotOption("competition","sBqtB7oK5bv,Bn7SzybDrwC"));



        given(offerRepository.getOffer("sBqtB7oK5bv"))
                .willReturn(new Offer(
                        "sBqtB7oK5bv",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(7)
                ));
        given(offerRepository.getOffer("Bn7SzybDrwC"))
                .willReturn(new Offer(
                        "Bn7SzybDrwC",
                        "BTC",
                        "ZAR",
                        "buy",
                        BigDecimal.valueOf(9.99)
                ));

        //When
        UpdateOfferRequest offerRequest = underTest.calculateMyOfferNewRate();


        //Then
        Assertions.assertNull(offerRequest);
    }
}