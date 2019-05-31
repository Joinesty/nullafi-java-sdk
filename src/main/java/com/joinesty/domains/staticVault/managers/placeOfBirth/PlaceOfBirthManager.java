package com.joinesty.domains.staticVault.managers.placeOfBirth;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * PlaceOfBirthManager Class
 */
public class PlaceOfBirthManager {

    private StaticVault vault;

    private PlaceOfBirthManager() {

    }

    /**
     * Creates an instance of PlaceOfBirthManager.
     *
     * @param vault
     */
    public PlaceOfBirthManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new PlaceOfBirth string to be aliased for a specific static vault
     *
     * @param placeOfBirth
     * @param tags
     *
     * @return PlaceOfBirthResponse
     */
    public PlaceOfBirthResponse create(String placeOfBirth, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(placeOfBirth);

            PlaceOfBirthRequest request = new PlaceOfBirthRequest();
            request.setPlaceofbirth(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<PlaceOfBirthResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/placeofbirth"))
                    .body(request)
                    .asObject(PlaceOfBirthResponse.class);

            PlaceOfBirthResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the PlaceOfBirth string alias from a static vault
     *
     * @param id
     *
     * @return PlaceOfBirthResponse
     */
    public PlaceOfBirthResponse retrieve(String id) {
        try {
            HttpResponse<PlaceOfBirthResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/placeofbirth/" + id))
                    .asObject(PlaceOfBirthResponse.class);

            PlaceOfBirthResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getPlaceofbirth());
            responseBody.setPlaceofbirth(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the PlaceOfBirth alias from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/placeofbirth/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
