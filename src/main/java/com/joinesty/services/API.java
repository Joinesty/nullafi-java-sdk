package com.joinesty.services;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;

public class API {

  private String API_HOST = "https://enterprise-api.nullafi.com";

  public API() {

    String env = System.getenv("API_URL");
    if (env != null) this.API_HOST = env;

    Unirest.setObjectMapper(new ObjectMapper() {

      private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind
        .ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL);

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

  public String setUrl(String url) {
    return this.API_HOST + url;
  }

}
