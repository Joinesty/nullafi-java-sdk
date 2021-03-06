package com.joinesty;

import com.joinesty.domains.authenticate.AuthenticateRequest;
import com.joinesty.domains.authenticate.AuthenticateResponse;
import com.joinesty.domains.communicationVault.CommunicationVault;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.API;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.util.List;

/**
 * Client Class
 */
public class Client {

  private API api;

  private String hashKey;

  private String tenantId;

  /**
   * Class constructor.
   */
  public Client() {
    this.api = new API();

    Unirest.setDefaultHeader("accept", "application/json");
    Unirest.setDefaultHeader("Content-Type", "application/json");
  }

  /**
   * Authenticate the Client API
   *
   * @param apiKey API Key
   */
  void authenticate(String apiKey) throws Exception {
    AuthenticateRequest request = new AuthenticateRequest();
    request.setApiKey(apiKey);

    HttpResponse<AuthenticateResponse> response = Unirest
      .post(this.getApi().setUrl("/authentication/token"))
      .body(request)
      .asObject(AuthenticateResponse.class);

    AuthenticateResponse authenticateResponse = response.getBody();

    this.hashKey = authenticateResponse.getHashKey();
    this.tenantId = authenticateResponse.getTenantId();

    Unirest.setDefaultHeader("Authorization", authenticateResponse.getToken());
  }

  /**
   * Create a new static vault
   *
   * @param name Name
   * @return StaticVault
   * @throws Exception Exception
   */
  public StaticVault createStaticVault(String name) throws Exception {
    return this.createStaticVault(name, null);
  }

  /**
   * Create a new static vault
   *
   * @param name Name
   * @param tags Tags
   * @return StaticVault
   * @throws Exception Exception
   */
  public StaticVault createStaticVault(String name, List<String> tags) throws Exception {
    return StaticVault.createStaticVault(this, name, tags);
  }

  /**
   * Retrieve a existing static vault
   *
   * @param vaultId VaultId Vault ID
   * @param masterKey Master Key
   * @return StaticVault
   * @throws Exception Exception
   */
  public StaticVault retrieveStaticVault(String vaultId, String masterKey) throws Exception {
    return StaticVault.retrieveStaticVault(this, vaultId, masterKey);
  }

  /**
   * Delete an existing static vault
   *
   * @param vaultId VaultId Vault ID
   * @return boolean
   * @throws Exception Exception
   */
  public boolean deleteStaticVault(String vaultId) throws Exception {
    return StaticVault.delete(this, vaultId);
  }

  /**
   * Create a new communication vault
   *
   * @param name Name
   * @return CommunicationVault
   * @throws Exception Exception
   */
  public CommunicationVault createCommunicationVault(String name) throws Exception {
    return CommunicationVault.createCommunicationVault(this, name, null);
  }

  /**
   * Create a new communication vault
   *
   * @param name Name
   * @param tags Tags
   * @return CommunicationVault
   * @throws Exception Exception
   */
  public CommunicationVault createCommunicationVault(String name, List<String> tags) throws Exception {
    return CommunicationVault.createCommunicationVault(this, name, tags);
  }

  /**
   * Retrieve a existing communication vault
   *
   * @param vaultId VaultId Vault ID
   * @param masterKey Master Key
   * @return CommunicationVault
   * @throws Exception Exception
   */
  public CommunicationVault retrieveCommunicationVault(String vaultId, String masterKey) throws Exception {
    return CommunicationVault.retrieveCommunicationVault(this, vaultId, masterKey);
  }

  /**
   * Delete an existing communication vault
   *
   * @param vaultId VaultId Vault ID
   * @return boolean
   * @throws Exception Exception
   */
  public boolean deleteCommunicationVault(String vaultId) throws Exception {
    return CommunicationVault.delete(this, vaultId);
  }


  public API getApi() {
    return api;
  }

  public String getHashKey() {
    return hashKey;
  }

  public String getTenantId() {
    return tenantId;
  }
}
