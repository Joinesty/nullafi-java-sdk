package com.joinesty.domains.staticVault.managers.ssn;

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

public class SsnManagerTest extends BaseMock {

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
    tags.add("some-ssn-tag-1");
    tags.add("some-ssn-tag-2");

    AESDTO cipher = staticVault.encrypt("some-ssn");

    SsnResponse ssnResponse = new SsnResponse();
    ssnResponse.setId("some-ssn-id");
    ssnResponse.setSsnAlias("some-ssn-alias");
    ssnResponse.setSsn(cipher.getEncryptedData());
    ssnResponse.setAuthTag(cipher.getAuthenticationTag());
    ssnResponse.setIv(cipher.getInitializationVector());
    ssnResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/ssn")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(ssnResponse.toJson())
      )
    );
  }

  private void mockPostWithState(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-ssn-tag-1");
    tags.add("some-ssn-tag-2");

    AESDTO cipher = staticVault.encrypt("some-ssn");

    SsnResponse ssnResponse = new SsnResponse();
    ssnResponse.setId("some-ssn-id");
    ssnResponse.setSsnAlias("some-ssn-alias");
    ssnResponse.setSsn(cipher.getEncryptedData());
    ssnResponse.setAuthTag(cipher.getAuthenticationTag());
    ssnResponse.setIv(cipher.getInitializationVector());
    ssnResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/ssn/some-state")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(ssnResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-ssn-tag-1");
    tags.add("some-ssn-tag-2");

    AESDTO cipher = staticVault.encrypt("some-ssn");

    SsnResponse ssnResponse = new SsnResponse();
    ssnResponse.setId("some-ssn-id");
    ssnResponse.setSsnAlias("some-ssn-alias");
    ssnResponse.setSsn(cipher.getEncryptedData());
    ssnResponse.setAuthTag(cipher.getAuthenticationTag());
    ssnResponse.setIv(cipher.getInitializationVector());
    ssnResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/ssn/some-ssn-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(ssnResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {
    AESDTO cipher = staticVault.encrypt("some-ssn");

    SsnResponse ssnResponse = new SsnResponse();
    ssnResponse.setId("some-ssn-id");
    ssnResponse.setSsnAlias("some-ssn-alias");
    ssnResponse.setSsn(cipher.getEncryptedData());
    ssnResponse.setAuthTag(cipher.getAuthenticationTag());
    ssnResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/ssn")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + ssnResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/ssn/some-ssn-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateASsnAliasWithTags_WhenCreatingAlias_ShouldReturnASsnAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-ssn-tag-1");
    tags.add("some-ssn-tag-2");

    SsnResponse ssnResponse = this.staticVault.getSsnManager()
      .create("some-ssn", null, tags);

    assertEquals(ssnResponse.getId(), "some-ssn-id");
    assertEquals(ssnResponse.getSsnAlias(), "some-ssn-alias");
    assertEquals(ssnResponse.getTags().get(0), "some-ssn-tag-1");
    assertEquals(ssnResponse.getTags().get(1), "some-ssn-tag-2");
    assertNotNull(ssnResponse.getSsn());
    assertNotNull(ssnResponse.getAuthTag());
    assertNotNull(ssnResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateASsnAliasWithState_WhenCreatingAlias_ShouldReturnASsnAlias() throws Exception {
    SsnResponse ssnResponse = this.staticVault.getSsnManager()
      .create("some-ssn", "some-state");

    assertEquals(ssnResponse.getId(), "some-ssn-id");
    assertEquals(ssnResponse.getSsnAlias(), "some-ssn-alias");
    assertNotNull(ssnResponse.getSsn());
    assertNotNull(ssnResponse.getAuthTag());
    assertNotNull(ssnResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateASsnAlias_WhenCreatingAlias_ShouldReturnASsnAlias() throws Exception {
    SsnResponse ssnResponse = this.staticVault.getSsnManager()
      .create("some-ssn");

    assertEquals(ssnResponse.getId(), "some-ssn-id");
    assertEquals(ssnResponse.getSsnAlias(), "some-ssn-alias");
    assertNotNull(ssnResponse.getSsn());
    assertNotNull(ssnResponse.getAuthTag());
    assertNotNull(ssnResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveASsnAlias_WhenRetrievingAlias_ShouldReturnASsnAlias() throws Exception {
    SsnResponse ssnResponse = this.staticVault.getSsnManager()
      .retrieve("some-ssn-id");

    assertEquals(ssnResponse.getId(), "some-ssn-id");
    assertEquals(ssnResponse.getSsnAlias(), "some-ssn-alias");
    assertEquals(ssnResponse.getTags().get(0), "some-ssn-tag-1");
    assertEquals(ssnResponse.getTags().get(1), "some-ssn-tag-2");
    assertNotNull(ssnResponse.getSsn());
    assertNotNull(ssnResponse.getAuthTag());
    assertNotNull(ssnResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveASsnAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnASsnAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-ssn-tag-1");
    tags.add("some-ssn-tag-2");

    SsnResponse[] ssnResponses = this.staticVault.getSsnManager()
      .retrieveFromRealData("some-ssn", tags);

    assertEquals(ssnResponses.length, 1);
    assertEquals(ssnResponses[0].getId(), "some-ssn-id");
    assertEquals(ssnResponses[0].getSsnAlias(), "some-ssn-alias");
    assertNotNull(ssnResponses[0].getSsn());
    assertNotNull(ssnResponses[0].getAuthTag());
    assertNotNull(ssnResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveASsnAliasFromRealData_WhenRetrievingAlias_ShouldReturnASsnAlias() throws Exception {
    SsnResponse[] ssnResponses = this.staticVault.getSsnManager()
      .retrieveFromRealData("some-ssn");

    assertEquals(ssnResponses.length, 1);
    assertEquals(ssnResponses[0].getId(), "some-ssn-id");
    assertEquals(ssnResponses[0].getSsnAlias(), "some-ssn-alias");
    assertNotNull(ssnResponses[0].getSsn());
    assertNotNull(ssnResponses[0].getAuthTag());
    assertNotNull(ssnResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteASsnAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    SsnManager ssnManager = this.staticVault.getSsnManager();
    SsnResponse ssnResponse = ssnManager.create("some-ssn");
    boolean success = ssnManager.delete(ssnResponse.getId());

    assertEquals(success, true);
  }

}