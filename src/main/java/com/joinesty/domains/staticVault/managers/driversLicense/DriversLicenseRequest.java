package com.joinesty.domains.staticVault.managers.driversLicense;

import lombok.Data;

@Data
public class DriversLicenseRequest {

    private String driverslicense;

    private String iv;

    private String authTag;

    private String[] tags;

}
