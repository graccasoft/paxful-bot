package com.graccasoft.paxful.repository;

import com.graccasoft.paxful.model.*;
import com.graccasoft.paxful.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Repository
@Slf4j
public class OfferRepository {

    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final OfferMapper offerMapper;
    private final String API_BASE_URL = "https://api.noones.com/noones/v1/";

    public OfferRepository(AuthService authService, RestTemplateBuilder templateBuilder, OfferMapper offerMapper) {
        this.authService = authService;
        this.restTemplate = templateBuilder.build();
        this.offerMapper = offerMapper;
    }

    public Offer getOffer(String hashId){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("offer_hash", Collections.singletonList(hashId));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth( authService.getJwt() );
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        GetOfferResponse getOffer = restTemplate.postForObject(API_BASE_URL + "offer/get",request, GetOfferResponse.class);

        log.info("Fetched offer: {}",getOffer.data());
        return getOffer.data();
    }

    public void updateOffer(UpdateOfferRequest updateOfferRequest) {
        log.info("Updating offer: {}", updateOfferRequest);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("offer_hash", Collections.singletonList(updateOfferRequest.offer_hash()));
        formData.put("margin", Collections.singletonList(updateOfferRequest.margin().toString()));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth( authService.getJwt() );
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        UpdateOfferResponse updateOfferResponse = restTemplate.postForObject(API_BASE_URL + "offer/update-price",request, UpdateOfferResponse.class);

        log.info("Updated offer: {}",updateOfferResponse);
    }

    public List<Offer> getOffers(){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("type", Collections.singletonList("buy"));
        formData.put("currency_code", Collections.singletonList("ZAR"));
        formData.put("payment_method", Collections.singletonList("bank-transfer"));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth( authService.getJwt() );
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        GetOffersResponse offers = restTemplate.postForObject(API_BASE_URL + "offer/all",request, GetOffersResponse.class);

        if(offers != null && offers.data() != null && offers.data().offers() != null){
            log.info("Fetched offers: {}",offers.data());

            return offers.data().offers()
                    .stream()
                    .map(offerMapper)
                    .toList();
        }
        return null;
    }
}
