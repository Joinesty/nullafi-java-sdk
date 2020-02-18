package com.joinesty.unit;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.authenticate.AuthenticateResponse;
import com.joinesty.domains.communicationVault.CommunicationVault;
import com.joinesty.domains.communicationVault.CommunicationVaultResponse;
import com.joinesty.domains.staticVault.StaticVaultResponse;
import com.joinesty.services.Security;
import com.joinesty.services.crypto.AESDTO;
import org.apache.http.HttpStatus;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.Rule;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class BaseMock {

  @Rule
  public WireMockRule wireMockRule = new WireMockRule();
  protected String API_KEY = "some-api-key";

  @Before
  public void setup() throws Exception {

    // AUTHENTICATION

    this.mock_authentication_token();

    // STATIC VAULT 

    this.mock_post_vault_static();
    this.mock_get_vault_static();
    this.mock_delete_vault_static();

    // COMMUNICATION VAULT

    this.mock_post_vault_communication();
    this.mock_get_vault_communication();
    this.mock_delete_vault_communication();
  }

  protected Client createClient() throws Exception {
    NullafiSDK nullafiSDK = new NullafiSDK(this.API_KEY);
    return nullafiSDK.createClient();
  }

  // AUTHENTICATION

  private void mock_authentication_token() {
    AuthenticateResponse authenticateResponse = new AuthenticateResponse();
    authenticateResponse.setToken("some-token");
    authenticateResponse.setTenantId("some-tenantId");
    authenticateResponse.setHashKey("some-hashKey");

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/authentication/token")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(authenticateResponse.toJson())
      )
    );
  }

  // STATIC VAULT

  private void mock_post_vault_static() {
    List<String> tags = new ArrayList<>();
    tags.add("some-vault-tag-1");
    tags.add("some-vault-tag-2");

    StaticVaultResponse staticVaultResponse = new StaticVaultResponse();
    staticVaultResponse.setId("some-vault-id");
    staticVaultResponse.setName("some-vault-name");
    staticVaultResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(staticVaultResponse.toJson())
      )
    );
  }

  private void mock_get_vault_static() {
    List<String> tags = new ArrayList<>();
    tags.add("some-vault-tag-1");
    tags.add("some-vault-tag-2");

    StaticVaultResponse staticVaultResponse = new StaticVaultResponse();
    staticVaultResponse.setId("some-vault-id");
    staticVaultResponse.setName("some-vault-name");
    staticVaultResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(staticVaultResponse.toJson())
      )
    );
  }

  private void mock_delete_vault_static() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  // COMMUNICATION VAULT

  private void mock_post_vault_communication() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-vault-tag-1");
    tags.add("some-vault-tag-2");

    CommunicationVaultResponse communicationVaultResponse = new CommunicationVaultResponse();
    communicationVaultResponse.setId("some-vault-id");
    communicationVaultResponse.setName("some-vault-name");
    communicationVaultResponse.setTags(tags);

    Security security = new Security();
    SecretKey vaultMasterKey = security.getAes().generateMasterKey();
    byte[] byteVaultMasterKey = Base64.encode(vaultMasterKey.getEncoded());

    SecretKey secLevelMasterKey = security.getAes().generateMasterKey();
    byte[] secLevelIv = security.getAes().generateIV();
    AESDTO encryptedMasterKey = security.getAes().encrypt(secLevelMasterKey, secLevelIv, byteVaultMasterKey);

    communicationVaultResponse.setIv(encryptedMasterKey.getInitializationVector());
    communicationVaultResponse.setAuthTag(encryptedMasterKey.getAuthenticationTag());
    communicationVaultResponse.setMasterKey(encryptedMasterKey.getEncryptedData());

    Key publicKey = CommunicationVault.getRsaKey().getPublic();
    byte[] byteSessionKey = security.getRsa().encrypt(publicKey, Base64.encode(secLevelMasterKey.getEncoded()));
    communicationVaultResponse.setSessionKey(Base64.toBase64String(byteSessionKey));

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/communication")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(communicationVaultResponse.toJson())
      )
    );
  }

  private void mock_get_vault_communication() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-vault-tag-1");
    tags.add("some-vault-tag-2");

    CommunicationVaultResponse communicationVaultResponse = new CommunicationVaultResponse();
    communicationVaultResponse.setId("some-vault-id");
    communicationVaultResponse.setName("some-vault-name");
    communicationVaultResponse.setTags(tags);

    Security security = new Security();
    SecretKey vaultMasterKey = security.getAes().generateMasterKey();
    byte[] byteVaultMasterKey = Base64.encode(vaultMasterKey.getEncoded());

    SecretKey secLevelMasterKey = security.getAes().generateMasterKey();
    byte[] secLevelIv = security.getAes().generateIV();
    AESDTO encryptedMasterKey = security.getAes().encrypt(secLevelMasterKey, secLevelIv, byteVaultMasterKey);

    communicationVaultResponse.setIv(encryptedMasterKey.getInitializationVector());
    communicationVaultResponse.setAuthTag(encryptedMasterKey.getAuthenticationTag());
    communicationVaultResponse.setMasterKey(encryptedMasterKey.getEncryptedData());

    Key publicKey = CommunicationVault.getRsaKey().getPublic();
    byte[] byteSessionKey = security.getRsa().encrypt(publicKey, Base64.encode(secLevelMasterKey.getEncoded()));
    communicationVaultResponse.setSessionKey(Base64.toBase64String(byteSessionKey));

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/communication/some-vault-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(communicationVaultResponse.toJson())
      )
    );
  }

  private void mock_delete_vault_communication() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/communication/some-vault-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }
}
