package com.joinesty;

import com.joinesty.domains.authenticate.AuthenticateRequest;
import com.joinesty.domains.authenticate.AuthenticateResponse;
import com.joinesty.domains.communicationVault.CommunicationVault;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.Getter;

import com.joinesty.services.API;
import com.joinesty.domains.staticVault.StaticVault;

/**
 * Client Class
 */
public class Client {

    @Getter
    private API api;

    /**
     * Class constructor.
     */
    public Client() {
        this.api = new API();

        Unirest.setDefaultHeader("accept", "application/json");
        Unirest.setDefaultHeader("Content-Type", "application/json");
    }

    /**
     * Authenticate the Client API
     *
     * @param  apiKey
     */
    public void authenticate(String apiKey) {
        try {
            AuthenticateRequest request = new AuthenticateRequest();
            request.setApiKey(apiKey);

             HttpResponse<AuthenticateResponse> response = Unirest
                     .post(this.getApi().setUrl("/authentication/token"))
                     .body(request)
                     .asObject(AuthenticateResponse.class);

             AuthenticateResponse authenticateResponse = response.getBody();

            Unirest.setDefaultHeader("Authorization", authenticateResponse.getToken());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a new static vault
     *
     * @param name
     * @param tags
     *
     * @return StaticVault
     */
    public StaticVault createStaticVault(String name, String[] tags) {
        return StaticVault.createStaticVault(this, name, tags);

    }

    /**
     * Retrieve a existing static vault
     *
     * @param vaultId
     * @param masterKey
     *
     * @return StaticVault
     */
    public StaticVault retrieveStaticVault(String vaultId, String masterKey) {
        return StaticVault.retrieveStaticVault(this, vaultId, masterKey);
    }

    /**
     * Create a new communication vault
     *
     * @param name
     * @param tags
     *
     * @return CommunicationVault
     */
    public CommunicationVault createCommunicationVault(String name, String[] tags) {
        return CommunicationVault.createCommunicationVault(this, name, tags);
    }

    /**
     * Retrieve a existing communication vault
     *
     * @param vaultId
     * @param masterKey
     *
     * @return CommunicationVault
     */
    public CommunicationVault retrieveCommunicationVault(String vaultId, String masterKey) {
        return CommunicationVault.retrieveCommunicationVault(this, vaultId, masterKey);
    }

}
