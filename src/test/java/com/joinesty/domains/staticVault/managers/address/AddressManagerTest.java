package com.joinesty.domains.staticVault.managers.address;

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

public class AddressManagerTest extends BaseMock {

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
    tags.add("some-address-tag-1");
    tags.add("some-address-tag-2");

    AESDTO cipher = staticVault.encrypt("some-address");

    AddressResponse addressResponse = new AddressResponse();
    addressResponse.setId("some-address-id");
    addressResponse.setAddressAlias("some-address-alias");
    addressResponse.setAddress(cipher.getEncryptedData());
    addressResponse.setAuthTag(cipher.getAuthenticationTag());
    addressResponse.setIv(cipher.getInitializationVector());
    addressResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/address")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(addressResponse.toJson())
      )
    );
  }

  private void mockPostWithState(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-address-tag-1");
    tags.add("some-address-tag-2");

    AESDTO cipher = staticVault.encrypt("some-address");

    AddressResponse addressResponse = new AddressResponse();
    addressResponse.setId("some-address-id");
    addressResponse.setAddressAlias("some-address-alias");
    addressResponse.setAddress(cipher.getEncryptedData());
    addressResponse.setAuthTag(cipher.getAuthenticationTag());
    addressResponse.setIv(cipher.getInitializationVector());
    addressResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/address/some-state")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(addressResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-address-tag-1");
    tags.add("some-address-tag-2");

    AESDTO cipher = staticVault.encrypt("some-address");

    AddressResponse addressResponse = new AddressResponse();
    addressResponse.setId("some-address-id");
    addressResponse.setAddressAlias("some-address-alias");
    addressResponse.setAddress(cipher.getEncryptedData());
    addressResponse.setAuthTag(cipher.getAuthenticationTag());
    addressResponse.setIv(cipher.getInitializationVector());
    addressResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/address/some-address-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(addressResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {
    AESDTO cipher = staticVault.encrypt("some-address");

    AddressResponse addressResponse = new AddressResponse();
    addressResponse.setId("some-address-id");
    addressResponse.setAddressAlias("some-address-alias");
    addressResponse.setAddress(cipher.getEncryptedData());
    addressResponse.setAuthTag(cipher.getAuthenticationTag());
    addressResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/address")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + addressResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/address/some-address-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateAAddressAliasWithTags_WhenCreatingAlias_ShouldReturnAAddressAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-address-tag-1");
    tags.add("some-address-tag-2");

    AddressResponse addressResponse = this.staticVault.getAddressManager()
      .create("some-address", tags);

    assertEquals(addressResponse.getId(), "some-address-id");
    assertEquals(addressResponse.getAddressAlias(), "some-address-alias");
    assertEquals(addressResponse.getTags().get(0), "some-address-tag-1");
    assertEquals(addressResponse.getTags().get(1), "some-address-tag-2");
    assertNotNull(addressResponse.getAddress());
    assertNotNull(addressResponse.getAuthTag());
    assertNotNull(addressResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateAAddressAliasWithStateAndTags_WhenCreatingAlias_ShouldReturnAAddressAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-address-tag-1");
    tags.add("some-address-tag-2");

    AddressResponse addressResponse = this.staticVault.getAddressManager()
      .create("some-address", "some-state", tags);

    assertEquals(addressResponse.getId(), "some-address-id");
    assertEquals(addressResponse.getAddressAlias(), "some-address-alias");
    assertEquals(addressResponse.getTags().get(0), "some-address-tag-1");
    assertEquals(addressResponse.getTags().get(1), "some-address-tag-2");
    assertNotNull(addressResponse.getAddress());
    assertNotNull(addressResponse.getAuthTag());
    assertNotNull(addressResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateAAddressAliasWithState_WhenCreatingAlias_ShouldReturnAAddressAlias() throws Exception {
    AddressResponse addressResponse = this.staticVault.getAddressManager()
      .create("some-address", "some-state");

    assertEquals(addressResponse.getId(), "some-address-id");
    assertEquals(addressResponse.getAddressAlias(), "some-address-alias");
    assertNotNull(addressResponse.getAddress());
    assertNotNull(addressResponse.getAuthTag());
    assertNotNull(addressResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateAAddressAlias_WhenCreatingAlias_ShouldReturnAAddressAlias() throws Exception {
    AddressResponse addressResponse = this.staticVault.getAddressManager()
      .create("some-address");

    assertEquals(addressResponse.getId(), "some-address-id");
    assertEquals(addressResponse.getAddressAlias(), "some-address-alias");
    assertNotNull(addressResponse.getAddress());
    assertNotNull(addressResponse.getAuthTag());
    assertNotNull(addressResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAAddressAlias_WhenRetrievingAlias_ShouldReturnAAddressAlias() throws Exception {
    AddressResponse addressResponse = this.staticVault.getAddressManager()
      .retrieve("some-address-id");

    assertEquals(addressResponse.getId(), "some-address-id");
    assertEquals(addressResponse.getAddressAlias(), "some-address-alias");
    assertEquals(addressResponse.getTags().get(0), "some-address-tag-1");
    assertEquals(addressResponse.getTags().get(1), "some-address-tag-2");
    assertNotNull(addressResponse.getAddress());
    assertNotNull(addressResponse.getAuthTag());
    assertNotNull(addressResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAAddressAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnAAddressAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-address-tag-1");
    tags.add("some-address-tag-2");

    AddressResponse[] addressResponses = this.staticVault.getAddressManager()
      .retrieveFromRealData("some-address", tags);

    assertEquals(addressResponses.length, 1);
    assertEquals(addressResponses[0].getId(), "some-address-id");
    assertEquals(addressResponses[0].getAddressAlias(), "some-address-alias");
    assertNotNull(addressResponses[0].getAddress());
    assertNotNull(addressResponses[0].getAuthTag());
    assertNotNull(addressResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveAAddressAliasFromRealData_WhenRetrievingAlias_ShouldReturnAAddressAlias() throws Exception {
    AddressResponse[] addressResponses = this.staticVault.getAddressManager()
      .retrieveFromRealData("some-address");

    assertEquals(addressResponses.length, 1);
    assertEquals(addressResponses[0].getId(), "some-address-id");
    assertEquals(addressResponses[0].getAddressAlias(), "some-address-alias");
    assertNotNull(addressResponses[0].getAddress());
    assertNotNull(addressResponses[0].getAuthTag());
    assertNotNull(addressResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteAAddressAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    AddressManager addressManager = this.staticVault.getAddressManager();
    AddressResponse addressResponse = addressManager.create("some-address");
    boolean success = addressManager.delete(addressResponse.getId());

    assertEquals(success, true);
  }

}