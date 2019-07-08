package com.joinesty.domains.staticVault.managers.placeOfBirth;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.List;

/**
 * PlaceOfBirthManager Class
 */
public class PlaceOfBirthManager {

  private StaticVault vault;

  private PlaceOfBirthManager() {

  }

  /**
   * Creates an instance of PlaceOfBirthManager.
   *
   * @param vault
   */
  public PlaceOfBirthManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new PlaceOfBirth string to be aliased for a specific static vault
   *
   * @param placeOfBirth
   * @return PlaceOfBirthResponse
   */
  public PlaceOfBirthResponse create(String placeOfBirth) throws Exception {
    return this.create(placeOfBirth, null, null);
  }

  /**
   * Create a new PlaceOfBirth string to be aliased for a specific static vault
   *
   * @param placeOfBirth
   * @param state
   * @return PlaceOfBirthResponse
   */
  public PlaceOfBirthResponse create(String placeOfBirth, String state) throws Exception {
    return this.create(placeOfBirth, state, null);
  }

  /**
   * Create a new PlaceOfBirth string to be aliased for a specific static vault
   *
   * @param placeOfBirth
   * @param tags
   * @return PlaceOfBirthResponse
   */
  public PlaceOfBirthResponse create(String placeOfBirth, List<String> tags) throws Exception {
    return this.create(placeOfBirth, null, tags);
  }

  /**
   * Create a new PlaceOfBirth string to be aliased for a specific static vault
   *
   * @param placeOfBirth
   * @param state
   * @param tags
   * @return PlaceOfBirthResponse
   */
  public PlaceOfBirthResponse create(String placeOfBirth, String state, List<String> tags) throws Exception {
    AESDTO cipher = this.vault.encrypt(placeOfBirth);
    String url = "/vault/static/" + this.vault.getId() + "/placeofbirth";
    if (state != null) url += "/" + state;

    PlaceOfBirthRequest request = new PlaceOfBirthRequest();
    request.setPlaceofbirth(cipher.getEncryptedData());
    request.setPlaceofbirthHash(this.vault.hash(placeOfBirth));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<PlaceOfBirthResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl(url))
      .body(request)
      .asObject(PlaceOfBirthResponse.class);

    PlaceOfBirthResponse responseBody = response.getBody();

    return responseBody;
  }

  /**
   * Retrieve the PlaceOfBirth string alias from a static vault
   *
   * @param id
   * @return PlaceOfBirthResponse
   */
  public PlaceOfBirthResponse retrieve(String id) throws Exception {
    HttpResponse<PlaceOfBirthResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/placeofbirth/" + id))
      .asObject(PlaceOfBirthResponse.class);

    PlaceOfBirthResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getPlaceofbirth());
    responseBody.setPlaceofbirth(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the PlaceOfBirth alias from real placeOfBirth
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by place created.
   *
   * @param placeOfBirth
   * @return List of PlaceOfBirthResponse
   */
  public PlaceOfBirthResponse[] retrieveFromRealData(String placeOfBirth) throws Exception {
    return this.retrieveFromRealData(placeOfBirth, null);
  }

  /**
   * Retrieve the PlaceOfBirth alias from real placeOfBirth
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by place created.
   *
   * @param placeOfBirth
   * @param tags
   * @return PlaceOfBirthResponse[]
   */
  public PlaceOfBirthResponse[] retrieveFromRealData(String placeOfBirth, List<String> tags) throws Exception {
    String hash = this.vault.hash(placeOfBirth);
    String url = "/vault/static/" + this.vault.getId() + "/placeofbirth?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<PlaceOfBirthResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(PlaceOfBirthResponse[].class);

    PlaceOfBirthResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getPlaceofbirth()
      );

      responseBody[i].setPlaceofbirth(decrypted);
    }

    return responseBody;
  }

  /**
   * Delete the PlaceOfBirth alias from static vault
   *
   * @param id
   * @return boolean
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/placeofbirth/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
