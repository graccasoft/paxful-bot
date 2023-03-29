package com.graccasoft.paxful.model;

import java.util.List;

public record UserOffers(
        boolean success,
        List<Offer> data
) {
}
