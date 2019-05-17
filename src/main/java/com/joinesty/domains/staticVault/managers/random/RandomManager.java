package com.joinesty.domains.staticVault.managers.random;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * RandomManager Class
 */
public class RandomManager {

    private StaticVault vault;

    private RandomManager() {

    }

    /**
     * Creates an instance of RandomManager.
     *
     * @param vault
     */
    public RandomManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new Random string to be tokenized for a specific static vault
     *
     * @param data
     * @param tags
     *
     * @return RandomResponse
     */
    public RandomResponse create(String data, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(data);

            RandomRequest request = new RandomRequest();
            request.setData(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<RandomResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/random"))
                    .body(request)
                    .asObject(RandomResponse.class);

            RandomResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the Random string token from a static vault
     *
     * @param id
     *
     * @return RandomResponse
     */
    public RandomResponse retrieve(String id) {
        try {
            HttpResponse<RandomResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/random/" + id))
                    .asObject(RandomResponse.class);

            RandomResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getData());
            responseBody.setData(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the Random token from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/random/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
