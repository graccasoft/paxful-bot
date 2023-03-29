package com.graccasoft.paxful.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaxfulLoginResponse(

        @JsonProperty("token_type") String tokenType,
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_in") Integer expiresIn
) {
}
