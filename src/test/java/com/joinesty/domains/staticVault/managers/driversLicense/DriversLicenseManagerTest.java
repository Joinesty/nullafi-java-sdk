package com.joinesty.domains.staticVault.managers.driversLicense;

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

public class DriversLicenseManagerTest extends BaseMock {

  private StaticVault staticVault;

  @Override
  public void setup() throws Exception {
    super.setup();

    Client client = this.createClient();
    this.staticVault = client.createStaticVault("some-static-vault");

    this.mockPost(this.staticVault);
    this.mockPostWithState(this.staticVault);
    this.mockGet(this.staticVault);
    this.mockGetWithRealData(this.staticVault);
    this.mockDelete();
  }

  private void mockPost(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-driverslicense-tag-1");
    tags.add("some-driverslicense-tag-2");

    AESDTO cipher = staticVault.encrypt("some-driverslicense");

    DriversLicenseResponse driversLicenseResponse = new DriversLicenseResponse();
    driversLicenseResponse.setId("some-driverslicense-id");
    driversLicenseResponse.setDriverslicenseAlias("some-driverslicense-alias");
    driversLicenseResponse.setDriverslicense(cipher.getEncryptedData());
    driversLicenseResponse.setAuthTag(cipher.getAuthenticationTag());
    driversLicenseResponse.setIv(cipher.getInitializationVector());
    driversLicenseResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/driverslicense")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(driversLicenseResponse.toJson())
      )
    );
  }

  private void mockPostWithState(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-driverslicense-tag-1");
    tags.add("some-driverslicense-tag-2");

    AESDTO cipher = staticVault.encrypt("some-driverslicense");

    DriversLicenseResponse driversLicenseResponse = new DriversLicenseResponse();
    driversLicenseResponse.setId("some-driverslicense-id");
    driversLicenseResponse.setDriverslicenseAlias("some-driverslicense-alias");
    driversLicenseResponse.setDriverslicense(cipher.getEncryptedData());
    driversLicenseResponse.setAuthTag(cipher.getAuthenticationTag());
    driversLicenseResponse.setIv(cipher.getInitializationVector());
    driversLicenseResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/driverslicense/some-state")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(driversLicenseResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-driverslicense-tag-1");
    tags.add("some-driverslicense-tag-2");

    AESDTO cipher = staticVault.encrypt("some-driverslicense");

    DriversLicenseResponse driversLicenseResponse = new DriversLicenseResponse();
    driversLicenseResponse.setId("some-driverslicense-id");
    driversLicenseResponse.setDriverslicenseAlias("some-driverslicense-alias");
    driversLicenseResponse.setDriverslicense(cipher.getEncryptedData());
    driversLicenseResponse.setAuthTag(cipher.getAuthenticationTag());
    driversLicenseResponse.setIv(cipher.getInitializationVector());
    driversLicenseResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/driverslicense/some-driverslicense-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(driversLicenseResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {
    AESDTO cipher = staticVault.encrypt("some-driverslicense");

    DriversLicenseResponse driversLicenseResponse = new DriversLicenseResponse();
    driversLicenseResponse.setId("some-driverslicense-id");
    driversLicenseResponse.setDriverslicenseAlias("some-driverslicense-alias");
    driversLicenseResponse.setDriverslicense(cipher.getEncryptedData());
    driversLicenseResponse.setAuthTag(cipher.getAuthenticationTag());
    driversLicenseResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/driverslicense")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + driversLicenseResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/driverslicense/some-driverslicense-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateADriversLicenseAliasWithTags_WhenCreatingAlias_ShouldReturnADriversLicenseAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-driverslicense-tag-1");
    tags.add("some-driverslicense-tag-2");

    DriversLicenseResponse driversLicenseResponse = this.staticVault.getDriversLicenseManager()
      .create("some-driverslicense", tags);

    assertEquals(driversLicenseResponse.getId(), "some-driverslicense-id");
    assertEquals(driversLicenseResponse.getDriverslicenseAlias(), "some-driverslicense-alias");
    assertEquals(driversLicenseResponse.getTags().get(0), "some-driverslicense-tag-1");
    assertEquals(driversLicenseResponse.getTags().get(1), "some-driverslicense-tag-2");
    assertNotNull(driversLicenseResponse.getDriverslicense());
    assertNotNull(driversLicenseResponse.getAuthTag());
    assertNotNull(driversLicenseResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateADriversLicenseAliasWithStateAndTags_WhenCreatingAlias_ShouldReturnADriversLicenseAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-driverslicense-tag-1");
    tags.add("some-driverslicense-tag-2");

    DriversLicenseResponse driversLicenseResponse = this.staticVault.getDriversLicenseManager()
      .create("some-driverslicense", "some-state", tags);

    assertEquals(driversLicenseResponse.getId(), "some-driverslicense-id");
    assertEquals(driversLicenseResponse.getDriverslicenseAlias(), "some-driverslicense-alias");
    assertEquals(driversLicenseResponse.getTags().get(0), "some-driverslicense-tag-1");
    assertEquals(driversLicenseResponse.getTags().get(1), "some-driverslicense-tag-2");
    assertNotNull(driversLicenseResponse.getDriverslicense());
    assertNotNull(driversLicenseResponse.getAuthTag());
    assertNotNull(driversLicenseResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateADriversLicenseAlias_WhenCreatingAlias_ShouldReturnADriversLicenseAlias() throws Exception {
    DriversLicenseResponse driversLicenseResponse = this.staticVault.getDriversLicenseManager()
      .create("some-driverslicense");

    assertEquals(driversLicenseResponse.getId(), "some-driverslicense-id");
    assertEquals(driversLicenseResponse.getDriverslicenseAlias(), "some-driverslicense-alias");
    assertNotNull(driversLicenseResponse.getDriverslicense());
    assertNotNull(driversLicenseResponse.getAuthTag());
    assertNotNull(driversLicenseResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateADriversLicenseAliasWithState_WhenCreatingAlias_ShouldReturnADriversLicenseAlias() throws Exception {
    DriversLicenseResponse driversLicenseResponse = this.staticVault.getDriversLicenseManager()
      .create("some-driverslicense", "some-state");

    assertEquals(driversLicenseResponse.getId(), "some-driverslicense-id");
    assertEquals(driversLicenseResponse.getDriverslicenseAlias(), "some-driverslicense-alias");
    assertNotNull(driversLicenseResponse.getDriverslicense());
    assertNotNull(driversLicenseResponse.getAuthTag());
    assertNotNull(driversLicenseResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveADriversLicenseAlias_WhenRetrievingAlias_ShouldReturnADriversLicenseAlias() throws Exception {
    DriversLicenseResponse driversLicenseResponse = this.staticVault.getDriversLicenseManager()
      .retrieve("some-driverslicense-id");

    assertEquals(driversLicenseResponse.getId(), "some-driverslicense-id");
    assertEquals(driversLicenseResponse.getDriverslicenseAlias(), "some-driverslicense-alias");
    assertEquals(driversLicenseResponse.getTags().get(0), "some-driverslicense-tag-1");
    assertEquals(driversLicenseResponse.getTags().get(1), "some-driverslicense-tag-2");
    assertNotNull(driversLicenseResponse.getDriverslicense());
    assertNotNull(driversLicenseResponse.getAuthTag());
    assertNotNull(driversLicenseResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveADriversLicenseAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnADriversLicenseAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-driverslicense-tag-1");
    tags.add("some-driverslicense-tag-2");

    DriversLicenseResponse[] driversLicenseResponses = this.staticVault.getDriversLicenseManager()
      .retrieveFromRealData("some-driverslicense", tags);

    assertEquals(driversLicenseResponses.length, 1);
    assertEquals(driversLicenseResponses[0].getId(), "some-driverslicense-id");
    assertEquals(driversLicenseResponses[0].getDriverslicenseAlias(), "some-driverslicense-alias");
    assertNotNull(driversLicenseResponses[0].getDriverslicense());
    assertNotNull(driversLicenseResponses[0].getAuthTag());
    assertNotNull(driversLicenseResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveADriversLicenseAliasFromRealData_WhenRetrievingAlias_ShouldReturnADriversLicenseAlias() throws Exception {
    DriversLicenseResponse[] driversLicenseResponses = this.staticVault.getDriversLicenseManager()
      .retrieveFromRealData("some-driverslicense");

    assertEquals(driversLicenseResponses.length, 1);
    assertEquals(driversLicenseResponses[0].getId(), "some-driverslicense-id");
    assertEquals(driversLicenseResponses[0].getDriverslicenseAlias(), "some-driverslicense-alias");
    assertNotNull(driversLicenseResponses[0].getDriverslicense());
    assertNotNull(driversLicenseResponses[0].getAuthTag());
    assertNotNull(driversLicenseResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteADriversLicenseAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    DriversLicenseManager driverslicenseManager = this.staticVault.getDriversLicenseManager();
    DriversLicenseResponse driversLicenseResponse = driverslicenseManager.create("some-driverslicense");
    boolean success = driverslicenseManager.delete(driversLicenseResponse.getId());

    assertEquals(success, true);
  }

}