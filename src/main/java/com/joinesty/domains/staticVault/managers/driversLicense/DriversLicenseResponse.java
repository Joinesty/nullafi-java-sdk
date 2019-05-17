package com.joinesty.domains.staticVault.managers.driversLicense;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DriversLicenseResponse {

    private String id;

    private String driversLicenseToken;

    private String driversLicense;

    private String iv;

    private String authTag;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

    @JsonFormat
    private Date updatedAt;

}
