package com.joinesty.domains.staticVault.managers.firstName;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.List;

/**
 * FirstNameManager Class
 */
public class FirstNameManager {

  private StaticVault vault;

  private FirstNameManager() {

  }

  /**
   * Creates an instance of FirstNameManager.
   *
   * @param vault Vault
   */
  public FirstNameManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new FirstName string to be aliased for a specific static vault
   *
   * @param firstName First Name
   * @return FirstNameResponse
   * @throws Exception Exception
   */
  public FirstNameResponse create(String firstName) throws Exception {
    return this.create(firstName, null, null);
  }

  /**
   * Create a new FirstName string to be aliased for a specific static vault
   *
   * @param firstName First Name
   * @param tags Tags
   * @return FirstNameResponse
   * @throws Exception Exception
   */
  public FirstNameResponse create(String firstName, List<String> tags) throws Exception {
    return this.create(firstName, null, tags);
  }

  /**
   * Create a new FirstName string to be aliased for a specific static vault
   *
   * @param firstName First Name
   * @param gender Gender
   * @return FirstNameResponse
   * @throws Exception Exception
   */
  public FirstNameResponse create(String firstName, String gender) throws Exception {
    return this.create(firstName, gender, null);
  }

  /**
   * Create a new FirstName string to be aliased for a specific static vault
   *
   * @param firstName First Name
   * @param gender Gender
   * @param tags Tags
   * @return FirstNameResponse
   * @throws Exception Exception
   */
  public FirstNameResponse create(String firstName, String gender, List<String> tags) throws Exception {
    AESDTO cipher = this.vault.encrypt(firstName);
    String url = "/vault/static/" + this.vault.getId() + "/firstname";
    if (gender != null) url += "/" + gender;

    FirstNameRequest request = new FirstNameRequest();
    request.setFirstname(cipher.getEncryptedData());
    request.setFirstnameHash(this.vault.hash(firstName));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<FirstNameResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl(url))
      .body(request)
      .asObject(FirstNameResponse.class);

    FirstNameResponse responseBody = response.getBody();

    return responseBody;
  }

  /**
   * Retrieve the FirstName string alias from a static vault
   *
   * @param id ID
   * @return FirstNameResponse
   * @throws Exception Exception
   */
  public FirstNameResponse retrieve(String id) throws Exception {
    HttpResponse<FirstNameResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/firstname/" + id))
      .asObject(FirstNameResponse.class);

    FirstNameResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getFirstname());
    responseBody.setFirstname(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the FirstName alias from real firstName
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param firstName First Name
   * @return List of FirstNameResponse
   * @throws Exception Exception
   */
  public FirstNameResponse[] retrieveFromRealData(String firstName) throws Exception {
    return this.retrieveFromRealData(firstName, null);
  }

  /**
   * Retrieve the FirstName alias from real firstName
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param firstName First Name
   * @param tags Tags
   * @return FirstNameResponse[]
   * @throws Exception Exception
   */
  public FirstNameResponse[] retrieveFromRealData(String firstName, List<String> tags) throws Exception {
    String hash = this.vault.hash(firstName);
    String url = "/vault/static/" + this.vault.getId() + "/firstname?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<FirstNameResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(FirstNameResponse[].class);

    FirstNameResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getFirstname()
      );

      responseBody[i].setFirstname(decrypted);
    }

    return responseBody;
  }

  /**
   * Delete the FirstName alias from static vault
   *
   * @param id ID
   * @return boolean
   * @throws Exception Exception
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/firstname/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
