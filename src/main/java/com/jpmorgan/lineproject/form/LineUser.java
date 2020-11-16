package com.jpmorgan.lineproject.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize(builder = LineUser.LineUserBuilder.class)
public class LineUser {
    private String uuid;
    private String lineUserId;
    private String userToken;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class LineUserBuilder {

    }
}
