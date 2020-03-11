package com.joinesty.integration.staticVault.managers;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.ssn.SsnResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SsnTest {
    @Test
    public void run() throws Exception {
        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        String name = "Sample Static Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");

        StaticVault staticVault = client.createStaticVault(name, tags);
        SsnResponse created = this.create(staticVault);
        SsnResponse retrieved = this.retrieve(staticVault, created.getId());

        this.delete(staticVault, retrieved.getId());

        SsnResponse createdWithState = this.createWithState(staticVault);
        SsnResponse retrievedWithState = this.retrieve(staticVault, createdWithState.getId());

        this.delete(staticVault, retrievedWithState.getId());

        client.deleteStaticVault(staticVault.getId());

        assertEquals(created.getId(), retrieved.getId());
    }

    private SsnResponse create(StaticVault vault) throws Exception {
        String name = "Ssn Example";
        SsnResponse created = vault.getSsnManager().create(name);
        return created;
    }

    private SsnResponse createWithState(StaticVault vault) throws Exception {
        String name = "Ssn With State Example";
        String state = "IL";

        SsnResponse created = vault.getSsnManager().create(name, state);
        return created;
    }

    private SsnResponse retrieve(StaticVault vault, String id) throws Exception {
        SsnResponse retrieved = vault.getSsnManager().retrieve(id);
        return retrieved;
    }

    private boolean delete(StaticVault vault, String id) throws Exception {
        boolean deleted = vault.getSsnManager().delete(id);

        if (!deleted) throw new RuntimeException("Error when delete SSN");

        return deleted;
    }
}
