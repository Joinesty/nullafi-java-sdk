package com.joinesty.domains.staticVault.managers.ssn;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joinesty.services.BaseModel;

import java.util.Date;
import java.util.List;

/**
 * SsnResponse Class
 */

public class SsnResponse extends BaseModel {

  private String id;

  private String ssnAlias;

  private String ssn;

  private String iv;

  private String authTag;

  private List<String> tags;

  @JsonFormat
  private Date createdAt;

  @JsonFormat
  private Date updatedAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSsnAlias() {
    return ssnAlias;
  }

  public void setSsnAlias(String ssnAlias) {
    this.ssnAlias = ssnAlias;
  }

  public String getSsn() {
    return ssn;
  }

  public void setSsn(String ssn) {
    this.ssn = ssn;
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

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
}
