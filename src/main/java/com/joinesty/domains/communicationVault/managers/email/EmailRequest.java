package com.joinesty.domains.communicationVault.managers.email;

import com.joinesty.services.BaseModel;

import java.util.List;

public class EmailRequest extends BaseModel {

  private String email;

  private String emailHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmailHash() {
    return emailHash;
  }

  public void setEmailHash(String emailHash) {
    this.emailHash = emailHash;
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
