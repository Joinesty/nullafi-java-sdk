package com.joinesty.integration.staticVault.managers;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.vehicleRegistration.VehicleRegistrationResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class VehicleRegistrationTest {
    @Test
    public void run() throws Exception {
        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        String name = "Sample Static Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");

        StaticVault staticVault = client.createStaticVault(name, tags);
        VehicleRegistrationResponse created = this.create(staticVault);
        VehicleRegistrationResponse retrieved = this.retrieve(staticVault, created.getId());
        this.delete(staticVault, retrieved.getId());

        client.deleteStaticVault(staticVault.getId());

        assertEquals(created.getId(), retrieved.getId());
    }

    private VehicleRegistrationResponse create(StaticVault vault) throws Exception {
        String name = "VehicleRegistration Example";
        VehicleRegistrationResponse created = vault.getVehicleRegistrationManager().create(name);
        return created;
    }

    private VehicleRegistrationResponse retrieve(StaticVault vault, String id) throws Exception {
        VehicleRegistrationResponse retrieved = vault.getVehicleRegistrationManager().retrieve(id);
        return retrieved;
    }

    private boolean delete(StaticVault vault, String id) throws Exception {
        boolean deleted = vault.getVehicleRegistrationManager().delete(id);

        if (!deleted) throw new RuntimeException("Error when delete VehicleRegistration");

        return deleted;
    }
}
