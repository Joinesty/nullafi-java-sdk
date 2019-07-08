package com.joinesty.domains.staticVault.managers.placeOfBirth;

import com.joinesty.services.BaseModel;

import java.util.List;

public class PlaceOfBirthRequest extends BaseModel {

  private String placeofbirth;

  private String placeofbirthHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getPlaceofbirth() {
    return placeofbirth;
  }

  public void setPlaceofbirth(String placeofbirth) {
    this.placeofbirth = placeofbirth;
  }

  public String getPlaceofbirthHash() {
    return placeofbirthHash;
  }

  public void setPlaceofbirthHash(String placeofbirthHash) {
    this.placeofbirthHash = placeofbirthHash;
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
