package com.graccasoft.paxful.model;

import java.math.BigDecimal;

public record UpdateOfferRequest(
        BigDecimal margin,
        String offer_hash
) {
}
