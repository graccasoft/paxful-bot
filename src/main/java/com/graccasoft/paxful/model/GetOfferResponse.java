package com.graccasoft.paxful.model;

public record GetOfferResponse(
        Offer data,
        String status
) {
}
