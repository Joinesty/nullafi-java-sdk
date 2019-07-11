package com.joinesty.domains.staticVault.managers.dateOfBirth;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.List;

/**
 * DateOfBirthManager Class
 */
public class DateOfBirthManager {

  private StaticVault vault;

  private DateOfBirthManager() {

  }

  /**
   * Creates an instance of DateOfBirthManager.
   *
   * @param vault Vault
   */
  public DateOfBirthManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new DateOfBirth string to be aliased for a specific static vault
   *
   * @param dateOfBirth Date of Birth
   * @return DateOfBirthResponse
   * @throws Exception Exception
   */
  public DateOfBirthResponse create(String dateOfBirth) throws Exception {
    return this.create(dateOfBirth, null, null, null);
  }

  /**
   * Create a new DateOfBirth string to be aliased for a specific static vault
   *
   * @param dateOfBirth Date of Birth
   * @param tags Tags
   * @return DateOfBirthResponse
   * @throws Exception Exception
   */
  public DateOfBirthResponse create(String dateOfBirth, List<String> tags) throws Exception {
    return this.create(dateOfBirth, null, null, tags);
  }

  /**
   * Create a new DateOfBirth string to be aliased for a specific static vault
   *
   * @param dateOfBirth Date of Birth
   * @param year Year
   * @param month Month
   * @return DateOfBirthResponse
   * @throws Exception Exception
   */
  public DateOfBirthResponse create(String dateOfBirth, Integer year, Integer month) throws Exception {
    return this.create(dateOfBirth, year, month, null);
  }

  /**
   * Create a new DateOfBirth string to be aliased for a specific static vault
   *
   * @param dateOfBirth Date of Birth
   * @param year Year
   * @param month Month
   * @param tags Tags
   * @return DateOfBirthResponse
   * @throws Exception Exception
   */
  public DateOfBirthResponse create(String dateOfBirth, Integer year, Integer month, List<String> tags) throws Exception {
    AESDTO cipher = this.vault.encrypt(dateOfBirth);
    String url = "/vault/static/" + this.vault.getId() + "/dateofbirth?";
    if (year != null) url += "year=" + year + "&";
    if (month != null) url += "month=" + month;

    DateOfBirthRequest request = new DateOfBirthRequest();
    request.setDateofbirth(cipher.getEncryptedData());
    request.setDateofbirthHash(this.vault.hash(dateOfBirth));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<DateOfBirthResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl(url))
      .body(request)
      .asObject(DateOfBirthResponse.class);

    DateOfBirthResponse responseBody = response.getBody();

    return responseBody;
  }

  /**
   * Retrieve the DateOfBirth string alias from a static vault
   *
   * @param id ID
   * @return DateOfBirthResponse
   * @throws Exception Exception
   */
  public DateOfBirthResponse retrieve(String id) throws Exception {
    HttpResponse<DateOfBirthResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/dateofbirth/" + id))
      .asObject(DateOfBirthResponse.class);

    DateOfBirthResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getDateofbirth());
    responseBody.setDateofbirth(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the DateOfBirth alias from real dateOfBirth
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param dateOfBirth Date of Birth
   * @return List of DateOfBirthResponse
   * @throws Exception Exception
   */
  public DateOfBirthResponse[] retrieveFromRealData(String dateOfBirth) throws Exception {
    return this.retrieveFromRealData(dateOfBirth, null);
  }

  /**
   * Retrieve the DateOfBirth alias from real dateOfBirth
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param dateOfBirth Date of Birth
   * @param tags Tags
   * @return DateOfBirthResponse[]
   * @throws Exception Exception
   */
  public DateOfBirthResponse[] retrieveFromRealData(String dateOfBirth, List<String> tags) throws Exception {
    String hash = this.vault.hash(dateOfBirth);
    String url = "/vault/static/" + this.vault.getId() + "/dateofbirth?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<DateOfBirthResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(DateOfBirthResponse[].class);

    DateOfBirthResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getDateofbirth()
      );

      responseBody[i].setDateofbirth(decrypted);
    }

    return responseBody;
  }

  /**
   * Delete the DateOfBirth alias from static vault
   *
   * @param id ID
   * @return boolean
   * @throws Exception Exception
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/dateofbirth/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
