package com.joinesty.domains.staticVault.managers.gender;

import com.joinesty.services.BaseModel;

import java.util.List;

public class GenderRequest extends BaseModel {

  private String gender;

  private String genderHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getGenderHash() {
    return genderHash;
  }

  public void setGenderHash(String genderHash) {
    this.genderHash = genderHash;
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
