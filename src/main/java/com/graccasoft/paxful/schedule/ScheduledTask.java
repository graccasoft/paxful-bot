package com.graccasoft.paxful.schedule;

import com.graccasoft.paxful.model.BotOption;
import com.graccasoft.paxful.model.UpdateOfferRequest;
import com.graccasoft.paxful.service.AuthService;
import com.graccasoft.paxful.service.BotOptionService;
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
    private final BotOptionService botOptionService;

    public ScheduledTask(OfferService offerService, BotOptionService botOptionService, AuthService authService) {
        this.offerService = offerService;
        this.botOptionService = botOptionService;
    }

    @Scheduled(fixedRate = 10000)
    public void updateOffersTask(){
        log.info("In the task now");
        try{
            BotOption botStatus = botOptionService.getOption("bot_running");
            if(botStatus.getValue().equals("1")){
                UpdateOfferRequest offerRequest = offerService.calculateMyOfferNewRate();
                if( offerRequest != null ){
                    offerService.updateOffer(offerRequest);
                    //update off
                }
            }else{
                log.info("Bot is off");
            }
        }catch(Exception ex){
            log.info(ex.getMessage());
        }

    }
}
