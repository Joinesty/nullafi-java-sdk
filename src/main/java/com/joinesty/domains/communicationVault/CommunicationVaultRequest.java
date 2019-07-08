package com.joinesty.domains.communicationVault;

import com.joinesty.services.BaseModel;

import java.util.List;

public class CommunicationVaultRequest extends BaseModel {

  private String name;

  private String publicKey;

  private List<String> tags;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }
}
