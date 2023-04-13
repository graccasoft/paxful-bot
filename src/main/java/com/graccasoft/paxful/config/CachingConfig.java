package com.graccasoft.paxful.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
@Slf4j
public class CachingConfig {

    @Bean
    public CacheManager cacheManager(){
        return new ConcurrentMapCacheManager("jwt");
    }

    /**
        Clear cached token and get fresh token every 12 hours
     */
    @CacheEvict(value = "jwt", allEntries = true)
    @Scheduled(fixedRate = 3200000)
    public void clearCache(){
        log.info("Emptying cache");
    }

}
