package com.joinesty.domains.staticVault.managers.taxpayer;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * TaxpayerManager Class
 */
public class TaxpayerManager {

    private StaticVault vault;

    private TaxpayerManager() {

    }

    /**
     * Creates an instance of TaxpayerManager.
     *
     * @param vault
     */
    public TaxpayerManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new Taxpayer string to be tokenized for a specific static vault
     *
     * @param taxpayer
     * @param tags
     *
     * @return TaxpayerResponse
     */
    public TaxpayerResponse create(String taxpayer, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(taxpayer);

            TaxpayerRequest request = new TaxpayerRequest();
            request.setTaxpayer(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<TaxpayerResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/taxpayer"))
                    .body(request)
                    .asObject(TaxpayerResponse.class);

            TaxpayerResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the Taxpayer string token from a static vault
     *
     * @param id
     *
     * @return TaxpayerResponse
     */
    public TaxpayerResponse retrieve(String id) {
        try {
            HttpResponse<TaxpayerResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/taxpayer/" + id))
                    .asObject(TaxpayerResponse.class);

            TaxpayerResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getTaxpayer());
            responseBody.setTaxpayer(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the TaxpayerManager token from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/taxpayer/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
