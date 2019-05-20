package com.joinesty.domains.staticVault.managers.vehicleRegistration;

import lombok.Data;

@Data
public class VehicleRegistrationRequest {

    private String vehicleregistration;

    private String iv;

    private String authTag;

    private String[] tags;

}
