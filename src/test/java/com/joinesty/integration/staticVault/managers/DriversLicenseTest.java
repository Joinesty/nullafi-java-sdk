package com.joinesty.integration.staticVault.managers;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.driversLicense.DriversLicenseResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DriversLicenseTest {

    public void run() throws Exception {
        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        String name = "Sample Static Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");

        StaticVault staticVault = client.createStaticVault(name, tags);

        DriversLicenseResponse created = this.create(staticVault);
        DriversLicenseResponse retrieved = this.retrieve(staticVault, created.getId());
        this.delete(staticVault, retrieved.getId());

        DriversLicenseResponse createdWithState = this.createWithState(staticVault);
        DriversLicenseResponse retrievedWithState = this.retrieve(staticVault, createdWithState.getId());
        this.delete(staticVault, retrievedWithState.getId());

        client.deleteStaticVault(staticVault.getId());

        assertEquals(created.getId(), retrieved.getId());
        assertEquals(createdWithState.getId(), retrievedWithState.getId());
    }

    private DriversLicenseResponse create(StaticVault vault) throws Exception {
        String name = "DriversLicense Example";
        DriversLicenseResponse created = vault.getDriversLicenseManager().create(name);
        return created;
    }

    private DriversLicenseResponse createWithState(StaticVault vault) throws Exception {
        String name = "DriversLicense With State Example";
        String state = "IL";

        DriversLicenseResponse created = vault.getDriversLicenseManager().create(name, state);
        return created;
    }

    private DriversLicenseResponse retrieve(StaticVault vault, String id) throws Exception {
        DriversLicenseResponse retrieved = vault.getDriversLicenseManager().retrieve(id);
        return retrieved;
    }

    private boolean delete(StaticVault vault, String id) throws Exception {
        boolean deleted = vault.getDriversLicenseManager().delete(id);

        if (!deleted) throw new RuntimeException("Error when delete DriversLicense");

        return deleted;
    }
}
