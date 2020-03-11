package com.joinesty.integration.staticVault.managers;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.gender.GenderResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GenderTest {

    public void run() throws Exception {
        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        String name = "Sample Static Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");

        StaticVault staticVault = client.createStaticVault(name, tags);

        GenderResponse created = this.create(staticVault);
        GenderResponse retrieved = this.retrieve(staticVault, created.getId());
        this.delete(staticVault, retrieved.getId());

        client.deleteStaticVault(staticVault.getId());

        assertEquals(created.getId(), retrieved.getId());
    }

    private GenderResponse create(StaticVault vault) throws Exception {
        String name = "male";
        GenderResponse created = vault.getGenderManager().create(name);
        return created;
    }

    private GenderResponse retrieve(StaticVault vault, String id) throws Exception {
        GenderResponse retrieved = vault.getGenderManager().retrieve(id);
        return retrieved;

    }

    private boolean delete(StaticVault vault, String id) throws Exception {
        boolean deleted = vault.getGenderManager().delete(id);

        if (!deleted) throw new RuntimeException("Error when delete Gender");

        return deleted;
    }
}
