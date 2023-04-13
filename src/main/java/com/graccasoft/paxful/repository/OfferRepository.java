package com.graccasoft.paxful.repository;

import com.graccasoft.paxful.model.GetOfferResponse;
import com.graccasoft.paxful.model.Offer;
import com.graccasoft.paxful.model.UpdateOfferRequest;
import com.graccasoft.paxful.model.UpdateOfferResponse;
import com.graccasoft.paxful.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Repository
@Slf4j
public class OfferRepository {

    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "https://api.noones.com/noones/v1/";

    public OfferRepository(AuthService authService, RestTemplate restTemplate) {
        this.authService = authService;
        this.restTemplate = restTemplate;
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
}
