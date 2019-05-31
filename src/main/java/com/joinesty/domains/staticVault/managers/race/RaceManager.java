package com.joinesty.domains.staticVault.managers.race;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * RaceManager Class
 */
public class RaceManager {

    private StaticVault vault;

    private RaceManager() {

    }

    /**
     * Creates an instance of RaceManager.
     *
     * @param vault
     */
    public RaceManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new Race string to be aliased for a specific static vault
     *
     * @param race
     * @param tags
     *
     * @return RaceResponse
     */
    public RaceResponse create(String race, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(race);

            RaceRequest request = new RaceRequest();
            request.setRace(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<RaceResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/race"))
                    .body(request)
                    .asObject(RaceResponse.class);

            RaceResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the Race string alias from a static vault
     *
     * @param id
     *
     * @return RaceResponse
     */
    public RaceResponse retrieve(String id) {
        try {
            HttpResponse<RaceResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/race/" + id))
                    .asObject(RaceResponse.class);

            RaceResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getRace());
            responseBody.setRace(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the RaceManager alias from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/race/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
