package com.graccasoft.paxful.service;

import com.graccasoft.paxful.model.*;
import com.graccasoft.paxful.repository.OfferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OfferServiceImpl implements OfferService {


    public static final double MAXIMUM_PERCENTAGE_GAP = 0.01;
    private final BotOptionService botOptionService;
    private final OfferRepository offerRepository;


    public OfferServiceImpl(BotOptionService botOptionService, OfferRepository offerRepository) {
        this.botOptionService = botOptionService;
        this.offerRepository = offerRepository;
    }

    @Override
    public List<Offer> fetchAllCompetitionOffersInOrder() {

        BotOption competition = botOptionService.getOption("competition");
        List<String> competitionIds = Arrays.stream(competition.getValue().split(",")).toList();
        List<Offer> offers = new ArrayList<>();
        competitionIds.forEach(hashId->{
            offers.add(  offerRepository.getOffer(hashId));
        });
        
        return offers
                .stream()
                .sorted(Comparator.comparing(Offer::margin) )
                .collect(Collectors.toList());
    }

    @Override
    public Offer fetchMyOffer() {
        BotOption myAdd = botOptionService.getOption("my_add");
        return offerRepository.getOffer(myAdd.getValue());
    }

    @Override
    public void updateOffer(UpdateOfferRequest offerRequest) {
        offerRepository.updateOffer(offerRequest);
    }

    @Override
    public UpdateOfferRequest calculateMyOfferNewRate() {
        BotOption myAdd = botOptionService.getOption("my_add");
        //find the offer with the highest margin
        List<Offer> sorted = fetchAllCompetitionOffersInOrder();

        log.info("Sorted offers: {}", sorted);
        if( sorted.size() > 0 ){
            Offer myOffer = offerRepository.getOffer(myAdd.getValue());
            log.info("My offer: {}", myOffer);
            BigDecimal highestMargin = sorted.get(sorted.size()-1 ).margin();

            //we are at the lead
            if( myOffer.margin().compareTo( highestMargin ) > 0 ){
                //let's check if we are far off and come down a bit
                //if we are not very high we just return null
                if( myOffer.margin().subtract( highestMargin )
                        .divide(highestMargin, new MathContext(2))
                        .compareTo(BigDecimal.valueOf(MAXIMUM_PERCENTAGE_GAP)) < 0 ){

                    return null;
                }
            }
            BigDecimal increaseRate =
                    new BigDecimal( botOptionService.getOption("increase_rate").getValue() )
                            .divide(BigDecimal.valueOf(100), new MathContext(2))
                            .add(BigDecimal.ONE);
            BigDecimal newMargin = highestMargin.multiply (increaseRate ).round(new MathContext(3, RoundingMode.UP)) ;
            log.info("New margin: {}", newMargin);
            return new UpdateOfferRequest(newMargin, myOffer.id());
        }

        return null;
    }


}
