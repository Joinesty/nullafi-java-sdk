package com.joinesty.domains.staticVault.managers.generic;

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

public class GenericManagerTest extends BaseMock {

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
    tags.add("some-generic-tag-1");
    tags.add("some-generic-tag-2");

    AESDTO cipher = staticVault.encrypt("some-generic");

    GenericResponse genericResponse = new GenericResponse();
    genericResponse.setId("some-generic-id");
    genericResponse.setAlias("some-generic-alias");
    genericResponse.setData(cipher.getEncryptedData());
    genericResponse.setAuthTag(cipher.getAuthenticationTag());
    genericResponse.setIv(cipher.getInitializationVector());
    genericResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/generic")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(genericResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-generic-tag-1");
    tags.add("some-generic-tag-2");

    AESDTO cipher = staticVault.encrypt("some-generic");

    GenericResponse genericResponse = new GenericResponse();
    genericResponse.setId("some-generic-id");
    genericResponse.setAlias("some-generic-alias");
    genericResponse.setData(cipher.getEncryptedData());
    genericResponse.setAuthTag(cipher.getAuthenticationTag());
    genericResponse.setIv(cipher.getInitializationVector());
    genericResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/generic/some-generic-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(genericResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {
    AESDTO cipher = staticVault.encrypt("some-generic");

    GenericResponse genericResponse = new GenericResponse();
    genericResponse.setId("some-generic-id");
    genericResponse.setAlias("some-generic-alias");
    genericResponse.setData(cipher.getEncryptedData());
    genericResponse.setAuthTag(cipher.getAuthenticationTag());
    genericResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/generic")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + genericResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/generic/some-generic-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateAGenericAliasWithTags_WhenCreatingAlias_ShouldReturnAGenericAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-generic-tag-1");
    tags.add("some-generic-tag-2");

    GenericResponse genericResponse = this.staticVault.getGenericManager()
      .create("some-generic", "[1-9]", tags);

    assertEquals(genericResponse.getId(), "some-generic-id");
    assertEquals(genericResponse.getAlias(), "some-generic-alias");
    assertEquals(genericResponse.getTags().get(0), "some-generic-tag-1");
    assertEquals(genericResponse.getTags().get(1), "some-generic-tag-2");
    assertNotNull(genericResponse.getData());
    assertNotNull(genericResponse.getAuthTag());
    assertNotNull(genericResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateAGenericAlias_WhenCreatingAlias_ShouldReturnAGenericAlias() throws Exception {
    GenericResponse genericResponse = this.staticVault.getGenericManager()
      .create("some-generic", "[1-9]");

    assertEquals(genericResponse.getId(), "some-generic-id");
    assertEquals(genericResponse.getAlias(), "some-generic-alias");
    assertNotNull(genericResponse.getData());
    assertNotNull(genericResponse.getAuthTag());
    assertNotNull(genericResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAGenericAlias_WhenRetrievingAlias_ShouldReturnAGenericAlias() throws Exception {
    GenericResponse genericResponse = this.staticVault.getGenericManager()
      .retrieve("some-generic-id");

    assertEquals(genericResponse.getId(), "some-generic-id");
    assertEquals(genericResponse.getAlias(), "some-generic-alias");
    assertEquals(genericResponse.getTags().get(0), "some-generic-tag-1");
    assertEquals(genericResponse.getTags().get(1), "some-generic-tag-2");
    assertNotNull(genericResponse.getData());
    assertNotNull(genericResponse.getAuthTag());
    assertNotNull(genericResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAGenericAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnAGenericAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-generic-tag-1");
    tags.add("some-generic-tag-2");

    GenericResponse[] genericResponses = this.staticVault.getGenericManager()
      .retrieveFromRealData("some-generic", tags);

    assertEquals(genericResponses.length, 1);
    assertEquals(genericResponses[0].getId(), "some-generic-id");
    assertEquals(genericResponses[0].getAlias(), "some-generic-alias");
    assertNotNull(genericResponses[0].getData());
    assertNotNull(genericResponses[0].getAuthTag());
    assertNotNull(genericResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveAGenericAliasFromRealData_WhenRetrievingAlias_ShouldReturnAGenericAlias() throws Exception {
    GenericResponse[] genericResponses = this.staticVault.getGenericManager()
      .retrieveFromRealData("some-generic");

    assertEquals(genericResponses.length, 1);
    assertEquals(genericResponses[0].getId(), "some-generic-id");
    assertEquals(genericResponses[0].getAlias(), "some-generic-alias");
    assertNotNull(genericResponses[0].getData());
    assertNotNull(genericResponses[0].getAuthTag());
    assertNotNull(genericResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteAGenericAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    GenericManager genericManager = this.staticVault.getGenericManager();
    GenericResponse genericResponse = genericManager.create("some-generic", "[1-9]");
    boolean success = genericManager.delete(genericResponse.getId());

    assertEquals(success, true);
  }

}