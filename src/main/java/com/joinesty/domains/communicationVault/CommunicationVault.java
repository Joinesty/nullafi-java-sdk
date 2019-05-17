package com.joinesty.domains.communicationVault;

import com.joinesty.Client;
import com.joinesty.domains.communicationVault.managers.email.EmailManager;
import com.joinesty.services.API;
import com.joinesty.services.Security;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.Getter;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

/**
 * CommunicationVault Class
 */
public class CommunicationVault extends API {

    @Getter
    private String id;

    @Getter
    private String name;

    @Getter
    private Client client;


    @Getter
    private EmailManager emailManager;


    private SecretKey secretKey;

    private Security security;


    private CommunicationVault() {

    }

    private CommunicationVault(Client client, String id, String name, SecretKey secretKey) {
        this.client = client;
        this.security = new Security();
        this.id = id;
        this.name = name;
        this.secretKey = secretKey;

        this.emailManager = new EmailManager(this);
    }

    public String getMasterKey() {
        return new String(Base64.encode(this.secretKey.getEncoded()));
    }

    public AESDTO encrypt(String value) {
        byte[] iv = this.security.getAes().generateIV();
        byte[] byteValue = value.getBytes(StandardCharsets.UTF_8);

        return this.security.getAes().encrypt(this.secretKey, iv, byteValue);
    }

    public String decrypt(String iv, String authTag, String value) {
        byte[] byteIv = Base64.decode(iv.getBytes());
        byte[] byteAuthTag = Base64.decode(authTag.getBytes());
        byte[] byteValue = Base64.decode(value.getBytes());

        byte[] decrypted = this.security.getAes().decrypt(this.secretKey, byteIv, byteAuthTag, byteValue);
        return new String(decrypted);
    }

    /**
     *  Request the API to create a new communication vault
     *
     * @param client
     * @param name
     * @param tags
     *
     * @return CommunicationVault
     */
    public static CommunicationVault createCommunicationVault(Client client, String name, String[] tags) {
        try {
            final Security security = new Security();
            final KeyPair rsaKey = security.getRsa().rsaGenerateEphemeralKeypairs();

            final String publicKey = new String(Base64.encode(rsaKey.getPublic().getEncoded()));

            CommunicationVaultRequest requestBody = new CommunicationVaultRequest();
            requestBody.setName(name);
            requestBody.setPublicKey(publicKey);
            requestBody.setTags(tags);

            HttpResponse<CommunicationVaultResponse> response = Unirest
                    .post(client.getApi().setUrl("/vault/communication"))
                    .body(requestBody)
                    .asObject(CommunicationVaultResponse.class);

            CommunicationVaultResponse responseBody = response.getBody();

            byte[] byteIv = Base64.decode(responseBody.getIv().getBytes());
            byte[] byteAuthTag = Base64.decode(responseBody.getAuthTag().getBytes());
            byte[] byteSessionKey = Base64.decode(responseBody.getSessionKey().getBytes());
            byte[] byteMasterKey = Base64.decode(responseBody.getMasterKey().getBytes());

            byte[] aesEncryptedMasterKey = security.getRsa().privateDecrypt(rsaKey.getPrivate(), byteSessionKey);
            SecretKey secretKey = new SecretKeySpec(aesEncryptedMasterKey, 0, aesEncryptedMasterKey.length, "AES");

            byte[] decrypted = security.getAes().decrypt(secretKey, byteIv, byteAuthTag, byteMasterKey);
            SecretKey masterKey = new SecretKeySpec(decrypted, 0, decrypted.length, "AES");

            return new CommunicationVault(client, responseBody.getId(), responseBody.getName(), masterKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the communication vault from id
     *
     * @param client
     * @param vaultId
     * @param masterKey
     *
     * @return CommunicationVault
     */
    public static CommunicationVault retrieveCommunicationVault(Client client, String vaultId, String masterKey) {
        try {
            HttpResponse<CommunicationVaultResponse> response = Unirest
                    .get(client.getApi().setUrl("/vault/communication/" + vaultId))
                    .asObject(CommunicationVaultResponse.class);

            CommunicationVaultResponse responseBody = response.getBody();
            byte[] decodedKey = Base64.decode(masterKey);
            SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            return new CommunicationVault(
                    client,
                    responseBody.getId(),
                    responseBody.getName(),
                    secretKey
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
