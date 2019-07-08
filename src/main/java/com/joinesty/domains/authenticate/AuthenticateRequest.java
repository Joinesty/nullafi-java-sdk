package com.joinesty.domains.authenticate;

import com.joinesty.services.BaseModel;

public class AuthenticateRequest extends BaseModel {
  private String apiKey;

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }
}
