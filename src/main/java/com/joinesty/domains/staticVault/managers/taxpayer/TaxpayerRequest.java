package com.joinesty.domains.staticVault.managers.taxpayer;

import lombok.Data;

@Data
public class TaxpayerRequest {

    private String taxpayer;

    private String iv;

    private String authTag;

    private String[] tags;

}
