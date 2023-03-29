package com.graccasoft.paxful.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceImplTest {

    @Autowired
    private AuthServiceImpl authService;

    @Test
    void shouldGetToken(){

        Assertions.assertNotNull( authService.getJwt() );

    }
}