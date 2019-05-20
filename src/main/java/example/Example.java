package example;

import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.communicationVault.CommunicationVault;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.address.AddressManager;
import com.joinesty.domains.staticVault.managers.address.AddressResponse;

public class Example {

    public static void main (String[] args) {
        NullafiSDK nullafiSDK = new NullafiSDK("/33GJOFALkzcEfaLnqHbJLFtVE5DrjkarAg7RdLsfl0=");
        Client client = nullafiSDK.createClient();

        // Create Static Vault
        /*
        StaticVault staticVault = client.createStaticVault("Java Static Vault", new String[1]);
        System.out.println("**** Create Static Vault:");
        System.out.println("-> Id: " + staticVault.getId());
        System.out.println("-> Name: " + staticVault.getName());
        System.out.println("-> MasterKey: " + staticVault.getMasterKey());
         */

        // Retrieve Static Vault
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


        // Create Communication Vault
        /*
        CommunicationVault communicationVault = client.createCommunicationVault("Java Communication Vault", new String[1]);
        System.out.println("**** Create Communication Vault:");
        System.out.println("-> Id: " + communicationVault.getId());
        System.out.println("-> Name: " + communicationVault.getName());
        System.out.println("-> MasterKey: " + communicationVault.getMasterKey());
         */


        // Retrieve Communication Vault
        /*
        String communicationVaultId = "55aff3b3-71fe-47ba-a4b3-eafa8a853796";
        String communicationVaultMasterKey = "TkszQklzbkhmUlVhdk9BMmdUcDUvZ2ZWc3VERzlVcUhrbHc0d1daZ1QvOD0=";
        CommunicationVault communicationVault = client.retrieveCommunicationVault(communicationVaultId, communicationVaultMasterKey);
        System.out.println("**** Retrieve Communication Vault:");
        System.out.println("-> Id: " + communicationVault.getId());
        System.out.println("-> Name: " + communicationVault.getName());
        System.out.println("-> MasterKey: " + communicationVault.getMasterKey());
        System.out.println("\n");
        */

    }
}
