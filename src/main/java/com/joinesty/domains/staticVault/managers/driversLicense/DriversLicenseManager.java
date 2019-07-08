package com.joinesty.domains.staticVault.managers.driversLicense;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.List;

/**
 * DriversLicenseManager Class
 */
public class DriversLicenseManager {

  private StaticVault vault;

  private DriversLicenseManager() {

  }

  /**
   * Creates an instance of DriversLicenseManager.
   *
   * @param vault
   */
  public DriversLicenseManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new DriversLicense string to be aliased for a specific static vault
   *
   * @param driversLicense
   * @return DriversLicenseResponse
   */
  public DriversLicenseResponse create(String driversLicense) throws Exception {
    return this.create(driversLicense, null, null);
  }

  /**
   * Create a new DriversLicense string to be aliased for a specific static vault
   *
   * @param driversLicense
   * @param tags
   * @return DriversLicenseResponse
   */
  public DriversLicenseResponse create(String driversLicense, List<String> tags) throws Exception {
    return this.create(driversLicense, null, tags);
  }

  /**
   * Create a new DriversLicense string to be aliased for a specific static vault
   *
   * @param driversLicense
   * @param state
   * @return DriversLicenseResponse
   */
  public DriversLicenseResponse create(String driversLicense, String state) throws Exception {
    return this.create(driversLicense, state, null);
  }

  /**
   * Create a new DriversLicense string to be aliased for a specific static vault
   *
   * @param driversLicense
   * @param tags
   * @param state
   * @return DriversLicenseResponse
   */
  public DriversLicenseResponse create(String driversLicense, String state, List<String> tags) throws Exception {
    AESDTO cipher = this.vault.encrypt(driversLicense);
    String url = "/vault/static/" + this.vault.getId() + "/driverslicense";
    if (state != null) url += "/" + state;

    DriversLicenseRequest request = new DriversLicenseRequest();
    request.setDriverslicense(cipher.getEncryptedData());
    request.setDriverslicenseHash(this.vault.hash(driversLicense));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<DriversLicenseResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl(url))
      .body(request)
      .asObject(DriversLicenseResponse.class);

    DriversLicenseResponse responseBody = response.getBody();

    return responseBody;

  }

  /**
   * Retrieve the DriversLicense string alias from a static vault
   *
   * @param id
   * @return DriversLicenseResponse
   */
  public DriversLicenseResponse retrieve(String id) throws Exception {

    HttpResponse<DriversLicenseResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/driverslicense/" + id))
      .asObject(DriversLicenseResponse.class);

    DriversLicenseResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getDriverslicense());
    responseBody.setDriverslicense(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the DriversLicense alias from real driversLicense
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param driversLicense
   * @return List of DriversLicenseResponse
   */
  public DriversLicenseResponse[] retrieveFromRealData(String driversLicense) throws Exception {
    return this.retrieveFromRealData(driversLicense, null);
  }

  /**
   * Retrieve the DriversLicense alias from real driversLicense
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param driversLicense
   * @param tags
   * @return DriversLicenseResponse[]
   */
  public DriversLicenseResponse[] retrieveFromRealData(String driversLicense, List<String> tags) throws Exception {
    String hash = this.vault.hash(driversLicense);
    String url = "/vault/static/" + this.vault.getId() + "/driverslicense?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<DriversLicenseResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(DriversLicenseResponse[].class);

    DriversLicenseResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getDriverslicense()
      );

      responseBody[i].setDriverslicense(decrypted);
    }

    return responseBody;
  }

  /**
   * Delete the DriversLicense alias from static vault
   *
   * @param id
   * @return boolean
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/driverslicense/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
