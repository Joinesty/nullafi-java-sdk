package com.joinesty;

import com.joinesty.domains.communicationVault.CommunicationVault;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.services.Security;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClientTest extends BaseMock {

  private Client client;

  private Security security;

  @Override
  public void setup() throws Exception {
    super.setup();

    this.client = this.createClient();
    this.security = new Security();
  }

  @Test
  public void GivenTheNeedToCommunicateWithAPI_WhenUsingTheSDK_ShouldAuthenticate() {
    assertEquals(client.getHashKey(), "some-hashKey");
    assertEquals(client.getTenantId(), "some-tenantId");
  }

  @Test
  public void GivenRequestToCreateStaticVault_WhenCreatingAStaticVault_ReturnAStaticVaultInstance() throws Exception {
    String vaultId = "some-vault-id";
    String vaultName = "some-vault-name";

    StaticVault staticVault = this.client.createStaticVault(vaultName);

    assertEquals(staticVault.getId(), vaultId);
    assertEquals(staticVault.getName(), vaultName);

    assertNotNull(staticVault.getMasterKey());

    assertNotNull(staticVault.getAddressManager());
    assertNotNull(staticVault.getDateOfBirthManager());
    assertNotNull(staticVault.getDriversLicenseManager());
    assertNotNull(staticVault.getFirstNameManager());
    assertNotNull(staticVault.getGenderManager());
    assertNotNull(staticVault.getGenericManager());
    assertNotNull(staticVault.getLastNameManager());
    assertNotNull(staticVault.getPassportManager());
    assertNotNull(staticVault.getPlaceOfBirthManager());
    assertNotNull(staticVault.getRaceManager());
    assertNotNull(staticVault.getRandomManager());
    assertNotNull(staticVault.getSsnManager());
    assertNotNull(staticVault.getTaxpayerManager());
    assertNotNull(staticVault.getVehicleRegistrationManager());
  }

  @Test
  public void GivenRequestToCreateStaticVaultWithTags_WhenCreatingAStaticVault_ReturnAStaticVaultInstance() throws Exception {
    String vaultId = "some-vault-id";
    String vaultName = "some-vault-name";

    List<String> tags = new ArrayList<>();
    tags.add("some-vault-tag-1");
    tags.add("some-vault-tag-2");

    StaticVault staticVault = this.client.createStaticVault(vaultName, tags);

    assertEquals(staticVault.getId(), vaultId);
    assertEquals(staticVault.getName(), vaultName);
    assertEquals(staticVault.getTags().get(0), "some-vault-tag-1");
    assertEquals(staticVault.getTags().get(1), "some-vault-tag-2");

    assertNotNull(staticVault.getMasterKey());

    assertNotNull(staticVault.getAddressManager());
    assertNotNull(staticVault.getDateOfBirthManager());
    assertNotNull(staticVault.getDriversLicenseManager());
    assertNotNull(staticVault.getFirstNameManager());
    assertNotNull(staticVault.getGenderManager());
    assertNotNull(staticVault.getGenericManager());
    assertNotNull(staticVault.getLastNameManager());
    assertNotNull(staticVault.getPassportManager());
    assertNotNull(staticVault.getPlaceOfBirthManager());
    assertNotNull(staticVault.getRaceManager());
    assertNotNull(staticVault.getRandomManager());
    assertNotNull(staticVault.getSsnManager());
    assertNotNull(staticVault.getTaxpayerManager());
    assertNotNull(staticVault.getVehicleRegistrationManager());
  }

  @Test
  public void GivenRequestToRetrieveStaticVault_WhenRetrievingAStaticVault_ReturnAStaticVaultInstance() throws Exception {
    String vaultId = "some-vault-id";
    String vaultName = "some-vault-name";
    String masterKey = new String(Base64.encode(security.getAes().generateMasterKey().getEncoded()));

    List<String> tags = new ArrayList<>();
    tags.add("some-vault-tag-1");
    tags.add("some-vault-tag-2");

    StaticVault staticVault = this.client.retrieveStaticVault(vaultId, masterKey);

    assertEquals(staticVault.getId(), vaultId);
    assertEquals(staticVault.getName(), vaultName);
    assertEquals(staticVault.getTags().get(0), "some-vault-tag-1");
    assertEquals(staticVault.getTags().get(1), "some-vault-tag-2");

    assertNotNull(staticVault.getMasterKey());

    assertNotNull(staticVault.getAddressManager());
    assertNotNull(staticVault.getDateOfBirthManager());
    assertNotNull(staticVault.getDriversLicenseManager());
    assertNotNull(staticVault.getFirstNameManager());
    assertNotNull(staticVault.getGenderManager());
    assertNotNull(staticVault.getGenericManager());
    assertNotNull(staticVault.getLastNameManager());
    assertNotNull(staticVault.getPassportManager());
    assertNotNull(staticVault.getPlaceOfBirthManager());
    assertNotNull(staticVault.getRaceManager());
    assertNotNull(staticVault.getRandomManager());
    assertNotNull(staticVault.getSsnManager());
    assertNotNull(staticVault.getTaxpayerManager());
    assertNotNull(staticVault.getVehicleRegistrationManager());
  }

  @Test
  public void GivenRequestToDeleteStaticVault_WhenDeletingAStaticVault_ReturnAOkResponse() throws Exception {
    StaticVault staticVault = this.client.createStaticVault("some-vault-name");
    boolean result = this.client.deleteStaticVault(staticVault.getId());

    assertEquals(result, true);
  }

  @Test
  public void GivenRequestToCreateCommunicationVault_WhenCreatingACommunicationVault_ReturnACommunicationVaultInstance() throws Exception {
    String vaultId = "some-vault-id";
    String vaultName = "some-vault-name";

    CommunicationVault communicationVault = this.client.createCommunicationVault(vaultName);

    assertEquals(communicationVault.getId(), vaultId);
    assertEquals(communicationVault.getName(), vaultName);

    assertNotNull(communicationVault.getMasterKey());

    assertNotNull(communicationVault.getEmailManager());
  }

  @Test
  public void GivenRequestToCreateCommunicationVaultWithTags_WhenCreatingACommunicationVault_ReturnACommunicationVaultInstance() throws Exception {
    String vaultId = "some-vault-id";
    String vaultName = "some-vault-name";

    List<String> tags = new ArrayList<>();
    tags.add("some-vault-tag-1");
    tags.add("some-vault-tag-2");

    CommunicationVault communicationVault = this.client.createCommunicationVault(vaultName, tags);

    assertEquals(communicationVault.getId(), vaultId);
    assertEquals(communicationVault.getName(), vaultName);
    assertEquals(communicationVault.getTags().get(0), "some-vault-tag-1");
    assertEquals(communicationVault.getTags().get(1), "some-vault-tag-2");

    assertNotNull(communicationVault.getMasterKey());

    assertNotNull(communicationVault.getEmailManager());
  }

  @Test
  public void GivenRequestToRetrieveCommunicationVault_WhenRetrievingACommunicationVault_ReturnACommunicationVaultInstance() throws Exception {
    String vaultId = "some-vault-id";
    String vaultName = "some-vault-name";
    String masterKey = new String(Base64.encode(this.security.getAes().generateMasterKey().getEncoded()));

    List<String> tags = new ArrayList<>();
    tags.add("some-address-tag-1");
    tags.add("some-address-tag-2");

    CommunicationVault communicationVault = this.client.retrieveCommunicationVault(vaultId, masterKey);

    assertEquals(communicationVault.getId(), vaultId);
    assertEquals(communicationVault.getName(), vaultName);

    assertNotNull(communicationVault.getEmailManager());
  }

  @Test
  public void GivenRequestToDeleteCommunicationVault_WhenDeletingACommunicationVault_ReturnAOkResponse() throws Exception {
    CommunicationVault communicationVault = this.client.createCommunicationVault("some-vault-name");
    boolean result = this.client.deleteCommunicationVault(communicationVault.getId());

    assertEquals(result, true);
  }


}