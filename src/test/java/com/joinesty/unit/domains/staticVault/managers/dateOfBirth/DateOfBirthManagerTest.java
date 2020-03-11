package com.joinesty.unit.domains.staticVault.managers.dateOfBirth;

import com.joinesty.domains.staticVault.managers.dateOfBirth.DateOfBirthManager;
import com.joinesty.domains.staticVault.managers.dateOfBirth.DateOfBirthResponse;
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

public class DateOfBirthManagerTest extends BaseMock {

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
    tags.add("some-dateofbirth-tag-1");
    tags.add("some-dateofbirth-tag-2");

    AESDTO cipher = staticVault.encrypt("some-dateofbirth");

    DateOfBirthResponse dateOfBirthResponse = new DateOfBirthResponse();
    dateOfBirthResponse.setId("some-dateofbirth-id");
    dateOfBirthResponse.setDateofbirthAlias("some-dateofbirth-alias");
    dateOfBirthResponse.setDateofbirth(cipher.getEncryptedData());
    dateOfBirthResponse.setAuthTag(cipher.getAuthenticationTag());
    dateOfBirthResponse.setIv(cipher.getInitializationVector());
    dateOfBirthResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/dateofbirth")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(dateOfBirthResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-dateofbirth-tag-1");
    tags.add("some-dateofbirth-tag-2");

    AESDTO cipher = staticVault.encrypt("some-dateofbirth");

    DateOfBirthResponse dateOfBirthResponse = new DateOfBirthResponse();
    dateOfBirthResponse.setId("some-dateofbirth-id");
    dateOfBirthResponse.setDateofbirthAlias("some-dateofbirth-alias");
    dateOfBirthResponse.setDateofbirth(cipher.getEncryptedData());
    dateOfBirthResponse.setAuthTag(cipher.getAuthenticationTag());
    dateOfBirthResponse.setIv(cipher.getInitializationVector());
    dateOfBirthResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/dateofbirth/some-dateofbirth-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(dateOfBirthResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {
    AESDTO cipher = staticVault.encrypt("some-dateofbirth");

    DateOfBirthResponse dateOfBirthResponse = new DateOfBirthResponse();
    dateOfBirthResponse.setId("some-dateofbirth-id");
    dateOfBirthResponse.setDateofbirthAlias("some-dateofbirth-alias");
    dateOfBirthResponse.setDateofbirth(cipher.getEncryptedData());
    dateOfBirthResponse.setAuthTag(cipher.getAuthenticationTag());
    dateOfBirthResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/dateofbirth")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + dateOfBirthResponse.toJson() + "]")
        )
    );

  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/dateofbirth/some-dateofbirth-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateADateOfBirthAliasWithTags_WhenCreatingAlias_ShouldReturnADateOfBirthAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-dateofbirth-tag-1");
    tags.add("some-dateofbirth-tag-2");

    DateOfBirthResponse dateOfBirthResponse = this.staticVault.getDateOfBirthManager()
      .create("some-dateofbirth", tags);

    assertEquals(dateOfBirthResponse.getId(), "some-dateofbirth-id");
    assertEquals(dateOfBirthResponse.getDateofbirthAlias(), "some-dateofbirth-alias");
    assertEquals(dateOfBirthResponse.getTags().get(0), "some-dateofbirth-tag-1");
    assertEquals(dateOfBirthResponse.getTags().get(1), "some-dateofbirth-tag-2");
    assertNotNull(dateOfBirthResponse.getDateofbirth());
    assertNotNull(dateOfBirthResponse.getAuthTag());
    assertNotNull(dateOfBirthResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateADateOfBirthAliasWithYearAndMonthAndTags_WhenCreatingAlias_ShouldReturnADateOfBirthAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-dateofbirth-tag-1");
    tags.add("some-dateofbirth-tag-2");

    DateOfBirthResponse dateOfBirthResponse = this.staticVault.getDateOfBirthManager()
      .create("some-dateofbirth", 1990, 1);

    assertEquals(dateOfBirthResponse.getId(), "some-dateofbirth-id");
    assertEquals(dateOfBirthResponse.getDateofbirthAlias(), "some-dateofbirth-alias");
    assertEquals(dateOfBirthResponse.getTags().get(0), "some-dateofbirth-tag-1");
    assertEquals(dateOfBirthResponse.getTags().get(1), "some-dateofbirth-tag-2");
    assertNotNull(dateOfBirthResponse.getDateofbirth());
    assertNotNull(dateOfBirthResponse.getAuthTag());
    assertNotNull(dateOfBirthResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateADateOfBirthAlias_WhenCreatingAlias_ShouldReturnADateOfBirthAlias() throws Exception {
    DateOfBirthResponse dateOfBirthResponse = this.staticVault.getDateOfBirthManager()
      .create("some-dateofbirth");

    assertEquals(dateOfBirthResponse.getId(), "some-dateofbirth-id");
    assertEquals(dateOfBirthResponse.getDateofbirthAlias(), "some-dateofbirth-alias");
    assertNotNull(dateOfBirthResponse.getDateofbirth());
    assertNotNull(dateOfBirthResponse.getAuthTag());
    assertNotNull(dateOfBirthResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateADateOfBirthAliasWithYearAndMonth_WhenCreatingAlias_ShouldReturnADateOfBirthAlias() throws Exception {
    DateOfBirthResponse dateOfBirthResponse = this.staticVault.getDateOfBirthManager()
      .create("some-dateofbirth", 1900, 1);

    assertEquals(dateOfBirthResponse.getId(), "some-dateofbirth-id");
    assertEquals(dateOfBirthResponse.getDateofbirthAlias(), "some-dateofbirth-alias");
    assertNotNull(dateOfBirthResponse.getDateofbirth());
    assertNotNull(dateOfBirthResponse.getAuthTag());
    assertNotNull(dateOfBirthResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveADateOfBirthAlias_WhenRetrievingAlias_ShouldReturnADateOfBirthAlias() throws Exception {
    DateOfBirthResponse dateOfBirthResponse = this.staticVault.getDateOfBirthManager()
      .retrieve("some-dateofbirth-id");

    assertEquals(dateOfBirthResponse.getId(), "some-dateofbirth-id");
    assertEquals(dateOfBirthResponse.getDateofbirthAlias(), "some-dateofbirth-alias");
    assertEquals(dateOfBirthResponse.getTags().get(0), "some-dateofbirth-tag-1");
    assertEquals(dateOfBirthResponse.getTags().get(1), "some-dateofbirth-tag-2");
    assertNotNull(dateOfBirthResponse.getDateofbirth());
    assertNotNull(dateOfBirthResponse.getAuthTag());
    assertNotNull(dateOfBirthResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveADateOfBirthAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnADateOfBirthAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-dateofbirth-tag-1");
    tags.add("some-dateofbirth-tag-2");

    DateOfBirthResponse[] dateOfBirthResponses = this.staticVault.getDateOfBirthManager()
      .retrieveFromRealData("some-dateofbirth", tags);

    assertEquals(dateOfBirthResponses.length, 1);
    assertEquals(dateOfBirthResponses[0].getId(), "some-dateofbirth-id");
    assertEquals(dateOfBirthResponses[0].getDateofbirthAlias(), "some-dateofbirth-alias");
    assertNotNull(dateOfBirthResponses[0].getDateofbirth());
    assertNotNull(dateOfBirthResponses[0].getAuthTag());
    assertNotNull(dateOfBirthResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveADateOfBirthAliasFromRealData_WhenRetrievingAlias_ShouldReturnADateOfBirthAlias() throws Exception {
    DateOfBirthResponse[] dateOfBirthResponses = this.staticVault.getDateOfBirthManager()
      .retrieveFromRealData("some-dateofbirth");

    assertEquals(dateOfBirthResponses.length, 1);
    assertEquals(dateOfBirthResponses[0].getId(), "some-dateofbirth-id");
    assertEquals(dateOfBirthResponses[0].getDateofbirthAlias(), "some-dateofbirth-alias");
    assertNotNull(dateOfBirthResponses[0].getDateofbirth());
    assertNotNull(dateOfBirthResponses[0].getAuthTag());
    assertNotNull(dateOfBirthResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteADateOfBirthAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    DateOfBirthManager dateofbirthManager = this.staticVault.getDateOfBirthManager();
    DateOfBirthResponse dateOfBirthResponse = dateofbirthManager.create("some-dateofbirth");
    boolean success = dateofbirthManager.delete(dateOfBirthResponse.getId());

    assertEquals(success, true);
  }

}