package com.joinesty.domains.staticVault.managers.gender;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * GenderManager Class
 */
public class GenderManager {

    private StaticVault vault;

    private GenderManager() {

    }

    /**
     * Creates an instance of GenderManager.
     *
     * @param vault
     */
    public GenderManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new Gender string to be aliased for a specific static vault
     *
     * @param gender
     * @param tags
     *
     * @return GenderResponse
     */
    public GenderResponse create(String gender, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(gender);

            GenderRequest request = new GenderRequest();
            request.setGender(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<GenderResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/gender"))
                    .body(request)
                    .asObject(GenderResponse.class);

            GenderResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the Gender string alias from a static vault
     *
     * @param id
     *
     * @return GenderResponse
     */
    public GenderResponse retrieve(String id) {
        try {
            HttpResponse<GenderResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/gender/" + id))
                    .asObject(GenderResponse.class);

            GenderResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getGender());
            responseBody.setGender(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the GenderManager alias from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/gender/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
