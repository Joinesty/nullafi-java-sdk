package com.joinesty.domains.staticVault.managers.passport;

import lombok.Data;

@Data
public class PassportRequest {

    private String passport;

    private String iv;

    private String authTag;

    private String[] tags;

}
