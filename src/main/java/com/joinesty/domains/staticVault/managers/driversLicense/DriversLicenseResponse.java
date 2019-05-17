package com.joinesty.domains.staticVault.managers.driversLicense;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DriversLicenseResponse {

    private String id;

    private String driverslicenseToken;

    private String driverslicense;

    private String iv;

    private String authTag;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

    @JsonFormat
    private Date updatedAt;

}
