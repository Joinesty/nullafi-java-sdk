package com.joinesty.domains.staticVault.managers.vehicleRegistration;

import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * VehicleRegistrationManager Class
 */
public class VehicleRegistrationManager {

    private StaticVault vault;

    private VehicleRegistrationManager() {

    }

    /**
     * Creates an instance of VehicleRegistrationManager.
     *
     * @param vault
     */
    public VehicleRegistrationManager(StaticVault vault) {
        this.vault = vault;
    }

    /**
     * Create a new VehicleRegistration string to be tokenized for a specific static vault
     *
     * @param vehicleRegistration
     * @param tags
     *
     * @return VehicleRegistrationResponse
     */
    public VehicleRegistrationResponse create(String vehicleRegistration, String[] tags) {
        try {
            AESDTO cipher = this.vault.encrypt(vehicleRegistration);

            VehicleRegistrationRequest request = new VehicleRegistrationRequest();
            request.setVehicleRegistration(cipher.getEncryptedData());
            request.setAuthTag(cipher.getAuthenticationTag());
            request.setIv(cipher.getInitializationVector());
            request.setTags(tags);

            HttpResponse<VehicleRegistrationResponse> response = Unirest
                    .post(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/vehicleregistration"))
                    .body(request)
                    .asObject(VehicleRegistrationResponse.class);

            VehicleRegistrationResponse responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the VehicleRegistration string token from a static vault
     *
     * @param id
     *
     * @return VehicleRegistrationResponse
     */
    public VehicleRegistrationResponse retrieve(String id) {
        try {
            HttpResponse<VehicleRegistrationResponse> response = Unirest
                    .get(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/vehicleregistration/" + id))
                    .asObject(VehicleRegistrationResponse.class);

            VehicleRegistrationResponse responseBody = response.getBody();
            String decrypted = this.vault.decrypt(responseBody.getIv(), responseBody.getAuthTag(), responseBody.getVehicleRegistration());
            responseBody.setVehicleRegistration(decrypted);

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the VehicleRegistration token from static vault
     *
     * @param id
     *
     * @return boolean
     */
    public boolean delete(String id) {
        try {
            HttpResponse<String> response = Unirest
                    .delete(this.vault.getClient().getApi().setUrl("/vault/static/" + this.vault.getId() + "/vehicleregistration/" + id))
                    .asString();

            return response.getBody().contains("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
