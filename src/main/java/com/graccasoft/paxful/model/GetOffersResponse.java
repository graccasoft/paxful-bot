package com.graccasoft.paxful.model;

import java.util.List;

public record GetOffersResponse(
        GetOffersResponseData data,
        String status
) {}
