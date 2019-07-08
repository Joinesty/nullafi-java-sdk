package com.joinesty.domains.staticVault.managers.ssn;

import com.joinesty.services.BaseModel;

import java.util.List;

public class SsnRequest extends BaseModel {

  private String ssn;

  private String ssnHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getSsn() {
    return ssn;
  }

  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

  public String getSsnHash() {
    return ssnHash;
  }

  public void setSsnHash(String ssnHash) {
    this.ssnHash = ssnHash;
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
