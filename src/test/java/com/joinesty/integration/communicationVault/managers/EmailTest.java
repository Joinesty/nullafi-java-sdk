package com.joinesty.integration.communicationVault.managers;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.communicationVault.CommunicationVault;
import com.joinesty.domains.communicationVault.managers.email.EmailResponse;
import com.joinesty.domains.staticVault.StaticVault;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EmailTest {

    @Test
    public void run() throws Exception {

        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        String name = "Sample Communication Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");
        CommunicationVault communicationVault = client.createCommunicationVault(name, tags);

        EmailResponse created = this.create(communicationVault);
        EmailResponse retrieved = this.retrieve(communicationVault, created.getId());

        this.delete(communicationVault, retrieved.getId());
        client.deleteCommunicationVault(communicationVault.getId());

        assertEquals(created.getId(), retrieved.getId());
    }

    private EmailResponse create(CommunicationVault vault) throws Exception {
        String name = "email@domain.com";
        EmailResponse created = vault.getEmailManager().create(name);
        return created;
    }

    private EmailResponse retrieve(CommunicationVault vault, String id) throws Exception {
        EmailResponse retrieved = vault.getEmailManager().retrieve(id);
        return retrieved;
    }

    private boolean delete(CommunicationVault vault, String id) throws Exception {
        boolean deleted = vault.getEmailManager().delete(id);
        return deleted;
    }
}
