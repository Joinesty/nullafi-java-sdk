package com.joinesty.domains.staticVault.managers.race;

import com.joinesty.services.BaseModel;

import java.util.List;

public class RaceRequest extends BaseModel {

  private String race;

  private String raceHash;

  private String iv;

  private String authTag;

  private List<String> tags;

  public String getRace() {
    return race;
  }

  public void setRace(String race) {
    this.race = race;
  }

  public String getRaceHash() {
    return raceHash;
  }

  public void setRaceHash(String raceHash) {
    this.raceHash = raceHash;
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
