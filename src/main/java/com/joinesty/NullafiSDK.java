package com.joinesty;

/**
 * NullafiSDK Class
 */
public class NullafiSDK {

  private String apiKey;

  private Client client;

  /**
   * Private constructor without params
   */
  private NullafiSDK() {

  }

  /**
   * Creates an instance of NullafiSDK.
   *
   * @param apiKey API Key
   */
  public NullafiSDK(String apiKey) {
    this.apiKey = apiKey;
  }

  /**
   * Create a new instance of Client
   *
   * @return Client
   * @throws Exception Exception
   */
  public Client createClient() throws Exception {
    this.client = new Client();
    this.client.authenticate(this.apiKey);
    return this.client;
  }

}
