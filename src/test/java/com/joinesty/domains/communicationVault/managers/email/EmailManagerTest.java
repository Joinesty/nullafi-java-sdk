package com.joinesty.domains.communicationVault.managers.email;

import com.joinesty.BaseMock;
import com.joinesty.Client;
import com.joinesty.domains.communicationVault.CommunicationVault;
import com.joinesty.services.crypto.AESDTO;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EmailManagerTest extends BaseMock {

  private CommunicationVault communicationVault;

  @Override
  public void setup() throws Exception {
    super.setup();

    Client client = this.createClient();
    this.communicationVault = client.createCommunicationVault("some-communication-vault");

    this.mockPost(this.communicationVault);
    this.mockPostWithState(this.communicationVault);
    this.mockGet(this.communicationVault);
    this.mockGetWithRealData(this.communicationVault);
    this.mockDelete();
  }

  private void mockPost(CommunicationVault communicationVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-email-tag-1");
    tags.add("some-email-tag-2");

    AESDTO cipher = communicationVault.encrypt("some-email");

    EmailResponse emailResponse = new EmailResponse();
    emailResponse.setId("some-email-id");
    emailResponse.setEmailAlias("some-email-alias");
    emailResponse.setEmail(cipher.getEncryptedData());
    emailResponse.setAuthTag(cipher.getAuthenticationTag());
    emailResponse.setIv(cipher.getInitializationVector());
    emailResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/communication/some-vault-id/email")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(emailResponse.toJson())
      )
    );
  }

  private void mockPostWithState(CommunicationVault communicationVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-email-tag-1");
    tags.add("some-email-tag-2");

    AESDTO cipher = communicationVault.encrypt("some-email");

    EmailResponse emailResponse = new EmailResponse();
    emailResponse.setId("some-email-id");
    emailResponse.setEmailAlias("some-email-alias");
    emailResponse.setEmail(cipher.getEncryptedData());
    emailResponse.setAuthTag(cipher.getAuthenticationTag());
    emailResponse.setIv(cipher.getInitializationVector());
    emailResponse.setTags(tags);

    wireMockRule.stubFor(
      post(
        urlPathEqualTo("/vault/communication/some-vault-id/email/some-state")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(emailResponse.toJson())
      )
    );
  }

  private void mockGet(CommunicationVault communicationVault) {
    List<String> tags = new ArrayList<>();
    tags.add("some-email-tag-1");
    tags.add("some-email-tag-2");

    AESDTO cipher = communicationVault.encrypt("some-email");

    EmailResponse emailResponse = new EmailResponse();
    emailResponse.setId("some-email-id");
    emailResponse.setEmailAlias("some-email-alias");
    emailResponse.setEmail(cipher.getEncryptedData());
    emailResponse.setAuthTag(cipher.getAuthenticationTag());
    emailResponse.setIv(cipher.getInitializationVector());
    emailResponse.setTags(tags);

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/communication/some-vault-id/email/some-email-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody(emailResponse.toJson())
      )
    );
  }

  private void mockGetWithRealData(CommunicationVault communicationVault) {
    AESDTO cipher = communicationVault.encrypt("some-email");

    EmailResponse emailResponse = new EmailResponse();
    emailResponse.setId("some-email-id");
    emailResponse.setEmailAlias("some-email-alias");
    emailResponse.setEmail(cipher.getEncryptedData());
    emailResponse.setAuthTag(cipher.getAuthenticationTag());
    emailResponse.setIv(cipher.getInitializationVector());

    wireMockRule.stubFor(
      get(
        urlPathEqualTo("/vault/communication/some-vault-id/email")
      )
        .withQueryParam("hash", matching(".*"))
        .willReturn(
          aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withBody("[" + emailResponse.toJson() + "]")
        )
    );
  }

  private void mockDelete() {
    wireMockRule.stubFor(
      delete(
        urlPathEqualTo("/vault/communication/some-vault-id/email/some-email-id")
      ).willReturn(
        aResponse()
          .withStatus(HttpStatus.SC_OK)
          .withBody("{\"ok\":true}")
      )
    );
  }

  @Test
  public void GivenRequestToCreateAEmailAliasWithTags_WhenCreatingAlias_ShouldReturnAEmailAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-email-tag-1");
    tags.add("some-email-tag-2");

    EmailResponse emailResponse = this.communicationVault.getEmailManager()
      .create("some-email", tags);

    assertEquals(emailResponse.getId(), "some-email-id");
    assertEquals(emailResponse.getEmailAlias(), "some-email-alias");
    assertEquals(emailResponse.getTags().get(0), "some-email-tag-1");
    assertEquals(emailResponse.getTags().get(1), "some-email-tag-2");
    assertNotNull(emailResponse.getEmail());
    assertNotNull(emailResponse.getAuthTag());
    assertNotNull(emailResponse.getIv());
  }

  @Test
  public void GivenRequestToCreateAEmailAlias_WhenCreatingAlias_ShouldReturnAEmailAlias() throws Exception {
    EmailResponse emailResponse = this.communicationVault.getEmailManager()
      .create("some-email");

    assertEquals(emailResponse.getId(), "some-email-id");
    assertEquals(emailResponse.getEmailAlias(), "some-email-alias");
    assertNotNull(emailResponse.getEmail());
    assertNotNull(emailResponse.getAuthTag());
    assertNotNull(emailResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAEmailAlias_WhenRetrievingAlias_ShouldReturnAEmailAlias() throws Exception {
    EmailResponse emailResponse = this.communicationVault.getEmailManager()
      .retrieve("some-email-id");

    assertEquals(emailResponse.getId(), "some-email-id");
    assertEquals(emailResponse.getEmailAlias(), "some-email-alias");
    assertEquals(emailResponse.getTags().get(0), "some-email-tag-1");
    assertEquals(emailResponse.getTags().get(1), "some-email-tag-2");
    assertNotNull(emailResponse.getEmail());
    assertNotNull(emailResponse.getAuthTag());
    assertNotNull(emailResponse.getIv());
  }

  @Test
  public void GivenRequestToRetrieveAEmailAliasFromRealDataWithTags_WhenRetrievingAlias_ShouldReturnAEmailAlias() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("some-email-tag-1");
    tags.add("some-email-tag-2");

    EmailResponse[] emailResponses = this.communicationVault.getEmailManager()
      .retrieveFromRealData("some-email", tags);

    assertEquals(emailResponses.length, 1);
    assertEquals(emailResponses[0].getId(), "some-email-id");
    assertEquals(emailResponses[0].getEmailAlias(), "some-email-alias");
    assertNotNull(emailResponses[0].getEmail());
    assertNotNull(emailResponses[0].getAuthTag());
    assertNotNull(emailResponses[0].getIv());
  }

  @Test
  public void GivenRequestToRetrieveAEmailAliasFromRealData_WhenRetrievingAlias_ShouldReturnAEmailAlias() throws Exception {
    EmailResponse[] emailResponses = this.communicationVault.getEmailManager()
      .retrieveFromRealData("some-email");

    assertEquals(emailResponses.length, 1);
    assertEquals(emailResponses[0].getId(), "some-email-id");
    assertEquals(emailResponses[0].getEmailAlias(), "some-email-alias");
    assertNotNull(emailResponses[0].getEmail());
    assertNotNull(emailResponses[0].getAuthTag());
    assertNotNull(emailResponses[0].getIv());
  }

  @Test
  public void GivenRequestToDeleteAEmailAlias_WhenDeletingAlias_ShouldReturnAOkResponse() throws Exception {
    EmailManager emailManager = this.communicationVault.getEmailManager();
    EmailResponse emailResponse = emailManager.create("some-email");
    boolean success = emailManager.delete(emailResponse.getId());

    assertEquals(success, true);
  }

}