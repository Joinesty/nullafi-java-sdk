package com.joinesty.domains.staticVault.managers.address;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * AddressManager Class
 */
public class AddressManager {

    private StaticVault vault;

    private AddressManager() {

    }

    /**
     * Creates an instance of AddressManager.
     *
     * @param vault
     */
    public AddressManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new Address string to be tokenized for a specific static vault
     *
     * @param address
     * @param tags
     *
     * @return AddressResponse
     */
    public AddressResponse create(String address, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(address);

            AddressRequest request = new AddressRequest();
            request.setAddress(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<AddressResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/address"))
                    .body(request)
                    .asObject(AddressResponse.class);

            AddressResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the Address string token from a static vault
     *
     * @param id
     *
     * @return AddressResponse
     */
    public AddressResponse retrieve(String id) {
        try {
            HttpResponse<AddressResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/address/" + id))
                    .asObject(AddressResponse.class);

            AddressResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getAddress());
            responseBody.setAddress(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the Address token from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/address/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
