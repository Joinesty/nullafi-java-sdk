package com.joinesty.integration.staticVault.managers;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.taxpayer.TaxpayerResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TaxpayerTest {
    public void run() throws Exception {
        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        String name = "Sample Static Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");

        StaticVault staticVault = client.createStaticVault(name, tags);
        TaxpayerResponse created = this.create(staticVault);
        TaxpayerResponse retrieved = this.retrieve(staticVault, created.getId());

        this.delete(staticVault, retrieved.getId());

        client.deleteStaticVault(staticVault.getId());

        assertEquals(created.getId(), retrieved.getId());
    }

    private TaxpayerResponse create(StaticVault vault) throws Exception {
        String name = "Taxpayer Example";
        TaxpayerResponse created = vault.getTaxpayerManager().create(name);
         return created;
    }

    private TaxpayerResponse retrieve(StaticVault vault, String id) throws Exception {
        TaxpayerResponse retrieved = vault.getTaxpayerManager().retrieve(id);
        return retrieved;
    }

    private boolean delete(StaticVault vault, String id) throws Exception {
        boolean deleted = vault.getTaxpayerManager().delete(id);

        if (!deleted) throw new RuntimeException("Error when delete Taxpayer");

        return deleted;
    }
}
