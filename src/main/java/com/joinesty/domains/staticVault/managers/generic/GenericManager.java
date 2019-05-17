package com.joinesty.domains.staticVault.managers.generic;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * GenericManager Class
 */
public class GenericManager {

    private StaticVault vault;

    private GenericManager() {

    }

    /**
     * Creates an instance of GenericManager.
     *
     * @param vault
     */
    public GenericManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new Generic string to be tokenized for a specific static vault
     *
     * @param data
     * @param template
     * @param tags
     *
     * @return GenericResponse
     */
    public GenericResponse create(String data, String template, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(data);

            GenericRequest request = new GenericRequest();
            request.setTemplate(template);
            request.setData(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<GenericResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/generic"))
                    .body(request)
                    .asObject(GenericResponse.class);

            GenericResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the Generic string token from a static vault
     *
     * @param id
     *
     * @return GenericResponse
     */
    public GenericResponse retrieve(String id) {
        try {
            HttpResponse<GenericResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/generic/" + id))
                    .asObject(GenericResponse.class);

            GenericResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getData());
            responseBody.setData(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the Generic token from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/generic/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
