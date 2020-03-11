package com.joinesty.integration.communicationVault;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.communicationVault.CommunicationVault;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommunicationVaultTest {


    @Test
    public void run() throws Exception {
        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        CommunicationVault created = createCommunicationVault(client);
        String vaultId = created.getId();
        String vaultMasterKey = created.getMasterKey();

        CommunicationVault retrieved = retrieveCommunicationVault(client, vaultId, vaultMasterKey);

        deleteCommunicationVault(client, vaultId);

        assertEquals(created.getId(), retrieved.getId());
    }

    private CommunicationVault createCommunicationVault(Client client) throws Exception {
        String name = "Sample Communication Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");
        CommunicationVault communicationVault = client.createCommunicationVault(name, tags);
        return communicationVault;
    }

    private CommunicationVault retrieveCommunicationVault(Client client, String id, String masterKey) throws Exception {
        CommunicationVault communicationVault = client.retrieveCommunicationVault(id, masterKey);
        return communicationVault;
    }

    private void deleteCommunicationVault(Client client, String id) throws Exception {
        client.deleteCommunicationVault(id);
    }



}
