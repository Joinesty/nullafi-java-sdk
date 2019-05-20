package com.joinesty.domains.staticVault.managers.taxpayer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * TaxpayerResponse Class
 */
@Data
public class TaxpayerResponse {

    private String id;

    private String taxpayerToken;

    private String taxpayer;

    private String iv;

    private String authTag;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

    @JsonFormat
    private Date updatedAt;

}
