package com.joinesty.domains.staticVault.managers.lastName;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * LastNameManager Class
 */
public class LastNameManager {

    private StaticVault vault;

    private LastNameManager() {

    }

    /**
     * Creates an instance of LastNameManager.
     *
     * @param vault
     */
    public LastNameManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new LastName string to be aliased for a specific static vault
     *
     * @param lastName
     * @param tags
     *
     * @return LastNameResponse
     */
    public LastNameResponse create(String lastName, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(lastName);

            LastNameRequest request = new LastNameRequest();
            request.setLastname(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<LastNameResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/lastname"))
                    .body(request)
                    .asObject(LastNameResponse.class);

            LastNameResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the LastName string alias from a static vault
     *
     * @param id
     *
     * @return LastNameResponse
     */
    public LastNameResponse retrieve(String id) {
        try {
            HttpResponse<LastNameResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/lastname/" + id))
                    .asObject(LastNameResponse.class);

            LastNameResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getLastname());
            responseBody.setLastname(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the LastName alias from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/lastname/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
