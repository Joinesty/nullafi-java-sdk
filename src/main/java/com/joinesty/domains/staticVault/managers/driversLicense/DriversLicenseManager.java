package com.joinesty.domains.staticVault.managers.driversLicense;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * DriversLicenseManager Class
 */
public class DriversLicenseManager {

    private StaticVault vault;

    private DriversLicenseManager() {

    }

    /**
     * Creates an instance of DriversLicenseManager.
     *
     * @param vault
     */
    public DriversLicenseManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new DriversLicense string to be tokenized for a specific static vault
     *
     * @param driversLicense
     * @param tags
     *
     * @return DriversLicenseResponse
     */
    public DriversLicenseResponse create(String driversLicense, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(driversLicense);

            DriversLicenseRequest request = new DriversLicenseRequest();
            request.setDriversLicense(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<DriversLicenseResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/driverslicense"))
                    .body(request)
                    .asObject(DriversLicenseResponse.class);

            DriversLicenseResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the DriversLicense string token from a static vault
     *
     * @param id
     *
     * @return DriversLicenseResponse
     */
    public DriversLicenseResponse retrieve(String id) {
        try {
            HttpResponse<DriversLicenseResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/driverslicense/" + id))
                    .asObject(DriversLicenseResponse.class);

            DriversLicenseResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getDriversLicense());
            responseBody.setDriversLicense(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the DriversLicense token from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/driverslicense/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
