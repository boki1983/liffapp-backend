package com.jpmorgan.lineproject.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize(builder = LineToken.LineTokenBuilder.class)
public class LineToken {
    private String id_token;
    private String client_id;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class LineTokenBuilder {

    }
}
