package com.joinesty.domains.authenticate;

import com.joinesty.services.BaseModel;

public class AuthenticateResponse extends BaseModel {
  private String token;
  private String hashKey;
  private String tenantId;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getHashKey() {
    return hashKey;
  }

  public void setHashKey(String hashKey) {
    this.hashKey = hashKey;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }
}
