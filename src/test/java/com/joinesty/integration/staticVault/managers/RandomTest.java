package com.joinesty.integration.staticVault.managers;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.random.RandomResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RandomTest {
    @Test
    public void run() throws Exception {
        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        String name = "Sample Static Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");

        StaticVault staticVault = client.createStaticVault(name, tags);
        RandomResponse created = this.create(staticVault);
        RandomResponse retrieved = this.retrieve(staticVault, created.getId());
        String retrievedId = retrieved.getId();

        this.delete(staticVault, retrievedId);

        client.deleteStaticVault(staticVault.getId());

        assertEquals(created.getId(), retrieved.getId());
    }

    private RandomResponse create(StaticVault vault) throws Exception {
        String name = "Random Example";
        RandomResponse created = vault.getRandomManager().create(name);
        return created;
    }

    private RandomResponse retrieve(StaticVault vault, String id) throws Exception {
        RandomResponse retrieved = vault.getRandomManager().retrieve(id);
        return retrieved;
    }

    private boolean delete(StaticVault vault, String id) throws Exception {
        boolean deleted = vault.getRandomManager().delete(id);

        if (!deleted) throw new RuntimeException("Error when delete Random");

        return deleted;
    }
}
