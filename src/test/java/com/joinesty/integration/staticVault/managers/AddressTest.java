package com.joinesty.integration.staticVault.managers;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.address.AddressResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AddressTest {

    @Test
    public void run() throws Exception {
        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        String name = "Sample Static Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");

        StaticVault staticVault = client.createStaticVault(name, tags);

        AddressResponse created = this.create(staticVault);
        AddressResponse retrieved = this.retrieve(staticVault, created.getId());

        this.delete(staticVault, retrieved.getId());
        AddressResponse createdWithState = this.createWithState(staticVault);
        AddressResponse retrievedWithState = this.retrieve(staticVault, createdWithState.getId());
        this.delete(staticVault, retrievedWithState.getId());

        client.deleteStaticVault(staticVault.getId());

        assertEquals(created.getId(), retrieved.getId());
        assertEquals(createdWithState.getId(), retrievedWithState.getId());
    }

    private AddressResponse create(StaticVault vault) throws Exception {
        String name = "Address Example";
        AddressResponse created = vault.getAddressManager().create(name);
        return created;
    }

    private AddressResponse createWithState(StaticVault vault) throws Exception {
        String name = "Address With State Example";
        String state = "IL";

        AddressResponse created = vault.getAddressManager().create(name, state);
        return created;
    }

    private AddressResponse retrieve(StaticVault vault, String id) throws Exception {
        AddressResponse retrieved = vault.getAddressManager().retrieve(id);
        return retrieved;
    }

    private boolean delete(StaticVault vault, String id) throws Exception {
        boolean deleted = vault.getAddressManager().delete(id);
        return deleted;
    }
}
