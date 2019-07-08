package com.joinesty.domains.staticVault.managers.address;

import com.joinesty.services.BaseModel;

import java.util.List;

public class AddressRequest extends BaseModel {

  private String address;

  private String addressHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddressHash() {
    return addressHash;
  }

  public void setAddressHash(String addressHash) {
    this.addressHash = addressHash;
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
