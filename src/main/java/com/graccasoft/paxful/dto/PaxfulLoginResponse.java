package com.graccasoft.paxful.dto;

public record PaxfulLoginResponse(
        String token_type,
        String access_token,
        Integer expires_in
) {
}
