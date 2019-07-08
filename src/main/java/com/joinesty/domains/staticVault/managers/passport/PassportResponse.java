package com.joinesty.domains.staticVault.managers.passport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joinesty.services.BaseModel;

import java.util.Date;
import java.util.List;

/**
 * PassportResponse Class
 */

public class PassportResponse extends BaseModel {

  private String id;

  private String passportAlias;

  private String passport;

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

  public String getPassportAlias() {
    return passportAlias;
  }

  public void setPassportAlias(String passportAlias) {
    this.passportAlias = passportAlias;
  }

  public String getPassport() {
    return passport;
  }

  public void setPassport(String passport) {
    this.passport = passport;
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
