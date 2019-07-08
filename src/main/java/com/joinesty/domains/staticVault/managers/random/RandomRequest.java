package com.joinesty.domains.staticVault.managers.random;

import com.joinesty.services.BaseModel;

import java.util.List;

public class RandomRequest extends BaseModel {

  private String data;

  private String dataHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getDataHash() {
    return dataHash;
  }

  public void setDataHash(String dataHash) {
    this.dataHash = dataHash;
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
