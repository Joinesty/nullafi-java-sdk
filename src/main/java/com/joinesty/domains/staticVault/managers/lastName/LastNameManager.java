package com.joinesty.domains.staticVault.managers.lastName;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * LastNameManager Class
 */
public class LastNameManager {

  private StaticVault vault;

  private LastNameManager() {

  }

  /**
   * Creates an instance of LastNameManager.
   *
   * @param vault
   */
  public LastNameManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new LastName string to be aliased for a specific static vault
   *
   * @param lastName
   * @return LastNameResponse
   */
  public LastNameResponse create(String lastName) throws Exception {
    return this.create(lastName, null, null);
  }

  /**
   * Create a new LastName string to be aliased for a specific static vault
   *
   * @param lastName
   * @param gender
   * @return LastNameResponse
   */
  public LastNameResponse create(String lastName, String gender) throws Exception {
    return this.create(lastName, gender, null);
  }

  /**
   * Create a new LastName string to be aliased for a specific static vault
   *
   * @param lastName
   * @param gender
   * @param tags
   * @return LastNameResponse
   */
  public LastNameResponse create(String lastName, String gender, List<String> tags) throws Exception {

    AESDTO cipher = this.vault.encrypt(lastName);
    String url = "/vault/static/" + this.vault.getId() + "/lastname";
    if (gender != null) url += "/" + gender;

    LastNameRequest request = new LastNameRequest();
    request.setLastname(cipher.getEncryptedData());
    request.setLastnameHash(this.vault.hash(lastName));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<LastNameResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl(url))
      .body(request)
      .asObject(LastNameResponse.class);

    LastNameResponse responseBody = response.getBody();

    return responseBody;


  }

  /**
   * Retrieve the LastName string alias from a static vault
   *
   * @param id
   * @return LastNameResponse
   */
  public LastNameResponse retrieve(String id) throws Exception {

    HttpResponse<LastNameResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/lastname/" + id))
      .asObject(LastNameResponse.class);

    LastNameResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getLastname());
    responseBody.setLastname(decrypted);

    return responseBody;


  }

  /**
   * Retrieve the LastName alias from real lastName
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param lastName
   * @return List of LastNameResponse
   */
  public LastNameResponse[] retrieveFromRealData(String lastName) throws Exception {
    return this.retrieveFromRealData(lastName, null);
  }

  /**
   * Retrieve the LastName alias from real lastName
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param lastName
   * @param tags
   * @return LastNameResponse[]
   */
  public LastNameResponse[] retrieveFromRealData(String lastName, List<String> tags) throws Exception {

    String hash = this.vault.hash(lastName);
    String url = "/vault/static/" + this.vault.getId() + "/lastname?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<LastNameResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(LastNameResponse[].class);

    LastNameResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getLastname()
      );

      responseBody[i].setLastname(decrypted);
    }

    return responseBody;


  }

  /**
   * Delete the LastName alias from static vault
   *
   * @param id
   * @return boolean
   */
  public boolean delete(String id) throws Exception {

    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/lastname/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");

  }


}
