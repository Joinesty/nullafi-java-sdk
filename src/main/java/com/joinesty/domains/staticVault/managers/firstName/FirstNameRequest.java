package com.joinesty.domains.staticVault.managers.firstName;

import com.joinesty.services.BaseModel;

import java.util.List;

public class FirstNameRequest extends BaseModel {

  private String firstname;

  private String firstnameHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getFirstnameHash() {
    return firstnameHash;
  }

  public void setFirstnameHash(String firstnameHash) {
    this.firstnameHash = firstnameHash;
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
