package com.joinesty.domains.staticVault.managers.address;

import lombok.Data;

@Data
public class AddressRequest {

    private String address;

    private String iv;

    private String authTag;

    private String[] tags;

}
