package com.joinesty.domains.staticVault.managers.generic;

import com.joinesty.services.BaseModel;

import java.util.List;

public class GenericRequest extends BaseModel {

  private String data;

  private String dataHash;

  private String template;

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

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
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
