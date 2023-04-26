package com.graccasoft.paxful.model;

import java.util.List;

public record GetOffersResponseData(
        Integer count,
        Integer limit,
        List<ListOffer> offers,
        Integer offset,
        Integer totalCount
) {
}
