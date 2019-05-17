package com.joinesty.domains.staticVault.managers.random;

import lombok.Data;

@Data
public class RandomRequest {

    private String data;

    private String iv;

    private String authTag;

    private String[] tags;

}
