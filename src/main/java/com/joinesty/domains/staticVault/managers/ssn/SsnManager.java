package com.joinesty.domains.staticVault.managers.ssn;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.List;

/**
 * SsnManager Class
 */
public class SsnManager {

  private StaticVault vault;

  private SsnManager() {

  }

  /**
   * Creates an instance of SsnManager.
   *
   * @param vault Vault
   */
  public SsnManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new Ssn string to be aliased for a specific static vault
   *
   * @param ssn Ssn
   * @return SsnResponse
   * @throws Exception Exception
   */
  public SsnResponse create(String ssn) throws Exception {
    return this.create(ssn, null, null);
  }

  /**
   * Create a new Ssn string to be aliased for a specific static vault
   *
   * @param ssn Ssn
   * @param state State
   * @return SsnResponse
   * @throws Exception Exception
   */
  public SsnResponse create(String ssn, String state) throws Exception {
    return this.create(ssn, state, null);
  }

  /**
   * Create a new Ssn string to be aliased for a specific static vault
   *
   * @param ssn Ssn
   * @param tags Tags
   * @return SsnResponse
   * @throws Exception Exception
   */
  public SsnResponse create(String ssn, List<String> tags) throws Exception {
    return this.create(ssn, null, tags);
  }

  /**
   * Create a new Ssn string to be aliased for a specific static vault
   *
   * @param ssn Ssn
   * @param tags Tags
   * @param state State
   * @return SsnResponse
   * @throws Exception Exception
   */
  public SsnResponse create(String ssn, String state, List<String> tags) throws Exception {

    AESDTO cipher = this.vault.encrypt(ssn);
    String url = "/vault/static/" + this.vault.getId() + "/ssn";
    if (state != null) url += "/" + state;

    SsnRequest request = new SsnRequest();
    request.setSsn(cipher.getEncryptedData());
    request.setSsnHash(this.vault.hash(ssn));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<SsnResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl(url))
      .body(request)
      .asObject(SsnResponse.class);

    SsnResponse responseBody = response.getBody();

    return responseBody;
  }

  /**
   * Retrieve the Ssn string alias from a static vault
   *
   * @param id ID
   * @return SsnResponse
   * @throws Exception Exception
   */
  public SsnResponse retrieve(String id) throws Exception {
    HttpResponse<SsnResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/ssn/" + id))
      .asObject(SsnResponse.class);

    SsnResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getSsn());
    responseBody.setSsn(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the Ssn alias from real ssn
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param ssn Ssn
   * @return List of SsnResponse
   * @throws Exception Exception
   */
  public SsnResponse[] retrieveFromRealData(String ssn) throws Exception {
    return this.retrieveFromRealData(ssn, null);
  }

  /**
   * Retrieve the Ssn alias from real ssn
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param ssn Ssn
   * @param tags Tags
   * @return SsnResponse[]
   * @throws Exception Exception
   */
  public SsnResponse[] retrieveFromRealData(String ssn, List<String> tags) throws Exception {
    String hash = this.vault.hash(ssn);
    String url = "/vault/static/" + this.vault.getId() + "/ssn?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<SsnResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(SsnResponse[].class);

    SsnResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getSsn()
      );

      responseBody[i].setSsn(decrypted);
    }

    return responseBody;
  }

  /**
   * Delete the SsnManager alias from static vault
   *
   * @param id ID
   * @return boolean
   * @throws Exception Exception
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/ssn/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
