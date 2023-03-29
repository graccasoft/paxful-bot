package com.graccasoft.paxful.service;

import com.graccasoft.paxful.dto.PaxfulLoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${paxful.key}")
    private String paxfulKey;

    @Value("${paxful.secret}")
    private String paxfulSecret;

    private final RestTemplate restTemplate;

    public AuthServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public String getJwt() {
        //todo fetch in local storage first
        PaxfulLoginResponse loginResponse = getJwtFromPaxful();
        System.out.println(loginResponse);
        return loginResponse.access_token();
    }

    private PaxfulLoginResponse getJwtFromPaxful(){

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("client_credentials"));
        formData.put("client_id", Collections.singletonList(paxfulKey));
        formData.put("client_secret", Collections.singletonList(paxfulSecret));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        return restTemplate.postForObject("https://accounts.paxful.com/oauth2/token",request, PaxfulLoginResponse.class);
    }


}
