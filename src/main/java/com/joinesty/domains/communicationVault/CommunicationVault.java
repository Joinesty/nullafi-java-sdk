package com.joinesty.domains.communicationVault;

import com.joinesty.Client;
import com.joinesty.domains.communicationVault.managers.email.EmailManager;
import com.joinesty.services.API;
import com.joinesty.services.Security;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

/**
 * CommunicationVault Class
 */
public class CommunicationVault extends API {

  private static KeyPair rsaKey = new Security().getRsa().generateKey();

  private String id;

  private String name;

  private List<String> tags;

  private Client client;

  private EmailManager emailManager;


  private SecretKey secretKey;

  private Security security;

  private CommunicationVault(Client client, String id, String name, List<String> tags, SecretKey secretKey) {
    this.client = client;
    this.security = new Security();
    this.id = id;
    this.name = name;
    this.tags = tags;
    this.secretKey = secretKey;

    this.emailManager = new EmailManager(this);
  }

  /**
   * Request the API to create a new communication vault
   *
   * @param client Client
   * @param name Name
   * @param tags Tags
   * @return CommunicationVault
   * @throws Exception Exception
   */
  public static CommunicationVault createCommunicationVault(Client client, String name, List<String> tags) throws Exception {
    final Security security = new Security();
    final String publicKey = security.getRsa().toPEM(rsaKey.getPublic().getEncoded());

    CommunicationVaultRequest requestBody = new CommunicationVaultRequest();
    requestBody.setName(name);
    requestBody.setPublicKey(publicKey);
    requestBody.setTags(tags);

    HttpResponse<CommunicationVaultResponse> response = Unirest
      .post(client.getApi().setUrl("/vault/communication"))
      .body(requestBody)
      .asObject(CommunicationVaultResponse.class);

    CommunicationVaultResponse responseBody = response.getBody();

    byte[] byteIv = Base64.decode(responseBody.getIv());
    byte[] byteAuthTag = Base64.decode(responseBody.getAuthTag());
    byte[] byteSessionKey = Base64.decode(responseBody.getSessionKey());
    byte[] byteMasterKey = Base64.decode(responseBody.getMasterKey());

    String encryptedMasterKey = new String(security.getRsa().decrypt(rsaKey.getPrivate(), byteSessionKey));
    encryptedMasterKey = encryptedMasterKey.replaceAll("\"", "");
    byte[] aesEncryptedMasterKey = Base64.decode(encryptedMasterKey);
    SecretKey skAesEncryptedMasterKey = new SecretKeySpec(aesEncryptedMasterKey, 0, aesEncryptedMasterKey.length, "AES");

    byte[] decryptedMasterKey = security.getAes().decrypt(skAesEncryptedMasterKey, byteIv, byteAuthTag, byteMasterKey);
    byte[] base64MasterKey = Base64.decode(decryptedMasterKey);
    SecretKey skMasterKey = new SecretKeySpec(base64MasterKey, 0, base64MasterKey.length, "AES");

    return new CommunicationVault(
      client,
      responseBody.getId(),
      responseBody.getName(),
      responseBody.getTags(),
      skMasterKey
    );
  }

  /**
   * Retrieve the communication vault from id
   *
   * @param client Client
   * @param vaultId VaultId Vault ID
   * @param masterKey Master Key
   * @return CommunicationVault
   * @throws Exception Exception
   */
  public static CommunicationVault retrieveCommunicationVault(Client client, String vaultId, String masterKey) throws Exception {
    HttpResponse<CommunicationVaultResponse> response = Unirest
      .get(client.getApi().setUrl("/vault/communication/" + vaultId))
      .asObject(CommunicationVaultResponse.class);

    CommunicationVaultResponse responseBody = response.getBody();
    byte[] decodedKey = masterKey.getBytes();
    byte[] base64MasterKey = Base64.decode(decodedKey);
    SecretKey secretKey = new SecretKeySpec(base64MasterKey, 0, base64MasterKey.length, "AES");

    return new CommunicationVault(
      client,
      responseBody.getId(),
      responseBody.getName(),
      responseBody.getTags(),
      secretKey
    );
  }

  /**
   * Delete the communication vault from id
   *
   * @param client Client
   * @param vaultId VaultId Vault ID
   * @return boolean
   * @throws Exception Exception
   */
  public static boolean delete(Client client, String vaultId) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(client.getApi().setUrl("/vault/communication/" + vaultId))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }

  public String getMasterKey() {
    return new String(Base64.encode(this.secretKey.getEncoded()));
  }

  public AESDTO encrypt(String value) {
    byte[] iv = this.security.getAes().generateIV();
    byte[] byteValue = value.getBytes();

    return this.security.getAes().encrypt(this.secretKey, iv, byteValue);
  }

  public String decrypt(String iv, String authTag, String value) {
    byte[] byteIv = Base64.decode(iv.getBytes());
    byte[] byteAuthTag = Base64.decode(authTag.getBytes());
    byte[] byteValue = Base64.decode(value.getBytes());

    byte[] decrypted = this.security.getAes().decrypt(this.secretKey, byteIv, byteAuthTag, byteValue);
    return new String(decrypted);
  }

  public String hash(String value) {
    return this.security.getHmac().hash(value, this.client.getHashKey());
  }

  public static KeyPair getRsaKey() {
    return rsaKey;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<String> getTags() {
    return tags;
  }

  public Client getClient() {
    return client;
  }

  public EmailManager getEmailManager() {
    return emailManager;
  }
}
