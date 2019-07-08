package com.joinesty.domains.staticVault.managers.dateOfBirth;

import com.joinesty.services.BaseModel;

import java.util.List;

public class DateOfBirthRequest extends BaseModel {

  private String dateofbirth;

  private String dateofbirthHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getDateofbirth() {
    return dateofbirth;
  }

  public void setDateofbirth(String dateofbirth) {
    this.dateofbirth = dateofbirth;
  }

  public String getDateofbirthHash() {
    return dateofbirthHash;
  }

  public void setDateofbirthHash(String dateofbirthHash) {
    this.dateofbirthHash = dateofbirthHash;
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
