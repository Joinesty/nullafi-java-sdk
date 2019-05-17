package com.joinesty.domains.staticVault.managers.firstName;

import lombok.Data;

@Data
public class FirstNameRequest {

    private String firstName;

    private String iv;

    private String authTag;

    private String[] tags;

}
