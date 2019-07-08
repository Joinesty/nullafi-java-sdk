package com.joinesty.domains.staticVault.managers.taxpayer;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * TaxpayerManager Class
 */
public class TaxpayerManager {

  private StaticVault vault;

  private TaxpayerManager() {

  }

  /**
   * Creates an instance of TaxpayerManager.
   *
   * @param vault
   */
  public TaxpayerManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new Taxpayer string to be aliased for a specific static vault
   *
   * @param taxpayer
   * @return TaxpayerResponse
   */
  public TaxpayerResponse create(String taxpayer) throws Exception {
    return this.create(taxpayer, null);
  }

  /**
   * Create a new Taxpayer string to be aliased for a specific static vault
   *
   * @param taxpayer
   * @param tags
   * @return TaxpayerResponse
   */
  public TaxpayerResponse create(String taxpayer, List<String> tags) throws Exception {
    AESDTO cipher = this.vault.encrypt(taxpayer);

    TaxpayerRequest request = new TaxpayerRequest();
    request.setTaxpayer(cipher.getEncryptedData());
    request.setTaxpayerHash(this.vault.hash(taxpayer));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<TaxpayerResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/taxpayer"))
      .body(request)
      .asObject(TaxpayerResponse.class);

    TaxpayerResponse responseBody = response.getBody();

    return responseBody;
  }

  /**
   * Retrieve the Taxpayer string alias from a static vault
   *
   * @param id
   * @return TaxpayerResponse
   */
  public TaxpayerResponse retrieve(String id) throws Exception {
    HttpResponse<TaxpayerResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/taxpayer/" + id))
      .asObject(TaxpayerResponse.class);

    TaxpayerResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getTaxpayer());
    responseBody.setTaxpayer(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the Taxpayer alias from real taxpayer
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param taxpayer
   * @return List of TaxpayerResponse
   */
  public TaxpayerResponse[] retrieveFromRealData(String taxpayer) throws Exception {
    return this.retrieveFromRealData(taxpayer, null);
  }

  /**
   * Retrieve the Taxpayer alias from real taxpayer
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param taxpayer
   * @param tags
   * @return TaxpayerResponse[]
   */
  public TaxpayerResponse[] retrieveFromRealData(String taxpayer, List<String> tags) throws Exception {
    String hash = this.vault.hash(taxpayer);
    String url = "/vault/static/" + this.vault.getId() + "/taxpayer?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<TaxpayerResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(TaxpayerResponse[].class);

    TaxpayerResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getTaxpayer()
      );

      responseBody[i].setTaxpayer(decrypted);
    }

    return responseBody;
  }

  /**
   * Delete the TaxpayerManager alias from static vault
   *
   * @param id
   * @return boolean
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/taxpayer/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
