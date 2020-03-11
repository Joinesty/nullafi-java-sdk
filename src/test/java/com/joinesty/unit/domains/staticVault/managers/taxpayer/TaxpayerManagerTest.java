package com.joinesty.unit.domains.staticVault.managers.taxpayer;

import com.joinesty.domains.staticVault.managers.taxpayer.TaxpayerManager;
import com.joinesty.domains.staticVault.managers.taxpayer.TaxpayerResponse;
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

public class TaxpayerManagerTest extends BaseMock {

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
    tags.add("some-taxpayer-tag-1");
    tags.add("some-taxpayer-tag-2");

    AESDTO cipher = staticVault.encrypt("some-taxpayer");

    TaxpayerResponse taxpayerResponse = new TaxpayerResponse();
    taxpayerResponse.setId("some-taxpayer-id");
    taxpayerResponse.setTaxpayerAlias("some-taxpayer-alias");
    taxpayerResponse.setTaxpayer(cipher.getEncryptedData());
    taxpayerResponse.setAuthTag(cipher.getAuthenticationTag());
    taxpayerResponse.setIv(cipher.getInitializationVector());
    taxpayerResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/static/some-vault-id/taxpayer")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(taxpayerResponse.toJson())
      )
    );
  }

  private void mockGet(StaticVault staticVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-taxpayer-tag-1");
    tags.add("some-taxpayer-tag-2");

    AESDTO cipher = staticVault.encrypt("some-taxpayer");

    TaxpayerResponse taxpayerResponse = new TaxpayerResponse();
    taxpayerResponse.setId("some-taxpayer-id");
    taxpayerResponse.setTaxpayerAlias("some-taxpayer-alias");
    taxpayerResponse.setTaxpayer(cipher.getEncryptedData());
    taxpayerResponse.setAuthTag(cipher.getAuthenticationTag());
    taxpayerResponse.setIv(cipher.getInitializationVector());
    taxpayerResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/taxpayer/some-taxpayer-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(taxpayerResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(StaticVault staticVault) {
    AESDTO cipher = staticVault.encrypt("some-taxpayer");

    TaxpayerResponse taxpayerResponse = new TaxpayerResponse();
    taxpayerResponse.setId("some-taxpayer-id");
    taxpayerResponse.setTaxpayerAlias("some-taxpayer-alias");
    taxpayerResponse.setTaxpayer(cipher.getEncryptedData());
    taxpayerResponse.setAuthTag(cipher.getAuthenticationTag());
    taxpayerResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/static/some-vault-id/taxpayer")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + taxpayerResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/static/some-vault-id/taxpayer/some-taxpayer-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateATaxpayerAliasWithTags_WhenCreatingAlias_ShouldReturnATaxpayerAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-taxpayer-tag-1");
    tags.add("some-taxpayer-tag-2");

    TaxpayerResponse taxpayerResponse = this.staticVault.getTaxpayerManager()
      .create("some-taxpayer", tags);

    assertEquals(taxpayerResponse.getId(), "some-taxpayer-id");
    assertEquals(taxpayerResponse.getTaxpayerAlias(), "some-taxpayer-alias");
    assertEquals(taxpayerResponse.getTags().get(0), "some-taxpayer-tag-1");
    assertEquals(taxpayerResponse.getTags().get(1), "some-taxpayer-tag-2");
    assertNotNull(taxpayerResponse.getTaxpayer());
    assertNotNull(taxpayerResponse.getAuthTag());
    assertNotNull(taxpayerResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateATaxpayerAlias_WhenCreatingAlias_ShouldReturnATaxpayerAlias() throws Exception {
    TaxpayerResponse taxpayerResponse = this.staticVault.getTaxpayerManager()
      .create("some-taxpayer");

    assertEquals(taxpayerResponse.getId(), "some-taxpayer-id");
    assertEquals(taxpayerResponse.getTaxpayerAlias(), "some-taxpayer-alias");
    assertNotNull(taxpayerResponse.getTaxpayer());
    assertNotNull(taxpayerResponse.getAuthTag());
    assertNotNull(taxpayerResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveATaxpayerAlias_WhenRetrievingAlias_ShouldReturnATaxpayerAlias() throws Exception {
    TaxpayerResponse taxpayerResponse = this.staticVault.getTaxpayerManager()
      .retrieve("some-taxpayer-id");

    assertEquals(taxpayerResponse.getId(), "some-taxpayer-id");
    assertEquals(taxpayerResponse.getTaxpayerAlias(), "some-taxpayer-alias");
    assertEquals(taxpayerResponse.getTags().get(0), "some-taxpayer-tag-1");
    assertEquals(taxpayerResponse.getTags().get(1), "some-taxpayer-tag-2");
    assertNotNull(taxpayerResponse.getTaxpayer());
    assertNotNull(taxpayerResponse.getAuthTag());
    assertNotNull(taxpayerResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveATaxpayerAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnATaxpayerAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-taxpayer-tag-1");
    tags.add("some-taxpayer-tag-2");

    TaxpayerResponse[] taxpayerResponses = this.staticVault.getTaxpayerManager()
      .retrieveFromRealData("some-taxpayer", tags);

    assertEquals(taxpayerResponses.length, 1);
    assertEquals(taxpayerResponses[0].getId(), "some-taxpayer-id");
    assertEquals(taxpayerResponses[0].getTaxpayerAlias(), "some-taxpayer-alias");
    assertNotNull(taxpayerResponses[0].getTaxpayer());
    assertNotNull(taxpayerResponses[0].getAuthTag());
    assertNotNull(taxpayerResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveATaxpayerAliasFromRealData_WhenRetrievingAlias_ShouldReturnATaxpayerAlias() throws Exception {
    TaxpayerResponse[] taxpayerResponses = this.staticVault.getTaxpayerManager()
      .retrieveFromRealData("some-taxpayer");

    assertEquals(taxpayerResponses.length, 1);
    assertEquals(taxpayerResponses[0].getId(), "some-taxpayer-id");
    assertEquals(taxpayerResponses[0].getTaxpayerAlias(), "some-taxpayer-alias");
    assertNotNull(taxpayerResponses[0].getTaxpayer());
    assertNotNull(taxpayerResponses[0].getAuthTag());
    assertNotNull(taxpayerResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteATaxpayerAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    TaxpayerManager taxpayerManager = this.staticVault.getTaxpayerManager();
    TaxpayerResponse taxpayerResponse = taxpayerManager.create("some-taxpayer");
    boolean success = taxpayerManager.delete(taxpayerResponse.getId());

    assertEquals(success, true);
  }

}