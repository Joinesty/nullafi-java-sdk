package com.joinesty.domains.staticVault.managers.race;

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

public class RaceManagerTest extends BaseMock {

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
    tags.add("some-race-tag-1");
    tags.add("some-race-tag-2");

    AESDTO cipher = staticVault.encrypt("some-race");

    RaceResponse raceResponse = new RaceResponse();
    raceResponse.setId("some-race-id");
    raceResponse.setRaceAlias("some-race-alias");
    raceResponse.setRace(cipher.getEncryptedData());
    raceResponse.setAuthTag(cipher.getAuthenticationTag());
    raceResponse.setIv(cipher.getInitializationVector());
    raceResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/race")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(raceResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-race-tag-1");
    tags.add("some-race-tag-2");

    AESDTO cipher = staticVault.encrypt("some-race");

    RaceResponse raceResponse = new RaceResponse();
    raceResponse.setId("some-race-id");
    raceResponse.setRaceAlias("some-race-alias");
    raceResponse.setRace(cipher.getEncryptedData());
    raceResponse.setAuthTag(cipher.getAuthenticationTag());
    raceResponse.setIv(cipher.getInitializationVector());
    raceResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/race/some-race-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(raceResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {
    AESDTO cipher = staticVault.encrypt("some-race");

    RaceResponse raceResponse = new RaceResponse();
    raceResponse.setId("some-race-id");
    raceResponse.setRaceAlias("some-race-alias");
    raceResponse.setRace(cipher.getEncryptedData());
    raceResponse.setAuthTag(cipher.getAuthenticationTag());
    raceResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/race")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + raceResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/race/some-race-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateARaceAliasWithTags_WhenCreatingAlias_ShouldReturnARaceAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-race-tag-1");
    tags.add("some-race-tag-2");

    RaceResponse raceResponse = this.staticVault.getRaceManager()
      .create("some-race", tags);

    assertEquals(raceResponse.getId(), "some-race-id");
    assertEquals(raceResponse.getRaceAlias(), "some-race-alias");
    assertEquals(raceResponse.getTags().get(0), "some-race-tag-1");
    assertEquals(raceResponse.getTags().get(1), "some-race-tag-2");
    assertNotNull(raceResponse.getRace());
    assertNotNull(raceResponse.getAuthTag());
    assertNotNull(raceResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateARaceAlias_WhenCreatingAlias_ShouldReturnARaceAlias() throws Exception {
    RaceResponse raceResponse = this.staticVault.getRaceManager()
      .create("some-race");

    assertEquals(raceResponse.getId(), "some-race-id");
    assertEquals(raceResponse.getRaceAlias(), "some-race-alias");
    assertNotNull(raceResponse.getRace());
    assertNotNull(raceResponse.getAuthTag());
    assertNotNull(raceResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveARaceAlias_WhenRetrievingAlias_ShouldReturnARaceAlias() throws Exception {
    RaceResponse raceResponse = this.staticVault.getRaceManager()
      .retrieve("some-race-id");

    assertEquals(raceResponse.getId(), "some-race-id");
    assertEquals(raceResponse.getRaceAlias(), "some-race-alias");
    assertEquals(raceResponse.getTags().get(0), "some-race-tag-1");
    assertEquals(raceResponse.getTags().get(1), "some-race-tag-2");
    assertNotNull(raceResponse.getRace());
    assertNotNull(raceResponse.getAuthTag());
    assertNotNull(raceResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveARaceAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnARaceAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-race-tag-1");
    tags.add("some-race-tag-2");

    RaceResponse[] raceResponses = this.staticVault.getRaceManager()
      .retrieveFromRealData("some-race", tags);

    assertEquals(raceResponses.length, 1);
    assertEquals(raceResponses[0].getId(), "some-race-id");
    assertEquals(raceResponses[0].getRaceAlias(), "some-race-alias");
    assertNotNull(raceResponses[0].getRace());
    assertNotNull(raceResponses[0].getAuthTag());
    assertNotNull(raceResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveARaceAliasFromRealData_WhenRetrievingAlias_ShouldReturnARaceAlias() throws Exception {
    RaceResponse[] raceResponses = this.staticVault.getRaceManager()
      .retrieveFromRealData("some-race");

    assertEquals(raceResponses.length, 1);
    assertEquals(raceResponses[0].getId(), "some-race-id");
    assertEquals(raceResponses[0].getRaceAlias(), "some-race-alias");
    assertNotNull(raceResponses[0].getRace());
    assertNotNull(raceResponses[0].getAuthTag());
    assertNotNull(raceResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteARaceAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    RaceManager raceManager = this.staticVault.getRaceManager();
    RaceResponse raceResponse = raceManager.create("some-race");
    boolean success = raceManager.delete(raceResponse.getId());

    assertEquals(success, true);
  }

}