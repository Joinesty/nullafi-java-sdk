package com.joinesty.domains.staticVault.managers.lastName;

import com.joinesty.services.BaseModel;

import java.util.List;

public class LastNameRequest extends BaseModel {

  private String lastname;

  private String lastnameHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getLastnameHash() {
    return lastnameHash;
  }

  public void setLastnameHash(String lastnameHash) {
    this.lastnameHash = lastnameHash;
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
