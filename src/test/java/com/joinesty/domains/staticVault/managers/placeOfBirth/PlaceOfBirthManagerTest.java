package com.joinesty.domains.staticVault.managers.placeOfBirth;

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

public class PlaceOfBirthManagerTest extends BaseMock {

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
    tags.add("some-placeofbirth-tag-1");
    tags.add("some-placeofbirth-tag-2");

    AESDTO cipher = staticVault.encrypt("some-placeofbirth");

    PlaceOfBirthResponse placeOfBirthResponse = new PlaceOfBirthResponse();
    placeOfBirthResponse.setId("some-placeofbirth-id");
    placeOfBirthResponse.setPlaceofbirthAlias("some-placeofbirth-alias");
    placeOfBirthResponse.setPlaceofbirth(cipher.getEncryptedData());
    placeOfBirthResponse.setAuthTag(cipher.getAuthenticationTag());
    placeOfBirthResponse.setIv(cipher.getInitializationVector());
    placeOfBirthResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/placeofbirth")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(placeOfBirthResponse.toJson())
      )
    );
  }

  private void mockPostWithState(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-placeofbirth-tag-1");
    tags.add("some-placeofbirth-tag-2");

    AESDTO cipher = staticVault.encrypt("some-placeofbirth");

    PlaceOfBirthResponse placeOfBirthResponse = new PlaceOfBirthResponse();
    placeOfBirthResponse.setId("some-placeofbirth-id");
    placeOfBirthResponse.setPlaceofbirthAlias("some-placeofbirth-alias");
    placeOfBirthResponse.setPlaceofbirth(cipher.getEncryptedData());
    placeOfBirthResponse.setAuthTag(cipher.getAuthenticationTag());
    placeOfBirthResponse.setIv(cipher.getInitializationVector());
    placeOfBirthResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/placeofbirth/some-state")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(placeOfBirthResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-placeofbirth-tag-1");
    tags.add("some-placeofbirth-tag-2");

    AESDTO cipher = staticVault.encrypt("some-placeofbirth");

    PlaceOfBirthResponse placeOfBirthResponse = new PlaceOfBirthResponse();
    placeOfBirthResponse.setId("some-placeofbirth-id");
    placeOfBirthResponse.setPlaceofbirthAlias("some-placeofbirth-alias");
    placeOfBirthResponse.setPlaceofbirth(cipher.getEncryptedData());
    placeOfBirthResponse.setAuthTag(cipher.getAuthenticationTag());
    placeOfBirthResponse.setIv(cipher.getInitializationVector());
    placeOfBirthResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/placeofbirth/some-placeofbirth-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(placeOfBirthResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {
    AESDTO cipher = staticVault.encrypt("some-placeofbirth");

    PlaceOfBirthResponse placeOfBirthResponse = new PlaceOfBirthResponse();
    placeOfBirthResponse.setId("some-placeofbirth-id");
    placeOfBirthResponse.setPlaceofbirthAlias("some-placeofbirth-alias");
    placeOfBirthResponse.setPlaceofbirth(cipher.getEncryptedData());
    placeOfBirthResponse.setAuthTag(cipher.getAuthenticationTag());
    placeOfBirthResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/placeofbirth")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + placeOfBirthResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/placeofbirth/some-placeofbirth-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateAPlaceOfBirthAliasWithTags_WhenCreatingAlias_ShouldReturnAPlaceOfBirthAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-placeofbirth-tag-1");
    tags.add("some-placeofbirth-tag-2");

    PlaceOfBirthResponse placeOfBirthResponse = this.staticVault.getPlaceOfBirthManager()
      .create("some-placeofbirth", tags);

    assertEquals(placeOfBirthResponse.getId(), "some-placeofbirth-id");
    assertEquals(placeOfBirthResponse.getPlaceofbirthAlias(), "some-placeofbirth-alias");
    assertEquals(placeOfBirthResponse.getTags().get(0), "some-placeofbirth-tag-1");
    assertEquals(placeOfBirthResponse.getTags().get(1), "some-placeofbirth-tag-2");
    assertNotNull(placeOfBirthResponse.getPlaceofbirth());
    assertNotNull(placeOfBirthResponse.getAuthTag());
    assertNotNull(placeOfBirthResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateAPlaceOfBirthAliasWithStateTags_WhenCreatingAlias_ShouldReturnAPlaceOfBirthAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-placeofbirth-tag-1");
    tags.add("some-placeofbirth-tag-2");

    PlaceOfBirthResponse placeOfBirthResponse = this.staticVault.getPlaceOfBirthManager()
      .create("some-placeofbirth", "some-state", tags);

    assertEquals(placeOfBirthResponse.getId(), "some-placeofbirth-id");
    assertEquals(placeOfBirthResponse.getPlaceofbirthAlias(), "some-placeofbirth-alias");
    assertEquals(placeOfBirthResponse.getTags().get(0), "some-placeofbirth-tag-1");
    assertEquals(placeOfBirthResponse.getTags().get(1), "some-placeofbirth-tag-2");
    assertNotNull(placeOfBirthResponse.getPlaceofbirth());
    assertNotNull(placeOfBirthResponse.getAuthTag());
    assertNotNull(placeOfBirthResponse.getIv());
  }


  @Test
  public void GivenRequestToCreateAPlaceOfBirthAlias_WhenCreatingAlias_ShouldReturnAPlaceOfBirthAlias() throws Exception {
    PlaceOfBirthResponse placeOfBirthResponse = this.staticVault.getPlaceOfBirthManager()
      .create("some-placeofbirth");

    assertEquals(placeOfBirthResponse.getId(), "some-placeofbirth-id");
    assertEquals(placeOfBirthResponse.getPlaceofbirthAlias(), "some-placeofbirth-alias");
    assertNotNull(placeOfBirthResponse.getPlaceofbirth());
    assertNotNull(placeOfBirthResponse.getAuthTag());
    assertNotNull(placeOfBirthResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateAPlaceOfBirthAliasWithState_WhenCreatingAlias_ShouldReturnAPlaceOfBirthAlias() throws Exception {
    PlaceOfBirthResponse placeOfBirthResponse = this.staticVault.getPlaceOfBirthManager()
      .create("some-placeofbirth", "some-state");

    assertEquals(placeOfBirthResponse.getId(), "some-placeofbirth-id");
    assertEquals(placeOfBirthResponse.getPlaceofbirthAlias(), "some-placeofbirth-alias");
    assertNotNull(placeOfBirthResponse.getPlaceofbirth());
    assertNotNull(placeOfBirthResponse.getAuthTag());
    assertNotNull(placeOfBirthResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAPlaceOfBirthAlias_WhenRetrievingAlias_ShouldReturnAPlaceOfBirthAlias() throws Exception {
    PlaceOfBirthResponse placeOfBirthResponse = this.staticVault.getPlaceOfBirthManager()
      .retrieve("some-placeofbirth-id");

    assertEquals(placeOfBirthResponse.getId(), "some-placeofbirth-id");
    assertEquals(placeOfBirthResponse.getPlaceofbirthAlias(), "some-placeofbirth-alias");
    assertEquals(placeOfBirthResponse.getTags().get(0), "some-placeofbirth-tag-1");
    assertEquals(placeOfBirthResponse.getTags().get(1), "some-placeofbirth-tag-2");
    assertNotNull(placeOfBirthResponse.getPlaceofbirth());
    assertNotNull(placeOfBirthResponse.getAuthTag());
    assertNotNull(placeOfBirthResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAPlaceOfBirthAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnAPlaceOfBirthAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-placeofbirth-tag-1");
    tags.add("some-placeofbirth-tag-2");

    PlaceOfBirthResponse[] placeOfBirthResponses = this.staticVault.getPlaceOfBirthManager()
      .retrieveFromRealData("some-placeofbirth", tags);

    assertEquals(placeOfBirthResponses.length, 1);
    assertEquals(placeOfBirthResponses[0].getId(), "some-placeofbirth-id");
    assertEquals(placeOfBirthResponses[0].getPlaceofbirthAlias(), "some-placeofbirth-alias");
    assertNotNull(placeOfBirthResponses[0].getPlaceofbirth());
    assertNotNull(placeOfBirthResponses[0].getAuthTag());
    assertNotNull(placeOfBirthResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveAPlaceOfBirthAliasFromRealData_WhenRetrievingAlias_ShouldReturnAPlaceOfBirthAlias() throws Exception {
    PlaceOfBirthResponse[] placeOfBirthResponses = this.staticVault.getPlaceOfBirthManager()
      .retrieveFromRealData("some-placeofbirth");

    assertEquals(placeOfBirthResponses.length, 1);
    assertEquals(placeOfBirthResponses[0].getId(), "some-placeofbirth-id");
    assertEquals(placeOfBirthResponses[0].getPlaceofbirthAlias(), "some-placeofbirth-alias");
    assertNotNull(placeOfBirthResponses[0].getPlaceofbirth());
    assertNotNull(placeOfBirthResponses[0].getAuthTag());
    assertNotNull(placeOfBirthResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteAPlaceOfBirthAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    PlaceOfBirthManager placeofbirthManager = this.staticVault.getPlaceOfBirthManager();
    PlaceOfBirthResponse placeOfBirthResponse = placeofbirthManager.create("some-placeofbirth");
    boolean success = placeofbirthManager.delete(placeOfBirthResponse.getId());

    assertEquals(success, true);
  }

}