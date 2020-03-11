package com.joinesty.integration.staticVault;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StaticVaultTest {


    @Test
    public void run() throws Exception {
        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        StaticVault created = createStaticVault(client);
        String vaultId = created.getId();
        String vaultMasterKey = created.getMasterKey();

        StaticVault retrieved = retrieveStaticVault(client, vaultId, vaultMasterKey);

        deleteStaticVault(client, vaultId);

        assertEquals(created.getId(), retrieved.getId());
    }

    private StaticVault createStaticVault(Client client) throws Exception {
        String name = "Sample Static Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");
        StaticVault staticVault = client.createStaticVault(name, tags);
        return staticVault;
    }

    private StaticVault retrieveStaticVault(Client client, String id, String masterKey) throws Exception {
        StaticVault staticVault = client.retrieveStaticVault(id, masterKey);
        return staticVault;
    }

    private void deleteStaticVault(Client client, String id) throws Exception {
        client.deleteStaticVault(id);
    }



}
