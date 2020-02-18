package com.joinesty.domains.staticVault;

import com.joinesty.Client;
import com.joinesty.domains.staticVault.managers.address.AddressManager;
import com.joinesty.domains.staticVault.managers.dateOfBirth.DateOfBirthManager;
import com.joinesty.domains.staticVault.managers.driversLicense.DriversLicenseManager;
import com.joinesty.domains.staticVault.managers.firstName.FirstNameManager;
import com.joinesty.domains.staticVault.managers.gender.GenderManager;
import com.joinesty.domains.staticVault.managers.generic.GenericManager;
import com.joinesty.domains.staticVault.managers.lastName.LastNameManager;
import com.joinesty.domains.staticVault.managers.passport.PassportManager;
import com.joinesty.domains.staticVault.managers.placeOfBirth.PlaceOfBirthManager;
import com.joinesty.domains.staticVault.managers.race.RaceManager;
import com.joinesty.domains.staticVault.managers.random.RandomManager;
import com.joinesty.domains.staticVault.managers.ssn.SsnManager;
import com.joinesty.domains.staticVault.managers.taxpayer.TaxpayerManager;
import com.joinesty.domains.staticVault.managers.vehicleRegistration.VehicleRegistrationManager;
import com.joinesty.services.API;
import com.joinesty.services.Security;
import com.joinesty.services.crypto.AESDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

/**
 * StaticVault Class
 */
public class StaticVault extends API {

  private String id;

  private String name;

  private Client client;

  private List<String> tags;


  private AddressManager addressManager;

  private DateOfBirthManager dateOfBirthManager;

  private DriversLicenseManager driversLicenseManager;

  private FirstNameManager firstNameManager;

  private GenderManager genderManager;

  private GenericManager genericManager;

  private LastNameManager lastNameManager;

  private PassportManager passportManager;

  private PlaceOfBirthManager placeOfBirthManager;

  private RaceManager raceManager;

  private RandomManager randomManager;

  private SsnManager ssnManager;

  private TaxpayerManager taxpayerManager;

  private VehicleRegistrationManager vehicleRegistrationManager;


  private SecretKey secretKey;

  private Security security;


  private StaticVault() {

  }

  private StaticVault(Client client, String id, String name, List<String> tags, SecretKey secretKey) {
    this.client = client;
    this.security = new Security();
    this.id = id;
    this.name = name;
    this.secretKey = secretKey;
    this.tags = tags;

    this.addressManager = new AddressManager(this);
    this.dateOfBirthManager = new DateOfBirthManager(this);
    this.driversLicenseManager = new DriversLicenseManager(this);
    this.firstNameManager = new FirstNameManager(this);
    this.genderManager = new GenderManager(this);
    this.genericManager = new GenericManager(this);
    this.lastNameManager = new LastNameManager(this);
    this.passportManager = new PassportManager(this);
    this.placeOfBirthManager = new PlaceOfBirthManager(this);
    this.raceManager = new RaceManager(this);
    this.randomManager = new RandomManager(this);
    this.ssnManager = new SsnManager(this);
    this.taxpayerManager = new TaxpayerManager(this);
    this.vehicleRegistrationManager = new VehicleRegistrationManager(this);
  }

  /**
   * Request the API to create a new static vault
   *
   * @param client Client
   * @param name Name
   * @param tags Tags
   * @return StaticVault
   * @throws Exception Exception
   */
  public static StaticVault createStaticVault(Client client, String name, List<String> tags) throws Exception {
    StaticVaultRequest requestBody = new StaticVaultRequest();

    requestBody.setName(name);
    requestBody.setTags(tags);

    HttpResponse<StaticVaultResponse> response = Unirest
      .post(client.getApi().setUrl("/vault/static"))
      .body(requestBody)
      .asObject(StaticVaultResponse.class);

    StaticVaultResponse responseBody = response.getBody();
    SecretKey masterKey = new Security().getAes().generateMasterKey();

    return new StaticVault(
      client,
      responseBody.getId(),
      responseBody.getName(),
      requestBody.getTags(),
      masterKey
    );
  }

  /**
   * Retrieve the static vault from id
   *
   * @param client Client
   * @param vaultId VaultId Vault ID
   * @param masterKey Master Key
   * @return StaticVault
   * @throws Exception Exception
   */
  public static StaticVault retrieveStaticVault(Client client, String vaultId, String masterKey) throws Exception {

    HttpResponse<StaticVaultResponse> response = Unirest
      .get(client.getApi().setUrl("/vault/static/" + vaultId))
      .asObject(StaticVaultResponse.class);

    StaticVaultResponse responseBody = response.getBody();
    byte[] decodedKey = Base64.decode(masterKey);
    SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

    return new StaticVault(
      client,
      responseBody.getId(),
      responseBody.getName(),
      responseBody.getTags(),
      secretKey
    );
  }

  /**
   * Delete the static vault from id
   *
   * @param client Client
   * @param vaultId VaultId Vault ID
   * @return boolean
   * @throws Exception Exception
   */
  public static boolean delete(Client client, String vaultId) throws Exception {
    HttpResponse<String> response = Unirest
      .delete(client.getApi().setUrl("/vault/static/" + vaultId))
      .asString();

    return response.getBody().contains("{\"ok\":true}");
  }

  public String getMasterKey() {
    return new String(Base64.encode(this.secretKey.getEncoded()));
  }

  public AESDTO encrypt(String value) {
    byte[] iv = this.security.getAes().generateIV();
    byte[] byteValue = value.getBytes();

    return this.security.getAes().encrypt(this.secretKey, iv, byteValue);
  }

  public String decrypt(String iv, String authTag, String value) {
    byte[] byteIv = Base64.decode(iv.getBytes());
    byte[] byteAuthTag = Base64.decode(authTag.getBytes());
    byte[] byteValue = Base64.decode(value.getBytes());

    byte[] decrypted = this.security.getAes().decrypt(this.secretKey, byteIv, byteAuthTag, byteValue);
    return new String(decrypted);
  }

  public String hash(String value) {
    return this.security.getHmac().hash(value, this.client.getHashKey());
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Client getClient() {
    return client;
  }

  public List<String> getTags() {
    return tags;
  }

  public AddressManager getAddressManager() {
    return addressManager;
  }

  public DateOfBirthManager getDateOfBirthManager() {
    return dateOfBirthManager;
  }

  public DriversLicenseManager getDriversLicenseManager() {
    return driversLicenseManager;
  }

  public FirstNameManager getFirstNameManager() {
    return firstNameManager;
  }

  public GenderManager getGenderManager() {
    return genderManager;
  }

  public GenericManager getGenericManager() {
    return genericManager;
  }

  public LastNameManager getLastNameManager() {
    return lastNameManager;
  }

  public PassportManager getPassportManager() {
    return passportManager;
  }

  public PlaceOfBirthManager getPlaceOfBirthManager() {
    return placeOfBirthManager;
  }

  public RaceManager getRaceManager() {
    return raceManager;
  }

  public RandomManager getRandomManager() {
    return randomManager;
  }

  public SsnManager getSsnManager() {
    return ssnManager;
  }

  public TaxpayerManager getTaxpayerManager() {
    return taxpayerManager;
  }

  public VehicleRegistrationManager getVehicleRegistrationManager() {
    return vehicleRegistrationManager;
  }
}
