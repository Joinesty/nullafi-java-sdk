package com.joinesty.domains.staticVault.managers.gender;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.List;

/**
 * GenderManager Class
 */
public class GenderManager {

  private StaticVault vault;

  private GenderManager() {

  }

  /**
   * Creates an instance of GenderManager.
   *
   * @param vault
   */
  public GenderManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new Gender string to be aliased for a specific static vault
   *
   * @param gender
   * @return GenderResponse
   */
  public GenderResponse create(String gender) throws Exception {
    return this.create(gender, null);
  }

  /**
   * Create a new Gender string to be aliased for a specific static vault
   *
   * @param gender
   * @param tags
   * @return GenderResponse
   */
  public GenderResponse create(String gender, List<String> tags) throws Exception {
    AESDTO cipher = this.vault.encrypt(gender);

    GenderRequest request = new GenderRequest();
    request.setGender(cipher.getEncryptedData());
    request.setGenderHash(this.vault.hash(gender));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<GenderResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/gender"))
      .body(request)
      .asObject(GenderResponse.class);

    GenderResponse responseBody = response.getBody();

    return responseBody;
  }

  /**
   * Retrieve the Gender string alias from a static vault
   *
   * @param id
   * @return GenderResponse
   */
  public GenderResponse retrieve(String id) throws Exception {
    HttpResponse<GenderResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/gender/" + id))
      .asObject(GenderResponse.class);

    GenderResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getGender());
    responseBody.setGender(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the Gender alias from real gender
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param gender
   * @return List of GenderResponse
   */
  public GenderResponse[] retrieveFromRealData(String gender) throws Exception {
    return this.retrieveFromRealData(gender, null);
  }

  /**
   * Retrieve the Gender alias from real gender
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param gender
   * @param tags
   * @return GenderResponse[]
   */
  public GenderResponse[] retrieveFromRealData(String gender, List<String> tags) throws Exception {

    String hash = this.vault.hash(gender);
    String url = "/vault/static/" + this.vault.getId() + "/gender?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<GenderResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(GenderResponse[].class);

    GenderResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getGender()
      );

      responseBody[i].setGender(decrypted);
    }

    return responseBody;
  }

  /**
   * Delete the GenderManager alias from static vault
   *
   * @param id
   * @return boolean
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/gender/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
