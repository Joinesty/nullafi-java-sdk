package com.joinesty.domains.staticVault.managers.vehicleRegistration;

import lombok.Data;

@Data
public class VehicleRegistrationRequest {

    private String vehicleRegistration;

    private String iv;

    private String authTag;

    private String[] tags;

}
