package com.joinesty.domains.staticVault.managers.driversLicense;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joinesty.services.BaseModel;

import java.util.Date;
import java.util.List;

public class DriversLicenseResponse extends BaseModel {

  private String id;

  private String driverslicenseAlias;

  private String driverslicense;

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

  public String getDriverslicenseAlias() {
    return driverslicenseAlias;
  }

  public void setDriverslicenseAlias(String driverslicenseAlias) {
    this.driverslicenseAlias = driverslicenseAlias;
  }

  public String getDriverslicense() {
    return driverslicense;
  }

  public void setDriverslicense(String driverslicense) {
    this.driverslicense = driverslicense;
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
