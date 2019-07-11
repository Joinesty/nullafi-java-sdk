package com.joinesty.domains.staticVault.managers.passport;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.List;

/**
 * PassportManager Class
 */
public class PassportManager {

  private StaticVault vault;

  private PassportManager() {

  }

  /**
   * Creates an instance of PassportManager.
   *
   * @param vault Vault
   */
  public PassportManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new Passport string to be aliased for a specific static vault
   *
   * @param passport Passport
   * @return PassportResponse
   * @throws Exception Exception
   */
  public PassportResponse create(String passport) throws Exception {
    return this.create(passport, null);
  }

  /**
   * Create a new Passport string to be aliased for a specific static vault
   *
   * @param passport Passport
   * @param tags Tags
   * @return PassportResponse
   * @throws Exception Exception
   */
  public PassportResponse create(String passport, List<String> tags) throws Exception {
    AESDTO cipher = this.vault.encrypt(passport);

    PassportRequest request = new PassportRequest();
    request.setPassport(cipher.getEncryptedData());
    request.setPassportHash(this.vault.hash(passport));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<PassportResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/passport"))
      .body(request)
      .asObject(PassportResponse.class);

    PassportResponse responseBody = response.getBody();

    return responseBody;
  }

  /**
   * Retrieve the Passport string alias from a static vault
   *
   * @param id ID
   * @return PassportResponse
   * @throws Exception Exception
   */
  public PassportResponse retrieve(String id) throws Exception {
    HttpResponse<PassportResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/passport/" + id))
      .asObject(PassportResponse.class);

    PassportResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getPassport());
    responseBody.setPassport(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the Passport alias from real passport
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param passport Passport
   * @return List of PassportResponse
   * @throws Exception Exception
   */
  public PassportResponse[] retrieveFromRealData(String passport) throws Exception {
    return this.retrieveFromRealData(passport, null);
  }

  /**
   * Retrieve the Passport alias from real passport
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param passport Passport
   * @param tags Tags
   * @return PassportResponse[]
   * @throws Exception Exception
   */
  public PassportResponse[] retrieveFromRealData(String passport, List<String> tags) throws Exception {
    String hash = this.vault.hash(passport);
    String url = "/vault/static/" + this.vault.getId() + "/passport?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<PassportResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(PassportResponse[].class);

    PassportResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getPassport()
      );

      responseBody[i].setPassport(decrypted);
    }

    return responseBody;
  }

  /**
   * Delete the PassportManager alias from static vault
   *
   * @param id ID
   * @return boolean
   * @throws Exception Exception
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/passport/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
