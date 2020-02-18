package com.joinesty.integration.staticVault.managers;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.dateOfBirth.DateOfBirthResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DateOfBirthTest {

    @Test
    public void run() throws Exception {
        NullafiSDK sdk = new NullafiSDK(System.getenv("API_KEY"));
        Client client = sdk.createClient();

        String name = "Sample Static Vault Name";
        List tags = new ArrayList<String>();
        tags.add("tag-1");

        StaticVault staticVault = client.createStaticVault(name, tags);

        DateOfBirthResponse created = this.create(staticVault);
        DateOfBirthResponse retrieved = this.retrieve(staticVault, created.getId());
        this.delete(staticVault, retrieved.getId());

        DateOfBirthResponse createdWithYear = this.createWithYear(staticVault);
        DateOfBirthResponse retrievedWithYear = this.retrieve(staticVault, createdWithYear.getId());
        this.delete(staticVault, retrievedWithYear.getId());

        DateOfBirthResponse createdWithMonth = this.createWithMonth(staticVault);
        DateOfBirthResponse retrievedWithMonth = this.retrieve(staticVault, createdWithMonth.getId());
        this.delete(staticVault, retrievedWithMonth.getId());

        DateOfBirthResponse createdWithYearMonth = this.createWithYearMonth(staticVault);
        DateOfBirthResponse retrievedWithYearMonth = this.retrieve(staticVault, createdWithYearMonth.getId());
        this.delete(staticVault, retrievedWithYearMonth.getId());

        client.deleteStaticVault(staticVault.getId());

        assertEquals(created.getId(), retrieved.getId());
        assertEquals(createdWithYear.getId(), retrievedWithYear.getId());
        assertEquals(createdWithMonth.getId(), retrievedWithMonth.getId());
        assertEquals(createdWithYearMonth.getId(), retrievedWithYearMonth.getId());
    }

    private DateOfBirthResponse create(StaticVault vault) throws Exception {
        String name = "DateOfBirth Example";
        DateOfBirthResponse created = vault.getDateOfBirthManager().create(name);
        return created;
    }

    private DateOfBirthResponse createWithYear(StaticVault vault) throws Exception {
        String name = "DateOfBirth With Year Example";
        Integer year = 1990;

        DateOfBirthResponse created = vault.getDateOfBirthManager().create(name, year, null);
        return created;
    }

    private DateOfBirthResponse createWithMonth(StaticVault vault) throws Exception {
        String name = "DateOfBirth With Month Example";
        Integer month = 1;

        DateOfBirthResponse created = vault.getDateOfBirthManager().create(name, null, month);
        return created;
    }

    private DateOfBirthResponse createWithYearMonth(StaticVault vault) throws Exception {
        String name = "DateOfBirth With Year and Month Example";
        Integer year = 1990;
        Integer month = 1;

        DateOfBirthResponse created = vault.getDateOfBirthManager().create(name, year, month);
        return created;
    }

    private DateOfBirthResponse retrieve(StaticVault vault, String id) throws Exception {
        DateOfBirthResponse retrieved = vault.getDateOfBirthManager().retrieve(id);
        return retrieved;
    }

    private boolean delete(StaticVault vault, String id) throws Exception {
        boolean deleted = vault.getDateOfBirthManager().delete(id);

        if (!deleted) throw new RuntimeException("Error when delete DateOfBirth");

        return deleted;
    }
}
