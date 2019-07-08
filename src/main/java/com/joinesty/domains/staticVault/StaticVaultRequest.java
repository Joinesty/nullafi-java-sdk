package com.joinesty.domains.staticVault;

import com.joinesty.services.BaseModel;
import java.util.List;

public class StaticVaultRequest extends BaseModel {

  private String name;

  private List<String> tags;

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
}
