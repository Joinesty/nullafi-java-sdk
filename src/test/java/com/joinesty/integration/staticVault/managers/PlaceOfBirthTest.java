package com.joinesty.integration.staticVault.managers;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.placeOfBirth.PlaceOfBirthResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PlaceOfBirthTest {
    @Test
    public void run() throws Exception {
        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        String name = "Sample Static Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");

        StaticVault staticVault = client.createStaticVault(name, tags);

        PlaceOfBirthResponse created = this.create(staticVault);
        PlaceOfBirthResponse retrieved = this.retrieve(staticVault, created.getId());
        this.delete(staticVault, retrieved.getId());

        PlaceOfBirthResponse createdWithState = this.createWithState(staticVault);
        PlaceOfBirthResponse retrievedWithState = this.retrieve(staticVault, createdWithState.getId());
        this.delete(staticVault, retrievedWithState.getId());

        client.deleteStaticVault(staticVault.getId());

        assertEquals(created.getId(), retrieved.getId());
    }

    private PlaceOfBirthResponse create(StaticVault vault) throws Exception {
        String name = "PlaceOfBirth Example";
        PlaceOfBirthResponse created = vault.getPlaceOfBirthManager().create(name);
        return created;
    }

    private PlaceOfBirthResponse createWithState(StaticVault vault) throws Exception {
        String name = "PlaceOfBirth With State Example";
        String state = "IL";

        PlaceOfBirthResponse created = vault.getPlaceOfBirthManager().create(name, state);
        return created;
    }

    private PlaceOfBirthResponse retrieve(StaticVault vault, String id) throws Exception {
        PlaceOfBirthResponse retrieved = vault.getPlaceOfBirthManager().retrieve(id);
        return retrieved;
    }

    private boolean delete(StaticVault vault, String id) throws Exception {
        boolean deleted = vault.getPlaceOfBirthManager().delete(id);
        return deleted;
    }
}
