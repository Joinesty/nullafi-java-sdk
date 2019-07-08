package com.joinesty.domains.staticVault.managers.generic;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * GenericManager Class
 */
public class GenericManager {

  private StaticVault vault;

  private GenericManager() {

  }

  /**
   * Creates an instance of GenericManager.
   *
   * @param vault
   */
  public GenericManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new Generic string to be aliased for a specific static vault
   *
   * @param data
   * @param template
   * @return GenericResponse
   */
  public GenericResponse create(String data, String template) throws Exception {
    return this.create(data, template, null);
  }

  /**
   * Create a new Generic string to be aliased for a specific static vault
   *
   * @param data
   * @param template
   * @param tags
   * @return GenericResponse
   */
  public GenericResponse create(String data, String template, List<String> tags) throws Exception {

    AESDTO cipher = this.vault.encrypt(data);

    GenericRequest request = new GenericRequest();
    request.setTemplate(template);
    request.setData(cipher.getEncryptedData());
    request.setDataHash(this.vault.hash(data));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<GenericResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/generic"))
      .body(request)
      .asObject(GenericResponse.class);

    GenericResponse responseBody = response.getBody();

    return responseBody;
  }

  /**
   * Retrieve the Generic string alias from a static vault
   *
   * @param id
   * @return GenericResponse
   */
  public GenericResponse retrieve(String id) throws Exception {
    HttpResponse<GenericResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/generic/" + id))
      .asObject(GenericResponse.class);

    GenericResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getData());
    responseBody.setData(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the Generic alias from real data
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param data
   * @return List of GenericResponse
   */
  public GenericResponse[] retrieveFromRealData(String data) throws Exception {
    return this.retrieveFromRealData(data, null);
  }

  /**
   * Retrieve the Generic alias from real data
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param data
   * @param tags
   * @return GenericResponse[]
   */
  public GenericResponse[] retrieveFromRealData(String data, List<String> tags) throws Exception {
    String hash = this.vault.hash(data);
    String url = "/vault/static/" + this.vault.getId() + "/generic?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<GenericResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(GenericResponse[].class);

    GenericResponse[] responseBody = response.getBody();

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
   * Delete the Generic alias from static vault
   *
   * @param id
   * @return boolean
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/generic/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
