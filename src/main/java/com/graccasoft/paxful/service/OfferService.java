package com.graccasoft.paxful.service;

import com.graccasoft.paxful.model.Offer;
import com.graccasoft.paxful.model.UpdateOfferRequest;

import java.util.List;


public interface OfferService {
    List<Offer> fetchAllCompetitionOffersInOrder();
    Offer fetchMyOffer();

    UpdateOfferRequest calculateMyOfferNewRate();

    void updateOffer(UpdateOfferRequest offerRequest);
}
