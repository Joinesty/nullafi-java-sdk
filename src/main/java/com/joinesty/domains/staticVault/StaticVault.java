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
import lombok.Getter;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * StaticVault Class
 */
public class StaticVault extends API {

    @Getter
    private String id;

    @Getter
    private String name;

    @Getter
    private Client client;


    @Getter
    private AddressManager addressManager;

    @Getter
    private DateOfBirthManager dateOfBirthManager;

    @Getter
    private DriversLicenseManager driversLicenseManager;

    @Getter
    private FirstNameManager firstNameManager;

    @Getter
    private GenderManager genderManager;

    @Getter
    private GenericManager genericManager;

    @Getter
    private LastNameManager lastNameManager;

    @Getter
    private PassportManager passportManager;

    @Getter
    private PlaceOfBirthManager placeOfBirthManager;

    @Getter
    private RaceManager raceManager;

    @Getter
    private RandomManager randomManager;

    @Getter
    private SsnManager ssnManager;

    @Getter
    private TaxpayerManager taxpayerManager;

    @Getter
    private VehicleRegistrationManager vehicleRegistrationManager;


    private SecretKey secretKey;

    private Security security;


    private StaticVault() {

    }

    private StaticVault(Client client, String id, String name, SecretKey secretKey) {
        this.client = client;
        this.security = new Security();
        this.id = id;
        this.name = name;
        this.secretKey = secretKey;

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

    public String getMasterKey() {
        return new String(Base64.encode(this.secretKey.getEncoded()));
    }

    public AESDTO encrypt(String value) {
        byte[] iv = this.security.getAes().generateIV();
        byte[] byteValue = value.getBytes(StandardCharsets.UTF_8);

        return this.security.getAes().encrypt(this.secretKey, iv, byteValue);
    }

    public String decrypt(String iv, String authTag, String value) {
        byte[] byteIv = Base64.decode(iv.getBytes());
        byte[] byteAuthTag = Base64.decode(authTag.getBytes());
        byte[] byteValue = Base64.decode(value.getBytes());

        byte[] decrypted = this.security.getAes().decrypt(this.secretKey, byteIv, byteAuthTag, byteValue);
        return new String(decrypted);
    }

    /**
     *  Request the API to create a new static vault
     *
     * @param client
     * @param name
     * @param tags
     *
     * @return StaticVault
     */
    public static StaticVault createStaticVault(Client client, String name, String[] tags) {
        try {
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
                    masterKey
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve the static vault from id
     *
     * @param client
     * @param vaultId
     * @param masterKey
     *
     * @return StaticVault
     */
    public static StaticVault retrieveStaticVault(Client client, String vaultId, String masterKey) {
        try {
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
                    secretKey
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
