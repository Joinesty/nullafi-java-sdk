package com.joinesty.domains.staticVault.managers.gender;

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

public class GenderManagerTest extends BaseMock {

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
    tags.add("some-gender-tag-1");
    tags.add("some-gender-tag-2");

    AESDTO cipher = staticVault.encrypt("some-gender");

    GenderResponse genderResponse = new GenderResponse();
    genderResponse.setId("some-gender-id");
    genderResponse.setGenderAlias("some-gender-alias");
    genderResponse.setGender(cipher.getEncryptedData());
    genderResponse.setAuthTag(cipher.getAuthenticationTag());
    genderResponse.setIv(cipher.getInitializationVector());
    genderResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/gender")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(genderResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-gender-tag-1");
    tags.add("some-gender-tag-2");

    AESDTO cipher = staticVault.encrypt("some-gender");

    GenderResponse genderResponse = new GenderResponse();
    genderResponse.setId("some-gender-id");
    genderResponse.setGenderAlias("some-gender-alias");
    genderResponse.setGender(cipher.getEncryptedData());
    genderResponse.setAuthTag(cipher.getAuthenticationTag());
    genderResponse.setIv(cipher.getInitializationVector());
    genderResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/gender/some-gender-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(genderResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {
    AESDTO cipher = staticVault.encrypt("some-gender");

    GenderResponse genderResponse = new GenderResponse();
    genderResponse.setId("some-gender-id");
    genderResponse.setGenderAlias("some-gender-alias");
    genderResponse.setGender(cipher.getEncryptedData());
    genderResponse.setAuthTag(cipher.getAuthenticationTag());
    genderResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/gender")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + genderResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/gender/some-gender-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateAGenderAliasWithTags_WhenCreatingAlias_ShouldReturnAGenderAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-gender-tag-1");
    tags.add("some-gender-tag-2");

    GenderResponse genderResponse = this.staticVault.getGenderManager()
      .create("some-gender", tags);

    assertEquals(genderResponse.getId(), "some-gender-id");
    assertEquals(genderResponse.getGenderAlias(), "some-gender-alias");
    assertEquals(genderResponse.getTags().get(0), "some-gender-tag-1");
    assertEquals(genderResponse.getTags().get(1), "some-gender-tag-2");
    assertNotNull(genderResponse.getGender());
    assertNotNull(genderResponse.getAuthTag());
    assertNotNull(genderResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateAGenderAlias_WhenCreatingAlias_ShouldReturnAGenderAlias() throws Exception {
    GenderResponse genderResponse = this.staticVault.getGenderManager()
      .create("some-gender");

    assertEquals(genderResponse.getId(), "some-gender-id");
    assertEquals(genderResponse.getGenderAlias(), "some-gender-alias");
    assertNotNull(genderResponse.getGender());
    assertNotNull(genderResponse.getAuthTag());
    assertNotNull(genderResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAGenderAlias_WhenRetrievingAlias_ShouldReturnAGenderAlias() throws Exception {
    GenderResponse genderResponse = this.staticVault.getGenderManager()
      .retrieve("some-gender-id");

    assertEquals(genderResponse.getId(), "some-gender-id");
    assertEquals(genderResponse.getGenderAlias(), "some-gender-alias");
    assertEquals(genderResponse.getTags().get(0), "some-gender-tag-1");
    assertEquals(genderResponse.getTags().get(1), "some-gender-tag-2");
    assertNotNull(genderResponse.getGender());
    assertNotNull(genderResponse.getAuthTag());
    assertNotNull(genderResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAGenderAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnAGenderAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-gender-tag-1");
    tags.add("some-gender-tag-2");

    GenderResponse[] genderResponses = this.staticVault.getGenderManager()
      .retrieveFromRealData("some-gender", tags);

    assertEquals(genderResponses.length, 1);
    assertEquals(genderResponses[0].getId(), "some-gender-id");
    assertEquals(genderResponses[0].getGenderAlias(), "some-gender-alias");
    assertNotNull(genderResponses[0].getGender());
    assertNotNull(genderResponses[0].getAuthTag());
    assertNotNull(genderResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveAGenderAliasFromRealData_WhenRetrievingAlias_ShouldReturnAGenderAlias() throws Exception {
    GenderResponse[] genderResponses = this.staticVault.getGenderManager()
      .retrieveFromRealData("some-gender");

    assertEquals(genderResponses.length, 1);
    assertEquals(genderResponses[0].getId(), "some-gender-id");
    assertEquals(genderResponses[0].getGenderAlias(), "some-gender-alias");
    assertNotNull(genderResponses[0].getGender());
    assertNotNull(genderResponses[0].getAuthTag());
    assertNotNull(genderResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteAGenderAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    GenderManager genderManager = this.staticVault.getGenderManager();
    GenderResponse genderResponse = genderManager.create("some-gender");
    boolean success = genderManager.delete(genderResponse.getId());

    assertEquals(success, true);
  }

}