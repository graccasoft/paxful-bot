package com.graccasoft.paxful.service;

import com.graccasoft.paxful.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class OfferServiceImpl implements OfferService {


    private final BotOptionService botOptionService;
    private final RestTemplate restTemplate;
    private final String jwt;
    private final String API_BASE_URL = "https://api.noones.com/noones/v1/";

    public OfferServiceImpl(BotOptionService botOptionService, RestTemplateBuilder templateBuilder, AuthService authService) {
        this.botOptionService = botOptionService;
        this.restTemplate = templateBuilder.build();

        this.jwt = authService.getJwt();
    }

    @Override
    public List<Offer> fetchAllCompetitionOffers() {

        BotOption competition = botOptionService.getOption("competition");
        List<String> competitionIds = Arrays.stream(competition.getValue().split(",")).toList();
        List<Offer> offers = new ArrayList<>();
        competitionIds.forEach(hashId->{
            offers.add(getOffer(hashId));
        });
        return offers;
    }

    @Override
    public Offer fetchMyOffer() {
        BotOption myAdd = botOptionService.getOption("my_add");
        return getOffer(myAdd.getValue());
    }

    @Override
    public UpdateOfferRequest calculateMyOfferNewRate() {
        BotOption myAdd = botOptionService.getOption("my_add");
        //find the offer with the highest margin
        List<Offer> sorted = fetchAllCompetitionOffers();

        log.info("Sorted offers: {}", sorted);
        if( sorted.size() > 0 ){
            Offer myOffer = getOffer(myAdd.getValue());
            log.info("My offer: {}", myOffer);
            BigDecimal highestMargin = sorted.get(sorted.size()-1 ).margin();
            if( myOffer.margin().compareTo( highestMargin ) > 0 ){
                return null;
            }

            BigDecimal newMargin = highestMargin.subtract ( highestMargin.multiply(BigDecimal.valueOf(0.1f)) );
            log.info("New margin: {}", newMargin);
            return new UpdateOfferRequest(newMargin, myOffer.id());
        }

        return null;
    }

    @Override
    public void updateOffer(UpdateOfferRequest updateOfferRequest) {
        log.info("Updating offer: {}", updateOfferRequest);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("offer_hash", Collections.singletonList(updateOfferRequest.offer_hash()));
        formData.put("margin", Collections.singletonList(updateOfferRequest.margin().toString()));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth( jwt );
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        UpdateOfferResponse updateOfferResponse = restTemplate.postForObject(API_BASE_URL + "offer/update-price",request, UpdateOfferResponse.class);

        log.info("Updated offer: {}",updateOfferResponse);
    }

    private Offer getOffer(String hashId){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("offer_hash", Collections.singletonList(hashId));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth( jwt );
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        GetOfferResponse getOffer = restTemplate.postForObject(API_BASE_URL + "offer/get",request, GetOfferResponse.class);

        log.info("Fetched offer: {}",getOffer.data());
        return getOffer.data();
    }
}
