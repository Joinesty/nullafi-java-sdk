package example;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;

public class Example {

    public static void main (String[] args) {
        NullafiSDK nullafiSDK = new NullafiSDK("/33GJOFALkzcEfaLnqHbJLFtVE5DrjkarAg7RdLsfl0=");
        Client client = nullafiSDK.createClient();

        // Create
        StaticVault staticVault = client.createStaticVault("Java Static Vault", new String[1]);
        System.out.println("**** Create Static Vault:");
        System.out.println("-> Id: " + staticVault.getId());
        System.out.println("-> Name: " + staticVault.getName());
        System.out.println("-> MasterKey: " + staticVault.getMasterKey());

        // Retrieve
        /*
        String staticVaultId = "81f2f706-638a-4435-a5dd-58b59e56b2f5";
        String staticVaultMasterKey = "EKOqBQHeGq13g7e7kd7kkZ0fDD65tCvLRXV6ZS7q7lo=";
        StaticVault staticVault = client.retrieveStaticVault(staticVaultId, staticVaultMasterKey);
        System.out.println("**** Retrieve Static Vault:");
        System.out.println("-> Id: " + staticVault.getId());
        System.out.println("-> Name: " + staticVault.getName());
        System.out.println("-> MasterKey: " + staticVault.getMasterKey());
        System.out.println("\n");
         */

    }
}
