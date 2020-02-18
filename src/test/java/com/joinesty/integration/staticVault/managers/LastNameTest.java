package com.joinesty.integration.staticVault.managers;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.lastName.LastNameResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LastNameTest {
    @Test
   public void run() throws Exception {

        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        String name = "Sample Static Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");

        StaticVault staticVault = client.createStaticVault(name, tags);

        LastNameResponse created = this.create(staticVault);
        LastNameResponse retrieved = this.retrieve(staticVault, created.getId());
        this.delete(staticVault, retrieved.getId());


        LastNameResponse createdWithGender = this.createWithGender(staticVault);
        LastNameResponse retrievedWithGender = this.retrieve(staticVault, createdWithGender.getId());
        this.delete(staticVault, retrievedWithGender.getId());

        client.deleteStaticVault(staticVault.getId());

        assertEquals(created.getId(), retrieved.getId());
    }

    private LastNameResponse create(StaticVault vault) throws Exception {
        String name = "LastName Example";

        LastNameResponse created = vault.getLastNameManager().create(name);
        return created;
    }

    private LastNameResponse createWithGender(StaticVault vault) throws Exception {
        String name = "LastName With Gender Example";
        String gender = "female";

        LastNameResponse created = vault.getLastNameManager().create(name, gender);
        return created;
    }

    private LastNameResponse retrieve(StaticVault vault, String id) throws Exception {
        LastNameResponse retrieved = vault.getLastNameManager().retrieve(id);
        return retrieved;
    }

    private boolean delete(StaticVault vault, String id) throws Exception {
        boolean deleted = vault.getLastNameManager().delete(id);

        if (!deleted) throw new RuntimeException("Error when delete LastName");

        return deleted;
    }
}
