package com.joinesty.domains.staticVault.managers.ssn;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * SsnManager Class
 */
public class SsnManager {

    private StaticVault vault;

    private SsnManager() {

    }

    /**
     * Creates an instance of SsnManager.
     *
     * @param vault
     */
    public SsnManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new Ssn string to be aliased for a specific static vault
     *
     * @param ssn
     * @param tags
     *
     * @return SsnResponse
     */
    public SsnResponse create(String ssn, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(ssn);

            SsnRequest request = new SsnRequest();
            request.setSsn(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<SsnResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/ssn"))
                    .body(request)
                    .asObject(SsnResponse.class);

            SsnResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the Ssn string alias from a static vault
     *
     * @param id
     *
     * @return SsnResponse
     */
    public SsnResponse retrieve(String id) {
        try {
            HttpResponse<SsnResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/ssn/" + id))
                    .asObject(SsnResponse.class);

            SsnResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getSsn());
            responseBody.setSsn(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the SsnManager alias from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/ssn/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
