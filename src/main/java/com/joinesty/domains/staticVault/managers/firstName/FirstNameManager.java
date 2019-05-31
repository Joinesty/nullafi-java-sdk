package com.joinesty.domains.staticVault.managers.firstName;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * FirstNameManager Class
 */
public class FirstNameManager {

    private StaticVault vault;

    private FirstNameManager() {

    }

    /**
     * Creates an instance of FirstNameManager.
     *
     * @param vault
     */
    public FirstNameManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new FirstName string to be aliased for a specific static vault
     *
     * @param firstName
     * @param tags
     *
     * @return FirstNameResponse
     */
    public FirstNameResponse create(String firstName, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(firstName);

            FirstNameRequest request = new FirstNameRequest();
            request.setFirstname(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<FirstNameResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/firstname"))
                    .body(request)
                    .asObject(FirstNameResponse.class);

            FirstNameResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the FirstName string alias from a static vault
     *
     * @param id
     *
     * @return FirstNameResponse
     */
    public FirstNameResponse retrieve(String id) {
        try {
            HttpResponse<FirstNameResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/firstname/" + id))
                    .asObject(FirstNameResponse.class);

            FirstNameResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getFirstname());
            responseBody.setFirstname(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the FirstName alias from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/firstname/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
