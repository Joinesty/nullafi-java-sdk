package com.joinesty.domains.staticVault.managers.dateOfBirth;

import lombok.Data;

@Data
public class DateOfBirthRequest {

    private String dateOfBirth;

    private String iv;

    private String authTag;

    private String[] tags;

}
