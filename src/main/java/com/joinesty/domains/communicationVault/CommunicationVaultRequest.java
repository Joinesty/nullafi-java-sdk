package com.joinesty.domains.communicationVault;

import lombok.Data;

@Data
public class CommunicationVaultRequest {

    private String name;

    private String publicKey;

    private String[] tags;

}
