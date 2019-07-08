package com.joinesty.domains.staticVault.managers.vehicleRegistration;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * VehicleRegistrationManager Class
 */
public class VehicleRegistrationManager {

  private StaticVault vault;

  private VehicleRegistrationManager() {

  }

  /**
   * Creates an instance of VehicleRegistrationManager.
   *
   * @param vault
   */
  public VehicleRegistrationManager(StaticVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new VehicleRegistration string to be aliased for a specific static vault
   *
   * @param vehicleRegistration
   * @return VehicleRegistrationResponse
   */
  public VehicleRegistrationResponse create(String vehicleRegistration) throws Exception {
    return this.create(vehicleRegistration, null);
  }

  /**
   * Create a new VehicleRegistration string to be aliased for a specific static vault
   *
   * @param vehicleRegistration
   * @param tags
   * @return VehicleRegistrationResponse
   */
  public VehicleRegistrationResponse create(String vehicleRegistration, List<String> tags) throws Exception {
    AESDTO cipher = this.vault.encrypt(vehicleRegistration);

    VehicleRegistrationRequest request = new VehicleRegistrationRequest();
    request.setVehicleregistration(cipher.getEncryptedData());
    request.setVehicleregistrationHash(this.vault.hash(vehicleRegistration));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<VehicleRegistrationResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/vehicleregistration"))
      .body(request)
      .asObject(VehicleRegistrationResponse.class);

    VehicleRegistrationResponse responseBody = response.getBody();

    return responseBody;
  }

  /**
   * Retrieve the VehicleRegistration string alias from a static vault
   *
   * @param id
   * @return VehicleRegistrationResponse
   */
  public VehicleRegistrationResponse retrieve(String id) throws Exception {
    HttpResponse<VehicleRegistrationResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/vehicleregistration/" + id))
      .asObject(VehicleRegistrationResponse.class);

    VehicleRegistrationResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getVehicleregistration());
    responseBody.setVehicleregistration(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the VehicleRegistration alias from real vehicleRegistration
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param vehicleRegistration
   * @return List of VehicleRegistrationResponse
   */
  public VehicleRegistrationResponse[] retrieveFromRealData(String vehicleRegistration) throws Exception {
    return this.retrieveFromRealData(vehicleRegistration, null);
  }

  /**
   * Retrieve the VehicleRegistration alias from real vehicleRegistration
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param vehicleRegistration
   * @param tags
   * @return VehicleRegistrationResponse[]
   */
  public VehicleRegistrationResponse[] retrieveFromRealData(String vehicleRegistration, List<String> tags) throws Exception {
    String hash = this.vault.hash(vehicleRegistration);
    String url = "/vault/static/" + this.vault.getId() + "/vehicleregistration?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<VehicleRegistrationResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(VehicleRegistrationResponse[].class);

    VehicleRegistrationResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getVehicleregistration()
      );

      responseBody[i].setVehicleregistration(decrypted);
    }

    return responseBody;
  }

  /**
   * Delete the VehicleRegistration alias from static vault
   *
   * @param id
   * @return boolean
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/vehicleregistration/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
