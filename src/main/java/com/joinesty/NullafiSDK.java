package com.joinesty;

/**
 * NullafiSDK Class
 */
public class NullafiSDK {

    private String apiKey;

    private Client client;

    /**
     * Private constructor without params
     */
    private NullafiSDK() {

    }

    /**
     * Creates an instance of NullafiSDK.
     *
     * @param apiKey
     */
    public NullafiSDK(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     *  Create a new instance of Client
     *
     * @return Client
     */
    public Client createClient() {
        this.client = new Client();
        this.client.authenticate(this.apiKey);
        return this.client;
    }

    public static void main (String[] args) {
        System.out.println("com.joinesty.NullafiSDK");
    }

}
