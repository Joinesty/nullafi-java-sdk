package com.joinesty.domains.staticVault.managers.lastName;

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

public class LastNameManagerTest extends BaseMock {

  private StaticVault staticVault;

  @Override
  public void setup() throws Exception {
    super.setup();

    Client client = this.createClient();
    this.staticVault = client.createStaticVault("some-static-vault");

    this.mockPost(this.staticVault);
    this.mockPostWithGender(this.staticVault);
    this.mockGet(this.staticVault);
    this.mockGetWithRealData(this.staticVault);
    this.mockDelete();
  }

  private void mockPost(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-lastname-tag-1");
    tags.add("some-lastname-tag-2");

    AESDTO cipher = staticVault.encrypt("some-lastname");

    LastNameResponse lastNameResponse = new LastNameResponse();
    lastNameResponse.setId("some-lastname-id");
    lastNameResponse.setLastnameAlias("some-lastname-alias");
    lastNameResponse.setLastname(cipher.getEncryptedData());
    lastNameResponse.setAuthTag(cipher.getAuthenticationTag());
    lastNameResponse.setIv(cipher.getInitializationVector());
    lastNameResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/lastname")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(lastNameResponse.toJson())
      )
    );
  }

  private void mockPostWithGender(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-lastname-tag-1");
    tags.add("some-lastname-tag-2");

    AESDTO cipher = staticVault.encrypt("some-lastname");

    LastNameResponse lastNameResponse = new LastNameResponse();
    lastNameResponse.setId("some-lastname-id");
    lastNameResponse.setLastnameAlias("some-lastname-alias");
    lastNameResponse.setLastname(cipher.getEncryptedData());
    lastNameResponse.setAuthTag(cipher.getAuthenticationTag());
    lastNameResponse.setIv(cipher.getInitializationVector());
    lastNameResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/lastname/some-gender")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(lastNameResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-lastname-tag-1");
    tags.add("some-lastname-tag-2");

    AESDTO cipher = staticVault.encrypt("some-lastname");

    LastNameResponse lastNameResponse = new LastNameResponse();
    lastNameResponse.setId("some-lastname-id");
    lastNameResponse.setLastnameAlias("some-lastname-alias");
    lastNameResponse.setLastname(cipher.getEncryptedData());
    lastNameResponse.setAuthTag(cipher.getAuthenticationTag());
    lastNameResponse.setIv(cipher.getInitializationVector());
    lastNameResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/lastname/some-lastname-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(lastNameResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {
    AESDTO cipher = staticVault.encrypt("some-lastname");

    LastNameResponse lastNameResponse = new LastNameResponse();
    lastNameResponse.setId("some-lastname-id");
    lastNameResponse.setLastnameAlias("some-lastname-alias");
    lastNameResponse.setLastname(cipher.getEncryptedData());
    lastNameResponse.setAuthTag(cipher.getAuthenticationTag());
    lastNameResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/lastname")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + lastNameResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/lastname/some-lastname-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateALastNameAliasWithTags_WhenCreatingAlias_ShouldReturnALastNameAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-lastname-tag-1");
    tags.add("some-lastname-tag-2");

    LastNameResponse lastNameResponse = this.staticVault.getLastNameManager()
      .create("some-lastname", tags);

    assertEquals(lastNameResponse.getId(), "some-lastname-id");
    assertEquals(lastNameResponse.getLastnameAlias(), "some-lastname-alias");
    assertEquals(lastNameResponse.getTags().get(0), "some-lastname-tag-1");
    assertEquals(lastNameResponse.getTags().get(1), "some-lastname-tag-2");
    assertNotNull(lastNameResponse.getLastname());
    assertNotNull(lastNameResponse.getAuthTag());
    assertNotNull(lastNameResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateALastNameAliasWithGenderTags_WhenCreatingAlias_ShouldReturnALastNameAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-lastname-tag-1");
    tags.add("some-lastname-tag-2");

    LastNameResponse lastNameResponse = this.staticVault.getLastNameManager()
      .create("some-lastname", "some-gender", tags);

    assertEquals(lastNameResponse.getId(), "some-lastname-id");
    assertEquals(lastNameResponse.getLastnameAlias(), "some-lastname-alias");
    assertEquals(lastNameResponse.getTags().get(0), "some-lastname-tag-1");
    assertEquals(lastNameResponse.getTags().get(1), "some-lastname-tag-2");
    assertNotNull(lastNameResponse.getLastname());
    assertNotNull(lastNameResponse.getAuthTag());
    assertNotNull(lastNameResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateALastNameAliasWithGender_WhenCreatingAlias_ShouldReturnALastNameAlias() throws Exception {
    LastNameResponse lastNameResponse = this.staticVault.getLastNameManager()
      .create("some-lastname", "some-gender");

    assertEquals(lastNameResponse.getId(), "some-lastname-id");
    assertEquals(lastNameResponse.getLastnameAlias(), "some-lastname-alias");
    assertNotNull(lastNameResponse.getLastname());
    assertNotNull(lastNameResponse.getAuthTag());
    assertNotNull(lastNameResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateALastNameAlias_WhenCreatingAlias_ShouldReturnALastNameAlias() throws Exception {
    LastNameResponse lastNameResponse = this.staticVault.getLastNameManager()
      .create("some-lastname");

    assertEquals(lastNameResponse.getId(), "some-lastname-id");
    assertEquals(lastNameResponse.getLastnameAlias(), "some-lastname-alias");
    assertNotNull(lastNameResponse.getLastname());
    assertNotNull(lastNameResponse.getAuthTag());
    assertNotNull(lastNameResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveALastNameAlias_WhenRetrievingAlias_ShouldReturnALastNameAlias() throws Exception {
    LastNameResponse lastNameResponse = this.staticVault.getLastNameManager()
      .retrieve("some-lastname-id");

    assertEquals(lastNameResponse.getId(), "some-lastname-id");
    assertEquals(lastNameResponse.getLastnameAlias(), "some-lastname-alias");
    assertEquals(lastNameResponse.getTags().get(0), "some-lastname-tag-1");
    assertEquals(lastNameResponse.getTags().get(1), "some-lastname-tag-2");
    assertNotNull(lastNameResponse.getLastname());
    assertNotNull(lastNameResponse.getAuthTag());
    assertNotNull(lastNameResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveALastNameAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnALastNameAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-lastname-tag-1");
    tags.add("some-lastname-tag-2");

    LastNameResponse[] lastNameResponses = this.staticVault.getLastNameManager()
      .retrieveFromRealData("some-lastname", tags);

    assertEquals(lastNameResponses.length, 1);
    assertEquals(lastNameResponses[0].getId(), "some-lastname-id");
    assertEquals(lastNameResponses[0].getLastnameAlias(), "some-lastname-alias");
    assertNotNull(lastNameResponses[0].getLastname());
    assertNotNull(lastNameResponses[0].getAuthTag());
    assertNotNull(lastNameResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveALastNameAliasFromRealData_WhenRetrievingAlias_ShouldReturnALastNameAlias() throws Exception {
    LastNameResponse[] lastNameResponses = this.staticVault.getLastNameManager()
      .retrieveFromRealData("some-lastname");

    assertEquals(lastNameResponses.length, 1);
    assertEquals(lastNameResponses[0].getId(), "some-lastname-id");
    assertEquals(lastNameResponses[0].getLastnameAlias(), "some-lastname-alias");
    assertNotNull(lastNameResponses[0].getLastname());
    assertNotNull(lastNameResponses[0].getAuthTag());
    assertNotNull(lastNameResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteALastNameAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    LastNameManager lastnameManager = this.staticVault.getLastNameManager();
    LastNameResponse lastNameResponse = lastnameManager.create("some-lastname");
    boolean success = lastnameManager.delete(lastNameResponse.getId());

    assertEquals(success, true);
  }

}