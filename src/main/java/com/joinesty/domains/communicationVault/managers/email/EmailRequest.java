package com.joinesty.domains.communicationVault.managers.email;

import lombok.Data;

@Data
public class EmailRequest {

    private String email;

    private String iv;

    private String authTag;

    private String[] tags;

}
