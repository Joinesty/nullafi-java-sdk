package com.joinesty.domains.staticVault.managers.passport;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * PassportManager Class
 */
public class PassportManager {

    private StaticVault vault;

    private PassportManager() {

    }

    /**
     * Creates an instance of PassportManager.
     *
     * @param vault
     */
    public PassportManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new Passport string to be tokenized for a specific static vault
     *
     * @param passport
     * @param tags
     *
     * @return PassportResponse
     */
    public PassportResponse create(String passport, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(passport);

            PassportRequest request = new PassportRequest();
            request.setPassport(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<PassportResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/passport"))
                    .body(request)
                    .asObject(PassportResponse.class);

            PassportResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the Passport string token from a static vault
     *
     * @param id
     *
     * @return PassportResponse
     */
    public PassportResponse retrieve(String id) {
        try {
            HttpResponse<PassportResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/passport/" + id))
                    .asObject(PassportResponse.class);

            PassportResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getPassport());
            responseBody.setPassport(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the PassportManager token from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/passport/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
