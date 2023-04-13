package com.graccasoft.paxful.service;

import com.graccasoft.paxful.model.*;
import com.graccasoft.paxful.repository.OfferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OfferServiceImpl implements OfferService {


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
            if( myOffer.margin().compareTo( highestMargin ) > 0 ){
                return null;
            }
            BigDecimal increaseRate =
                    new BigDecimal( botOptionService.getOption("increase_rate").getValue() )
                            .divide(BigDecimal.valueOf(100), new MathContext(8))
                            .add(BigDecimal.ONE);
            BigDecimal newMargin = highestMargin.multiply (increaseRate );
            log.info("New margin: {}", newMargin);
            return new UpdateOfferRequest(newMargin, myOffer.id());
        }

        return null;
    }


}
