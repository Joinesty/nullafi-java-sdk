package com.joinesty.domains.staticVault.managers.firstName;

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

public class FirstNameManagerTest extends BaseMock {

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
    tags.add("some-firstname-tag-1");
    tags.add("some-firstname-tag-2");

    AESDTO cipher = staticVault.encrypt("some-firstname");

    FirstNameResponse firstNameResponse = new FirstNameResponse();
    firstNameResponse.setId("some-firstname-id");
    firstNameResponse.setFirstnameAlias("some-firstname-alias");
    firstNameResponse.setFirstname(cipher.getEncryptedData());
    firstNameResponse.setAuthTag(cipher.getAuthenticationTag());
    firstNameResponse.setIv(cipher.getInitializationVector());
    firstNameResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/firstname")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(firstNameResponse.toJson())
      )
    );
  }

  private void mockPostWithGender(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-firstname-tag-1");
    tags.add("some-firstname-tag-2");

    AESDTO cipher = staticVault.encrypt("some-firstname");

    FirstNameResponse firstNameResponse = new FirstNameResponse();
    firstNameResponse.setId("some-firstname-id");
    firstNameResponse.setFirstnameAlias("some-firstname-alias");
    firstNameResponse.setFirstname(cipher.getEncryptedData());
    firstNameResponse.setAuthTag(cipher.getAuthenticationTag());
    firstNameResponse.setIv(cipher.getInitializationVector());
    firstNameResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/firstname/some-gender")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(firstNameResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-firstname-tag-1");
    tags.add("some-firstname-tag-2");

    AESDTO cipher = staticVault.encrypt("some-firstname");

    FirstNameResponse firstNameResponse = new FirstNameResponse();
    firstNameResponse.setId("some-firstname-id");
    firstNameResponse.setFirstnameAlias("some-firstname-alias");
    firstNameResponse.setFirstname(cipher.getEncryptedData());
    firstNameResponse.setAuthTag(cipher.getAuthenticationTag());
    firstNameResponse.setIv(cipher.getInitializationVector());
    firstNameResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/firstname/some-firstname-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(firstNameResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {
    AESDTO cipher = staticVault.encrypt("some-firstname");

    FirstNameResponse firstNameResponse = new FirstNameResponse();
    firstNameResponse.setId("some-firstname-id");
    firstNameResponse.setFirstnameAlias("some-firstname-alias");
    firstNameResponse.setFirstname(cipher.getEncryptedData());
    firstNameResponse.setAuthTag(cipher.getAuthenticationTag());
    firstNameResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/firstname")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + firstNameResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/firstname/some-firstname-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateAFirstNameAliasWithTags_WhenCreatingAlias_ShouldReturnAFirstNameAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-firstname-tag-1");
    tags.add("some-firstname-tag-2");

    FirstNameResponse firstNameResponse = this.staticVault.getFirstNameManager()
      .create("some-firstname", null, tags);

    assertEquals(firstNameResponse.getId(), "some-firstname-id");
    assertEquals(firstNameResponse.getFirstnameAlias(), "some-firstname-alias");
    assertEquals(firstNameResponse.getTags().get(0), "some-firstname-tag-1");
    assertEquals(firstNameResponse.getTags().get(1), "some-firstname-tag-2");
    assertNotNull(firstNameResponse.getFirstname());
    assertNotNull(firstNameResponse.getAuthTag());
    assertNotNull(firstNameResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateAFirstNameAlias_WhenCreatingAlias_ShouldReturnAFirstNameAlias() throws Exception {
    FirstNameResponse firstNameResponse = this.staticVault.getFirstNameManager()
      .create("some-firstname");

    assertEquals(firstNameResponse.getId(), "some-firstname-id");
    assertEquals(firstNameResponse.getFirstnameAlias(), "some-firstname-alias");
    assertNotNull(firstNameResponse.getFirstname());
    assertNotNull(firstNameResponse.getAuthTag());
    assertNotNull(firstNameResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateAFirstNameAliasWithGender_WhenCreatingAlias_ShouldReturnAFirstNameAlias() throws Exception {
    FirstNameResponse firstNameResponse = this.staticVault.getFirstNameManager()
      .create("some-firstname", "some-gender");

    assertEquals(firstNameResponse.getId(), "some-firstname-id");
    assertEquals(firstNameResponse.getFirstnameAlias(), "some-firstname-alias");
    assertNotNull(firstNameResponse.getFirstname());
    assertNotNull(firstNameResponse.getAuthTag());
    assertNotNull(firstNameResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAFirstNameAlias_WhenRetrievingAlias_ShouldReturnAFirstNameAlias() throws Exception {
    FirstNameResponse firstNameResponse = this.staticVault.getFirstNameManager()
      .retrieve("some-firstname-id");

    assertEquals(firstNameResponse.getId(), "some-firstname-id");
    assertEquals(firstNameResponse.getFirstnameAlias(), "some-firstname-alias");
    assertEquals(firstNameResponse.getTags().get(0), "some-firstname-tag-1");
    assertEquals(firstNameResponse.getTags().get(1), "some-firstname-tag-2");
    assertNotNull(firstNameResponse.getFirstname());
    assertNotNull(firstNameResponse.getAuthTag());
    assertNotNull(firstNameResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAFirstNameAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnAFirstNameAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-firstname-tag-1");
    tags.add("some-firstname-tag-2");

    FirstNameResponse[] firstNameResponses = this.staticVault.getFirstNameManager()
      .retrieveFromRealData("some-firstname", tags);

    assertEquals(firstNameResponses.length, 1);
    assertEquals(firstNameResponses[0].getId(), "some-firstname-id");
    assertEquals(firstNameResponses[0].getFirstnameAlias(), "some-firstname-alias");
    assertNotNull(firstNameResponses[0].getFirstname());
    assertNotNull(firstNameResponses[0].getAuthTag());
    assertNotNull(firstNameResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveAFirstNameAliasFromRealData_WhenRetrievingAlias_ShouldReturnAFirstNameAlias() throws Exception {
    FirstNameResponse[] firstNameResponses = this.staticVault.getFirstNameManager()
      .retrieveFromRealData("some-firstname");

    assertEquals(firstNameResponses.length, 1);
    assertEquals(firstNameResponses[0].getId(), "some-firstname-id");
    assertEquals(firstNameResponses[0].getFirstnameAlias(), "some-firstname-alias");
    assertNotNull(firstNameResponses[0].getFirstname());
    assertNotNull(firstNameResponses[0].getAuthTag());
    assertNotNull(firstNameResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteAFirstNameAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    FirstNameManager firstnameManager = this.staticVault.getFirstNameManager();
    FirstNameResponse firstNameResponse = firstnameManager.create("some-firstname");
    boolean success = firstnameManager.delete(firstNameResponse.getId());

    assertEquals(success, true);
  }

}