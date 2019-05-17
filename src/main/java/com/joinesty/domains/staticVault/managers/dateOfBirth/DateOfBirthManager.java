package com.joinesty.domains.staticVault.managers.dateOfBirth;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * DateOfBirthManager Class
 */
public class DateOfBirthManager {

    private StaticVault vault;

    private DateOfBirthManager() {

    }

    /**
     * Creates an instance of DateOfBirthManager.
     *
     * @param vault
     */
    public DateOfBirthManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new DateOfBirth string to be tokenized for a specific static vault
     *
     * @param dateOfBirth
     * @param tags
     *
     * @return DateOfBirthResponse
     */
    public DateOfBirthResponse create(String dateOfBirth, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(dateOfBirth);

            DateOfBirthRequest request = new DateOfBirthRequest();
            request.setDateOfBirth(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<DateOfBirthResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/dateofbirth"))
                    .body(request)
                    .asObject(DateOfBirthResponse.class);

            DateOfBirthResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the DateOfBirth string token from a static vault
     *
     * @param id
     *
     * @return DateOfBirthResponse
     */
    public DateOfBirthResponse retrieve(String id) {
        try {
            HttpResponse<DateOfBirthResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/dateofbirth/" + id))
                    .asObject(DateOfBirthResponse.class);

            DateOfBirthResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getDateOfBirth());
            responseBody.setDateOfBirth(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the DateOfBirth token from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/dateofbirth/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
