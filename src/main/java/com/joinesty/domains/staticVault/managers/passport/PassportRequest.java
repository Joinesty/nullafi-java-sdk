package com.joinesty.domains.staticVault.managers.passport;

import com.joinesty.services.BaseModel;

import java.util.List;

public class PassportRequest extends BaseModel {

  private String passport;

  private String passportHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getPassport() {
    return passport;
  }

  public void setPassport(String passport) {
    this.passport = passport;
  }

  public String getPassportHash() {
    return passportHash;
  }

  public void setPassportHash(String passportHash) {
    this.passportHash = passportHash;
  }

  public String getIv() {
    return iv;
  }

  public void setIv(String iv) {
    this.iv = iv;
  }

  public String getAuthTag() {
    return authTag;
  }

  public void setAuthTag(String authTag) {
    this.authTag = authTag;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }
}
