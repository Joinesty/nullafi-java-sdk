package com.joinesty.domains.communicationVault.managers.email;

import com.joinesty.domains.communicationVault.CommunicationVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * EmailManager Class
 */
public class EmailManager {

  private CommunicationVault vault;

  private EmailManager() {

  }

  /**
   * Creates an instance of EmailManager.
   *
   * @param vault Vault
   */
  public EmailManager(CommunicationVault vault) {
    this.vault = vault;
  }

  /**
   * Create a new Email string to be aliased for a specific static vault
   *
   * @param email Email Email
   * @return EmailResponse
   * @throws Exception Exception
   */
  public EmailResponse create(String email) throws Exception {
    return this.create(email, null);
  }

  /**
   * Create a new Email string to be aliased for a specific static vault
   *
   * @param email Email
   * @param tags Tags
   * @return EmailResponse
   * @throws Exception Exception
   */
  public EmailResponse create(String email, List<String> tags) throws Exception {
    AESDTO cipher = this.vault.encrypt(email);

    EmailRequest request = new EmailRequest();
    request.setEmail(cipher.getEncryptedData());
    request.setEmailHash(this.vault.hash(email));
    request.setAuthTag(cipher.getAuthenticationTag());
    request.setIv(cipher.getInitializationVector());
    request.setTags(tags);

    HttpResponse<EmailResponse> response = Unirest
      .post(this.vault.getClient().getApi().setUrl("/vault/communication/" + this.vault.getId() + "/email"))
      .body(request)
      .asObject(EmailResponse.class);

    EmailResponse responseBody = response.getBody();

    return responseBody;
  }

  /**
   * Retrieve the Email string alias from a communication vault
   *
   * @param id ID
   * @return EmailResponse
   * @throws Exception Exception
   */
  public EmailResponse retrieve(String id) throws Exception {
    HttpResponse<EmailResponse> response = Unirest
      .get(this.vault.getClient().getApi().setUrl("/vault/communication/" + this.vault.getId() + "/email/" + id))
      .asObject(EmailResponse.class);

    EmailResponse responseBody = response.getBody();
    String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getEmail());
    responseBody.setEmail(decrypted);

    return responseBody;
  }

  /**
   * Retrieve the Email alias from real email
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param email Email
   * @return List of EmailResponse
   * @throws Exception Exception
   */
  public EmailResponse[] retrieveFromRealData(String email) throws Exception {
    return this.retrieveFromRealData(email, null);
  }

  /**
   * Retrieve the Email alias from real email
   * Real value must be an exact match and will also be case sensitive.
   * Returns an array of matching values.Array will be sorted by date created.
   *
   * @param email Email
   * @param tags Tags
   * @return EmailResponse[]
   * @throws Exception Exception
   */
  public EmailResponse[] retrieveFromRealData(String email, List<String> tags) throws Exception {
    String hash = this.vault.hash(email);
    String url = "/vault/communication/" + this.vault.getId() + "/email?hash=" + URLEncoder.encode(hash, "UTF-8");

    if (tags != null) {
      for (int i = 0; i < tags.size(); i++) {
        url += ("&tag=" + URLEncoder.encode(tags.get(i), "UTF-8"));
      }
    }

    HttpResponse<EmailResponse[]> response = Unirest
      .get(this.vault.getClient().getApi().setUrl(url))
      .asObject(EmailResponse[].class);

    EmailResponse[] responseBody = response.getBody();

    for (int i = 0; i < responseBody.length; i++) {
      String decrypted = this.vault.decrypt(
        responseBody[i].getIv(),
        responseBody[i].getAuthTag(),
        responseBody[i].getEmail()
      );

      responseBody[i].setEmail(decrypted);
    }

    return responseBody;
  }

  /**
   * Delete the Email alias from communication vault
   *
   * @param id ID
   * @return boolean
   * @throws Exception Exception
   */
  public boolean delete(String id) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(this.vault.getClient().getApi().setUrl("/vault/communication/" + this.vault.getId() + "/email/" + id))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }


}
