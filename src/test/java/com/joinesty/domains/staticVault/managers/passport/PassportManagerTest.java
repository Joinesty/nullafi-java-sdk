package com.joinesty.domains.staticVault.managers.passport;

import com.joinesty.BaseMock;
import com.joinesty.Client;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.crypto.AESDTO;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PassportManagerTest extends BaseMock {

  private StaticVault staticVault;

  @Override
  public void setup() throws Exception {
    super.setup();

    Client client = this.createClient();
    this.staticVault = client.createStaticVault("some-static-vault");

    this.mockPost(this.staticVault);
    this.mockGet(this.staticVault);
    this.mockGetWithRealData(this.staticVault);
    this.mockDelete();
  }

  private void mockPost(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-passport-tag-1");
    tags.add("some-passport-tag-2");

    AESDTO cipher = staticVault.encrypt("some-passport");

    PassportResponse passportResponse = new PassportResponse();
    passportResponse.setId("some-passport-id");
    passportResponse.setPassportAlias("some-passport-alias");
    passportResponse.setPassport(cipher.getEncryptedData());
    passportResponse.setAuthTag(cipher.getAuthenticationTag());
    passportResponse.setIv(cipher.getInitializationVector());
    passportResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/passport")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(passportResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-passport-tag-1");
    tags.add("some-passport-tag-2");

    AESDTO cipher = staticVault.encrypt("some-passport");

    PassportResponse passportResponse = new PassportResponse();
    passportResponse.setId("some-passport-id");
    passportResponse.setPassportAlias("some-passport-alias");
    passportResponse.setPassport(cipher.getEncryptedData());
    passportResponse.setAuthTag(cipher.getAuthenticationTag());
    passportResponse.setIv(cipher.getInitializationVector());
    passportResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/passport/some-passport-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(passportResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {
    AESDTO cipher = staticVault.encrypt("some-passport");

    PassportResponse passportResponse = new PassportResponse();
    passportResponse.setId("some-passport-id");
    passportResponse.setPassportAlias("some-passport-alias");
    passportResponse.setPassport(cipher.getEncryptedData());
    passportResponse.setAuthTag(cipher.getAuthenticationTag());
    passportResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/passport")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + passportResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/passport/some-passport-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateAPassportAliasWithTags_WhenCreatingAlias_ShouldReturnAPassportAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-passport-tag-1");
    tags.add("some-passport-tag-2");

    PassportResponse passportResponse = this.staticVault.getPassportManager()
      .create("some-passport", tags);

    assertEquals(passportResponse.getId(), "some-passport-id");
    assertEquals(passportResponse.getPassportAlias(), "some-passport-alias");
    assertEquals(passportResponse.getTags().get(0), "some-passport-tag-1");
    assertEquals(passportResponse.getTags().get(1), "some-passport-tag-2");
    assertNotNull(passportResponse.getPassport());
    assertNotNull(passportResponse.getAuthTag());
    assertNotNull(passportResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateAPassportAlias_WhenCreatingAlias_ShouldReturnAPassportAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-passport-tag-1");
    tags.add("some-passport-tag-2");

    PassportResponse passportResponse = this.staticVault.getPassportManager()
      .create("some-passport", tags);

    assertEquals(passportResponse.getId(), "some-passport-id");
    assertEquals(passportResponse.getPassportAlias(), "some-passport-alias");
    assertNotNull(passportResponse.getPassport());
    assertNotNull(passportResponse.getAuthTag());
    assertNotNull(passportResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAPassportAlias_WhenRetrievingAlias_ShouldReturnAPassportAlias() throws Exception {
    PassportResponse passportResponse = this.staticVault.getPassportManager()
      .retrieve("some-passport-id");

    assertEquals(passportResponse.getId(), "some-passport-id");
    assertEquals(passportResponse.getPassportAlias(), "some-passport-alias");
    assertEquals(passportResponse.getTags().get(0), "some-passport-tag-1");
    assertEquals(passportResponse.getTags().get(1), "some-passport-tag-2");
    assertNotNull(passportResponse.getPassport());
    assertNotNull(passportResponse.getAuthTag());
    assertNotNull(passportResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAPassportAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnAPassportAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-passport-tag-1");
    tags.add("some-passport-tag-2");

    PassportResponse[] passportResponses = this.staticVault.getPassportManager()
      .retrieveFromRealData("some-passport", tags);

    assertEquals(passportResponses.length, 1);
    assertEquals(passportResponses[0].getId(), "some-passport-id");
    assertEquals(passportResponses[0].getPassportAlias(), "some-passport-alias");
    assertNotNull(passportResponses[0].getPassport());
    assertNotNull(passportResponses[0].getAuthTag());
    assertNotNull(passportResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveAPassportAliasFromRealData_WhenRetrievingAlias_ShouldReturnAPassportAlias() throws Exception {
    PassportResponse[] passportResponses = this.staticVault.getPassportManager()
      .retrieveFromRealData("some-passport");

    assertEquals(passportResponses.length, 1);
    assertEquals(passportResponses[0].getId(), "some-passport-id");
    assertEquals(passportResponses[0].getPassportAlias(), "some-passport-alias");
    assertNotNull(passportResponses[0].getPassport());
    assertNotNull(passportResponses[0].getAuthTag());
    assertNotNull(passportResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteAPassportAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    PassportManager passportManager = this.staticVault.getPassportManager();
    PassportResponse passportResponse = passportManager.create("some-passport");
    boolean success = passportManager.delete(passportResponse.getId());

    assertEquals(success, true);
  }

}