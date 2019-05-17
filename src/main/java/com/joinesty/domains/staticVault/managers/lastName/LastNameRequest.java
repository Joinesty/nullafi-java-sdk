package com.joinesty.domains.staticVault.managers.lastName;

import lombok.Data;

@Data
public class LastNameRequest {

    private String lastName;

    private String iv;

    private String authTag;

    private String[] tags;

}
