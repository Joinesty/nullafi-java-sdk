package com.joinesty.domains.staticVault.managers.placeOfBirth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joinesty.services.BaseModel;

import java.util.Date;
import java.util.List;

public class PlaceOfBirthResponse extends BaseModel {

  private String id;

  private String placeofbirthAlias;

  private String placeofbirth;

  private String iv;

  private String authTag;

  private List<String> tags;

  @JsonFormat
  private Date createdAt;

  @JsonFormat
  private Date updatedAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPlaceofbirthAlias() {
    return placeofbirthAlias;
  }

  public void setPlaceofbirthAlias(String placeofbirthAlias) {
    this.placeofbirthAlias = placeofbirthAlias;
  }

  public String getPlaceofbirth() {
    return placeofbirth;
  }

  public void setPlaceofbirth(String placeofbirth) {
    this.placeofbirth = placeofbirth;
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

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
}
