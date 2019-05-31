package com.joinesty.domains.communicationVault.managers.email;

import com.joinesty.domains.communicationVault.CommunicationVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

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
     * @param vault
     */
    public EmailManager(CommunicationVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new Email string to be aliased for a specific static vault
     *
     * @param email
     * @param tags
     *
     * @return EmailResponse
     */
    public EmailResponse create(String email, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(email);

            EmailRequest request = new EmailRequest();
            request.setEmail(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<EmailResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/communication/" + this.vault.getId() + "/email"))
                    .body(request)
                    .asObject(EmailResponse.class);

            EmailResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the Email string alias from a communication vault
     *
     * @param id
     *
     * @return EmailResponse
     */
    public EmailResponse retrieve(String id) {
        try {
            HttpResponse<EmailResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/communication/" + this.vault.getId() + "/email/" + id))
                    .asObject(EmailResponse.class);

            EmailResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getEmail());
            responseBody.setEmail(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the Email alias from communication vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/communication/" + this.vault.getId() + "/email/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
