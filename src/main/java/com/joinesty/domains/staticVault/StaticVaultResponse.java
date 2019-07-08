package com.joinesty.domains.staticVault;

import com.joinesty.services.BaseModel;

import java.util.Date;
import java.util.List;

public class StaticVaultResponse extends BaseModel {

  private String id;

  private String name;

  private List<String> tags;

  private Date createdAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
}
