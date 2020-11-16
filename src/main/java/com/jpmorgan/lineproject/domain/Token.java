package com.jpmorgan.lineproject.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize(builder = Token.TokenBuilder.class)
public class Token {
    private String access_token;
    private String token_type;
    private long expires_in;
    private String scope;
    private String soap_instance_url;
    private String rest_instance_url;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class TokenBuilder {

    }
}