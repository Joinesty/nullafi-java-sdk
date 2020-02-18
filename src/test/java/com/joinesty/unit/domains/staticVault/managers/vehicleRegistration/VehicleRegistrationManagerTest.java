package com.joinesty.unit.domains.staticVault.managers.vehicleRegistration;

import com.joinesty.domains.staticVault.managers.vehicleRegistration.VehicleRegistrationManager;
import com.joinesty.domains.staticVault.managers.vehicleRegistration.VehicleRegistrationResponse;
import com.joinesty.unit.BaseMock;
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

public class VehicleRegistrationManagerTest extends BaseMock {

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
    tags.add("some-vehicleregistration-tag-1");
    tags.add("some-vehicleregistration-tag-2");

    AESDTO cipher = staticVault.encrypt("some-vehicleregistration");

    VehicleRegistrationResponse vehicleRegistrationResponse = new VehicleRegistrationResponse();
    vehicleRegistrationResponse.setId("some-vehicleregistration-id");
    vehicleRegistrationResponse.setVehicleregistrationAlias("some-vehicleregistration-alias");
    vehicleRegistrationResponse.setVehicleregistration(cipher.getEncryptedData());
    vehicleRegistrationResponse.setAuthTag(cipher.getAuthenticationTag());
    vehicleRegistrationResponse.setIv(cipher.getInitializationVector());
    vehicleRegistrationResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/vehicleregistration")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(vehicleRegistrationResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-vehicleregistration-tag-1");
    tags.add("some-vehicleregistration-tag-2");

    AESDTO cipher = staticVault.encrypt("some-vehicleregistration");

    VehicleRegistrationResponse vehicleRegistrationResponse = new VehicleRegistrationResponse();
    vehicleRegistrationResponse.setId("some-vehicleregistration-id");
    vehicleRegistrationResponse.setVehicleregistrationAlias("some-vehicleregistration-alias");
    vehicleRegistrationResponse.setVehicleregistration(cipher.getEncryptedData());
    vehicleRegistrationResponse.setAuthTag(cipher.getAuthenticationTag());
    vehicleRegistrationResponse.setIv(cipher.getInitializationVector());
    vehicleRegistrationResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/vehicleregistration/some-vehicleregistration-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(vehicleRegistrationResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {
    AESDTO cipher = staticVault.encrypt("some-vehicleregistration");

    VehicleRegistrationResponse vehicleRegistrationResponse = new VehicleRegistrationResponse();
    vehicleRegistrationResponse.setId("some-vehicleregistration-id");
    vehicleRegistrationResponse.setVehicleregistrationAlias("some-vehicleregistration-alias");
    vehicleRegistrationResponse.setVehicleregistration(cipher.getEncryptedData());
    vehicleRegistrationResponse.setAuthTag(cipher.getAuthenticationTag());
    vehicleRegistrationResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/vehicleregistration")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + vehicleRegistrationResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/vehicleregistration/some-vehicleregistration-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateAVehicleRegistrationAliasWithTags_WhenCreatingAlias_ShouldReturnAVehicleRegistrationAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-vehicleregistration-tag-1");
    tags.add("some-vehicleregistration-tag-2");

    VehicleRegistrationResponse vehicleRegistrationResponse = this.staticVault.getVehicleRegistrationManager()
      .create("some-vehicleregistration", tags);

    assertEquals(vehicleRegistrationResponse.getId(), "some-vehicleregistration-id");
    assertEquals(vehicleRegistrationResponse.getVehicleregistrationAlias(), "some-vehicleregistration-alias");
    assertEquals(vehicleRegistrationResponse.getTags().get(0), "some-vehicleregistration-tag-1");
    assertEquals(vehicleRegistrationResponse.getTags().get(1), "some-vehicleregistration-tag-2");
    assertNotNull(vehicleRegistrationResponse.getVehicleregistration());
    assertNotNull(vehicleRegistrationResponse.getAuthTag());
    assertNotNull(vehicleRegistrationResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateAVehicleRegistrationAlias_WhenCreatingAlias_ShouldReturnAVehicleRegistrationAlias() throws Exception {
    VehicleRegistrationResponse vehicleRegistrationResponse = this.staticVault.getVehicleRegistrationManager()
      .create("some-vehicleregistration");

    assertEquals(vehicleRegistrationResponse.getId(), "some-vehicleregistration-id");
    assertEquals(vehicleRegistrationResponse.getVehicleregistrationAlias(), "some-vehicleregistration-alias");
    assertNotNull(vehicleRegistrationResponse.getVehicleregistration());
    assertNotNull(vehicleRegistrationResponse.getAuthTag());
    assertNotNull(vehicleRegistrationResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAVehicleRegistrationAlias_WhenRetrievingAlias_ShouldReturnAVehicleRegistrationAlias() throws Exception {
    VehicleRegistrationResponse vehicleRegistrationResponse = this.staticVault.getVehicleRegistrationManager()
      .retrieve("some-vehicleregistration-id");

    assertEquals(vehicleRegistrationResponse.getId(), "some-vehicleregistration-id");
    assertEquals(vehicleRegistrationResponse.getVehicleregistrationAlias(), "some-vehicleregistration-alias");
    assertEquals(vehicleRegistrationResponse.getTags().get(0), "some-vehicleregistration-tag-1");
    assertEquals(vehicleRegistrationResponse.getTags().get(1), "some-vehicleregistration-tag-2");
    assertNotNull(vehicleRegistrationResponse.getVehicleregistration());
    assertNotNull(vehicleRegistrationResponse.getAuthTag());
    assertNotNull(vehicleRegistrationResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAVehicleRegistrationAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnAVehicleRegistrationAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-vehicleregistration-tag-1");
    tags.add("some-vehicleregistration-tag-2");

    VehicleRegistrationResponse[] vehicleRegistrationResponses = this.staticVault.getVehicleRegistrationManager()
      .retrieveFromRealData("some-vehicleregistration", tags);

    assertEquals(vehicleRegistrationResponses.length, 1);
    assertEquals(vehicleRegistrationResponses[0].getId(), "some-vehicleregistration-id");
    assertEquals(vehicleRegistrationResponses[0].getVehicleregistrationAlias(), "some-vehicleregistration-alias");
    assertNotNull(vehicleRegistrationResponses[0].getVehicleregistration());
    assertNotNull(vehicleRegistrationResponses[0].getAuthTag());
    assertNotNull(vehicleRegistrationResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveAVehicleRegistrationAliasFromRealData_WhenRetrievingAlias_ShouldReturnAVehicleRegistrationAlias() throws Exception {
    VehicleRegistrationResponse[] vehicleRegistrationResponses = this.staticVault.getVehicleRegistrationManager()
      .retrieveFromRealData("some-vehicleregistration");

    assertEquals(vehicleRegistrationResponses.length, 1);
    assertEquals(vehicleRegistrationResponses[0].getId(), "some-vehicleregistration-id");
    assertEquals(vehicleRegistrationResponses[0].getVehicleregistrationAlias(), "some-vehicleregistration-alias");
    assertNotNull(vehicleRegistrationResponses[0].getVehicleregistration());
    assertNotNull(vehicleRegistrationResponses[0].getAuthTag());
    assertNotNull(vehicleRegistrationResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteAVehicleRegistrationAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    VehicleRegistrationManager vehicleregistrationManager = this.staticVault.getVehicleRegistrationManager();
    VehicleRegistrationResponse vehicleRegistrationResponse = vehicleregistrationManager.create("some-vehicleregistration");
    boolean success = vehicleregistrationManager.delete(vehicleRegistrationResponse.getId());

    assertEquals(success, true);
  }

}