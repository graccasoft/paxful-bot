package com.graccasoft.paxful.schedule;

import com.graccasoft.paxful.model.UpdateOfferRequest;
import com.graccasoft.paxful.service.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class ScheduledTask {

    private final OfferService offerService;

    public ScheduledTask(OfferService offerService) {
        this.offerService = offerService;
    }

    @Scheduled(fixedRate = 60000)
    public void updateOffersTask(){
        log.info("In the task now");

        UpdateOfferRequest offerRequest = offerService.calculateMyOfferNewRate();
        if( offerRequest != null ){
            //update off
        }
    }
}
