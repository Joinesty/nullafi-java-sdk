package com.joinesty.domains.communicationVault;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CommunicationVaultResponse {

    private String id;

    private String name;

    private String[] tags;

    private String sessionKey;

    private String iv;

    private String authTag;

    private String masterKey;

    @JsonFormat
    private Date createdAt;

}
