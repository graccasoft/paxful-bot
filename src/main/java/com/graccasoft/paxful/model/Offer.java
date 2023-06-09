package com.graccasoft.paxful.model;

import java.math.BigDecimal;

public record Offer(
        String id,
        String crypto_currency,
        String fiat_currency_code,
        String offer_type,
        BigDecimal margin,
        BigDecimal fiat_price_per_btc
) {
}
