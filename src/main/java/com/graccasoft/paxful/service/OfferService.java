package com.graccasoft.paxful.service;

import com.graccasoft.paxful.model.Offer;
import com.graccasoft.paxful.model.UpdateOfferRequest;

import java.util.List;


/*
1. Get list of competition orders
2. Find the highest margin
3. Get user's order
4. Adjust users margin while checking the margin limit
5. Save users order
 */

public interface OfferService {
    List<Offer> fetchAllCompetitionOffers();
    Offer fetchMyOffer();

    UpdateOfferRequest calculateMyOfferNewRate();
    void updateOffer(UpdateOfferRequest updateOfferRequest);

}
