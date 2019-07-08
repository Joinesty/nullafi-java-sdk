package com.joinesty.domains.staticVault.managers.address;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.List;

/**
 * AddressManager Class
 */
public class AddressManager {

  private StaticVault vault;

  private AddressManager() {

  }

  /**
   * Creates an instance of AddressManager.
   *
   * @param vault
   */
  public AddressManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new Address string to be aliased for a specific static vault
   *
   * @param address
   * @return AddressResponse
   */
  public AddressResponse create(String address) throws Exception {
    return this.create(address, null, null);
  }

  /**
   * Create a new Address string to be aliased for a specific static vault
   *
   * @param address
   * @param tags
   * @return AddressResponse
   */
  public AddressResponse create(String address, List<String> tags) throws Exception {
    return this.create(address, null, tags);
  }

  /**
   * Create a new Address string to be aliased for a specific static vault
   *
   * @param address
   * @param state
   * @return AddressResponse
   */
  public AddressResponse create(String address, String state) throws Exception {
    return this.create(address, state, null);
  }

  /**
   * Create a new Address string to be aliased for a specific static vault
   *
   * @param address
   * @param state
   * @param tags
   * @return AddressResponse
   */
  public AddressResponse create(String address, String state, List<String> tags) throws Exception {
    AESDTO cipher = this.vault.encrypt(address);
    String url = "/vault/static/" + this.vault.getId() + "/address";
    if (state != null) url += "/" + state;

    AddressRequest request = new AddressRequest();
    request.setAddress(cipher.getEncryptedData());
    request.setAddressHash(this.vault.hash(address));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<AddressResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl(url))
      .body(request)
      .asObject(AddressResponse.class);

    AddressResponse responseBody = response.getBody();

    return responseBody;
  }

  /**
   * Retrieve the Address string alias from a static vault
   *
   * @param id
   * @return AddressResponse
   */
  public AddressResponse retrieve(String id) throws Exception {
    HttpResponse<AddressResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/address/" + id))
      .asObject(AddressResponse.class);

    AddressResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getAddress());
    responseBody.setAddress(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the Address alias from real address
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param address
   * @return List of AddressResponse
   */
  public AddressResponse[] retrieveFromRealData(String address) throws Exception {
    return this.retrieveFromRealData(address, null);
  }

  /**
   * Retrieve the Address alias from real address
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param address
   * @param tags
   * @return AddressResponse[]
   */
  public AddressResponse[] retrieveFromRealData(String address, List<String> tags) throws Exception {
    String hash = this.vault.hash(address);
    String url = "/vault/static/" + this.vault.getId() + "/address?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<AddressResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(AddressResponse[].class);

    AddressResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getAddress()
      );

      responseBody[i].setAddress(decrypted);
    }

    return responseBody;
  }

  /**
   * Delete the Address alias from static vault
   *
   * @param id
   * @return boolean
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/address/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
