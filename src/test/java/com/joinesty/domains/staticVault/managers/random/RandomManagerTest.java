package com.joinesty.domains.staticVault.managers.random;

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

public class RandomManagerTest extends BaseMock {

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
    tags.add("some-random-tag-1");
    tags.add("some-random-tag-2");

    AESDTO cipher = staticVault.encrypt("some-random");

    RandomResponse randomResponse = new RandomResponse();
    randomResponse.setId("some-random-id");
    randomResponse.setAlias("some-random-alias");
    randomResponse.setData(cipher.getEncryptedData());
    randomResponse.setAuthTag(cipher.getAuthenticationTag());
    randomResponse.setIv(cipher.getInitializationVector());
    randomResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/random")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(randomResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-random-tag-1");
    tags.add("some-random-tag-2");

    AESDTO cipher = staticVault.encrypt("some-random");

    RandomResponse randomResponse = new RandomResponse();
    randomResponse.setId("some-random-id");
    randomResponse.setAlias("some-random-alias");
    randomResponse.setData(cipher.getEncryptedData());
    randomResponse.setAuthTag(cipher.getAuthenticationTag());
    randomResponse.setIv(cipher.getInitializationVector());
    randomResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/random/some-random-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(randomResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {

    AESDTO cipher = staticVault.encrypt("some-random");

    RandomResponse randomResponse = new RandomResponse();
    randomResponse.setId("some-random-id");
    randomResponse.setAlias("some-random-alias");
    randomResponse.setData(cipher.getEncryptedData());
    randomResponse.setAuthTag(cipher.getAuthenticationTag());
    randomResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/random")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + randomResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/random/some-random-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateARandomAliasWithTags_WhenCreatingAlias_ShouldReturnARandomAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-random-tag-1");
    tags.add("some-random-tag-2");

    RandomResponse randomResponse = this.staticVault.getRandomManager()
      .create("some-random", tags);

    assertEquals(randomResponse.getId(), "some-random-id");
    assertEquals(randomResponse.getAlias(), "some-random-alias");
    assertEquals(randomResponse.getTags().get(0), "some-random-tag-1");
    assertEquals(randomResponse.getTags().get(1), "some-random-tag-2");
    assertNotNull(randomResponse.getData());
    assertNotNull(randomResponse.getAuthTag());
    assertNotNull(randomResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateARandomAlias_WhenCreatingAlias_ShouldReturnARandomAlias() throws Exception {
    RandomResponse randomResponse = this.staticVault.getRandomManager()
      .create("some-random");

    assertEquals(randomResponse.getId(), "some-random-id");
    assertEquals(randomResponse.getAlias(), "some-random-alias");
    assertNotNull(randomResponse.getData());
    assertNotNull(randomResponse.getAuthTag());
    assertNotNull(randomResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveARandomAlias_WhenRetrievingAlias_ShouldReturnARandomAlias() throws Exception {
    RandomResponse randomResponse = this.staticVault.getRandomManager()
      .retrieve("some-random-id");

    assertEquals(randomResponse.getId(), "some-random-id");
    assertEquals(randomResponse.getAlias(), "some-random-alias");
    assertEquals(randomResponse.getTags().get(0), "some-random-tag-1");
    assertEquals(randomResponse.getTags().get(1), "some-random-tag-2");
    assertNotNull(randomResponse.getData());
    assertNotNull(randomResponse.getAuthTag());
    assertNotNull(randomResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveARandomAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnARandomAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-random-tag-1");
    tags.add("some-random-tag-2");

    RandomResponse[] randomResponses = this.staticVault.getRandomManager()
      .retrieveFromRealData("some-random", tags);

    assertEquals(randomResponses.length, 1);
    assertEquals(randomResponses[0].getId(), "some-random-id");
    assertEquals(randomResponses[0].getAlias(), "some-random-alias");
    assertNotNull(randomResponses[0].getData());
    assertNotNull(randomResponses[0].getAuthTag());
    assertNotNull(randomResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveARandomAliasFromRealData_WhenRetrievingAlias_ShouldReturnARandomAlias() throws Exception {
    RandomResponse[] randomResponses = this.staticVault.getRandomManager()
      .retrieveFromRealData("some-random");

    assertEquals(randomResponses.length, 1);
    assertEquals(randomResponses[0].getId(), "some-random-id");
    assertEquals(randomResponses[0].getAlias(), "some-random-alias");
    assertNotNull(randomResponses[0].getData());
    assertNotNull(randomResponses[0].getAuthTag());
    assertNotNull(randomResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteARandomAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    RandomManager randomManager = this.staticVault.getRandomManager();
    RandomResponse randomResponse = randomManager.create("some-random");
    boolean success = randomManager.delete(randomResponse.getId());

    assertEquals(success, true);
  }

}