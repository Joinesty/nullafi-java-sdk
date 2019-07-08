package com.joinesty.domains.staticVault.managers.driversLicense;

import com.joinesty.services.BaseModel;

import java.util.List;

public class DriversLicenseRequest extends BaseModel {

  private String driverslicense;

  private String driverslicenseHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getDriverslicense() {
    return driverslicense;
  }

  public void setDriverslicense(String driverslicense) {
    this.driverslicense = driverslicense;
  }

  public String getDriverslicenseHash() {
    return driverslicenseHash;
  }

  public void setDriverslicenseHash(String driverslicenseHash) {
    this.driverslicenseHash = driverslicenseHash;
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
