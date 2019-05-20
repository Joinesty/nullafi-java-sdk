package com.joinesty.domains.staticVault.managers.gender;

import lombok.Data;

@Data
public class GenderRequest {

    private String gender;

    private String iv;

    private String authTag;

    private String[] tags;

}
