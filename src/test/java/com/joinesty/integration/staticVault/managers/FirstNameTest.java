package com.joinesty.integration.staticVault.managers;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.firstName.FirstNameResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FirstNameTest {

    @Test
    public void run() throws Exception {
        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        String name = "Sample Static Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");

        StaticVault staticVault = client.createStaticVault(name, tags);

        FirstNameResponse created = this.create(staticVault);
        FirstNameResponse retrieved = this.retrieve(staticVault, created.getId());
        this.delete(staticVault, retrieved.getId());

        FirstNameResponse createdWithGender = this.createWithGender(staticVault);
        FirstNameResponse retrievedWithGender = this.retrieve(staticVault, createdWithGender.getId());
        this.delete(staticVault, retrievedWithGender.getId());

        client.deleteStaticVault(staticVault.getId());

        assertEquals(created.getId(), retrieved.getId());
        assertEquals(createdWithGender.getId(), retrievedWithGender.getId());
    }

    private FirstNameResponse create(StaticVault vault) throws Exception {
        String name = "FirstName Example";
        FirstNameResponse created = vault.getFirstNameManager().create(name);
        return created;
    }

    private FirstNameResponse createWithGender(StaticVault vault) throws Exception {
        String name = "FirstName With Gender Example";
        String gender = "male";
        FirstNameResponse created = vault.getFirstNameManager().create(name, gender);
        return created;
    }

    private FirstNameResponse retrieve(StaticVault vault, String id) throws Exception {
        FirstNameResponse retrieved = vault.getFirstNameManager().retrieve(id);
        return retrieved;

    }

    private boolean delete(StaticVault vault, String id) throws Exception {
        boolean deleted = vault.getFirstNameManager().delete(id);

        if (!deleted) throw new RuntimeException("Error when delete FirstName");

        return deleted;
    }
}
