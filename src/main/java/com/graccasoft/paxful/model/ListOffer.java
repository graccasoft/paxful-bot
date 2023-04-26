package com.graccasoft.paxful.model;

import java.math.BigDecimal;

public record ListOffer(
        String offer_id,
        String crypto_currency_code,
        String fiat_currency_code,
        String offer_type,
        BigDecimal margin,
        BigDecimal fiat_price_per_btc
) {
}
