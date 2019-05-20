package com.joinesty.services;



import lombok.Getter;
import lombok.Setter;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.ObjectMapper;

import java.util.Arrays;

public class API {

    private final String API_HOST = "https://enterprise-api.nullafi.com";

    public API() {
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Getter @Setter
    private String sessionToken;

    public String setUrl(String url) {
        return this.API_HOST + url;
    }

}
