package com.joinesty.domains.staticVault.managers.generic;

import lombok.Data;

@Data
public class GenericRequest {

    private String data;

    private String template;

    private String iv;

    private String authTag;

    private String[] tags;

}
