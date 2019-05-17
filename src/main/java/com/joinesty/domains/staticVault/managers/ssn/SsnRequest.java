package com.joinesty.domains.staticVault.managers.ssn;

import lombok.Data;

@Data
public class SsnRequest {

    private String ssn;

    private String iv;

    private String authTag;

    private String[] tags;

}
