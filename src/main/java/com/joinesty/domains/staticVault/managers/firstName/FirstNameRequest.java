package com.joinesty.domains.staticVault.managers.firstName;

import lombok.Data;

@Data
public class FirstNameRequest {

    private String firstname;

    private String iv;

    private String authTag;

    private String[] tags;

}
