package com.joinesty.domains.staticVault.managers.race;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.List;

/**
 * RaceManager Class
 */
public class RaceManager {

  private StaticVault vault;

  private RaceManager() {

  }

  /**
   * Creates an instance of RaceManager.
   *
   * @param vault
   */
  public RaceManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new Race string to be aliased for a specific static vault
   *
   * @param race
   * @return RaceResponse
   */
  public RaceResponse create(String race) throws Exception {
    return this.create(race, null);
  }

  /**
   * Create a new Race string to be aliased for a specific static vault
   *
   * @param race
   * @param tags
   * @return RaceResponse
   */
  public RaceResponse create(String race, List<String> tags) throws Exception {
    AESDTO cipher = this.vault.encrypt(race);

    RaceRequest request = new RaceRequest();
    request.setRace(cipher.getEncryptedData());
    request.setRaceHash(this.vault.hash(race));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<RaceResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/race"))
      .body(request)
      .asObject(RaceResponse.class);

    RaceResponse responseBody = response.getBody();

    return responseBody;
  }

  /**
   * Retrieve the Race string alias from a static vault
   *
   * @param id
   * @return RaceResponse
   */
  public RaceResponse retrieve(String id) throws Exception {
    HttpResponse<RaceResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/race/" + id))
      .asObject(RaceResponse.class);

    RaceResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getRace());
    responseBody.setRace(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the Race alias from real race
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param race
   * @return List of RaceResponse
   */
  public RaceResponse[] retrieveFromRealData(String race) throws Exception {
    return this.retrieveFromRealData(race, null);
  }

  /**
   * Retrieve the Race alias from real race
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param race
   * @param tags
   * @return RaceResponse[]
   */
  public RaceResponse[] retrieveFromRealData(String race, List<String> tags) throws Exception {

    String hash = this.vault.hash(race);
    String url = "/vault/static/" + this.vault.getId() + "/race?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<RaceResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(RaceResponse[].class);

    RaceResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getRace()
      );

      responseBody[i].setRace(decrypted);
    }

    return responseBody;
  }

  /**
   * Delete the RaceManager alias from static vault
   *
   * @param id
   * @return boolean
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/race/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
