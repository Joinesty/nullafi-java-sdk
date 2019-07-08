package com.joinesty.domains.staticVault.managers.taxpayer;

import com.joinesty.services.BaseModel;

import java.util.List;

public class TaxpayerRequest extends BaseModel {

  private String taxpayer;

  private String taxpayerHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getTaxpayer() {
    return taxpayer;
  }

  public void setTaxpayer(String taxpayer) {
    this.taxpayer = taxpayer;
  }

  public String getTaxpayerHash() {
    return taxpayerHash;
  }

  public void setTaxpayerHash(String taxpayerHash) {
    this.taxpayerHash = taxpayerHash;
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
