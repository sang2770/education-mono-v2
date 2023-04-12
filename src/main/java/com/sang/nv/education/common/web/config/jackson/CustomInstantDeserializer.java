package com.sang.nv.education.common.web.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;

public class CustomInstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (jsonParser.getCurrentToken() != JsonToken.VALUE_NUMBER_INT) {
            throw deserializationContext.wrongTokenException(jsonParser,
                    Long.class,
                    JsonToken.VALUE_NUMBER_INT,
                    "Attempted to parse non-Int value to Long but this is forbidden");
        }
        return Instant.ofEpochMilli(jsonParser.getValueAsLong());
    }
}
