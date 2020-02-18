package com.joinesty.integration.staticVault.managers;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.generic.GenericResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GenericTest {
    @Test
    public void run() throws Exception {
        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        String name = "Sample Static Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");

        StaticVault staticVault = client.createStaticVault(name, tags);

        GenericResponse created = this.create(staticVault);
        GenericResponse retrieved = this.retrieve(staticVault, created.getId());
        this.delete(staticVault, retrieved.getId());

        client.deleteStaticVault(staticVault.getId());

        assertEquals(created.getId(), retrieved.getId());
    }

    private GenericResponse create(StaticVault vault) throws Exception {
        String name = "Generic Example";
        String template = "[A-Z]{4}";

        GenericResponse created = vault.getGenericManager().create(name, template);

        System.out.println("**** GenericExample.create:");
        System.out.println("-> Original: " + name);
        System.out.println("-> " + created.toString());
        System.out.println("\n");

        return created;
    }

    private GenericResponse retrieve(StaticVault vault, String id) throws Exception {
        GenericResponse retrieved = vault.getGenericManager().retrieve(id);

        System.out.println("**** GenericExample.retrieve:");
        System.out.println("-> " + retrieved.toString());
        System.out.println("\n");

        return retrieved;

    }

    private boolean delete(StaticVault vault, String id) throws Exception {
        boolean deleted = vault.getGenericManager().delete(id);

        System.out.println("**** GenericExample.delete:");
        System.out.println("-> Success: " + deleted);
        System.out.println("\n");

        if (!deleted) throw new RuntimeException("Error when delete Generic");

        return deleted;
    }
}
