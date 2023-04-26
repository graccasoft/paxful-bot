package com.graccasoft.paxful.model;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class OfferMapper implements Function<ListOffer, Offer> {
    @Override
    public Offer apply(ListOffer listOffer) {
        return new Offer(listOffer.offer_id(),
                listOffer.crypto_currency_code(),
                listOffer.fiat_currency_code(),
                listOffer.offer_type(),
                listOffer.margin(),
                listOffer.fiat_price_per_btc()
                );
    }
}
