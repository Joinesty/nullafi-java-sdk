package com.joinesty.domains.staticVault.managers.vehicleRegistration;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class VehicleRegistrationResponse {

    private String id;

    private String vehicleRegistrationToken;

    private String vehicleRegistration;

    private String iv;

    private String authTag;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

    @JsonFormat
    private Date updatedAt;

}
