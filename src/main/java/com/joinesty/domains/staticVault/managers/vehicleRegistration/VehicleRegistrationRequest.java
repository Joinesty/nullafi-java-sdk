package com.joinesty.domains.staticVault.managers.vehicleRegistration;

import com.joinesty.services.BaseModel;

import java.util.List;

public class VehicleRegistrationRequest extends BaseModel {

  private String vehicleregistration;

  private String vehicleregistrationHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getVehicleregistration() {
    return vehicleregistration;
  }

  public void setVehicleregistration(String vehicleregistration) {
    this.vehicleregistration = vehicleregistration;
  }

  public String getVehicleregistrationHash() {
    return vehicleregistrationHash;
  }

  public void setVehicleregistrationHash(String vehicleregistrationHash) {
    this.vehicleregistrationHash = vehicleregistrationHash;
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
