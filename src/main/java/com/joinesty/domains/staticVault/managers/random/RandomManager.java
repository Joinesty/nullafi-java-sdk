package com.joinesty.domains.staticVault.managers.random;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.List;

/**
 * RandomManager Class
 */
public class RandomManager {

  private StaticVault vault;

  private RandomManager() {

  }

  /**
   * Creates an instance of RandomManager.
   *
   * @param vault Vault
   */
  public RandomManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new Random string to be aliased for a specific static vault
   *
   * @param data Data
   * @return RandomResponse
   * @throws Exception Exception
   */
  public RandomResponse create(String data) throws Exception {
    return this.create(data, null);
  }

  /**
   * Create a new Random string to be aliased for a specific static vault
   *
   * @param data Data
   * @param tags Tags
   * @return RandomResponse
   * @throws Exception Exception
   */
  public RandomResponse create(String data, List<String> tags) throws Exception {
    AESDTO cipher = this.vault.encrypt(data);

    RandomRequest request = new RandomRequest();
    request.setData(cipher.getEncryptedData());
    request.setDataHash(this.vault.hash(data));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<RandomResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/random"))
      .body(request)
      .asObject(RandomResponse.class);

    RandomResponse responseBody = response.getBody();

    return responseBody;
  }

  /**
   * Retrieve the Random string alias from a static vault
   *
   * @param id ID
   * @return RandomResponse
   * @throws Exception Exception
   */
  public RandomResponse retrieve(String id) throws Exception {
    HttpResponse<RandomResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/random/" + id))
      .asObject(RandomResponse.class);

    RandomResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getData());
    responseBody.setData(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the Random alias from real data
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param data Data
   * @return List of RandomResponse
   * @throws Exception Exception
   */
  public RandomResponse[] retrieveFromRealData(String data) throws Exception {
    return this.retrieveFromRealData(data, null);
  }

  /**
   * Retrieve the Random alias from real data
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param data Data
   * @param tags Tags
   * @return RandomResponse[]
   * @throws Exception Exception
   */
  public RandomResponse[] retrieveFromRealData(String data, List<String> tags) throws Exception {
    String hash = this.vault.hash(data);
    String url = "/vault/static/" + this.vault.getId() + "/random?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<RandomResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(RandomResponse[].class);

    RandomResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getData()
      );

      responseBody[i].setData(decrypted);
    }

    return responseBody;
  }

  /**
   * Delete the Random alias from static vault
   *
   * @param id ID
   * @return boolean
   * @throws Exception Exception
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/random/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
