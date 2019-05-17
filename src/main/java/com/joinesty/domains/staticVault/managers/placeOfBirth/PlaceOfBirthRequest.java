package com.joinesty.domains.staticVault.managers.placeOfBirth;

import lombok.Data;

@Data
public class PlaceOfBirthRequest {

    private String placeOfBirth;

    private String iv;

    private String authTag;

    private String[] tags;

}
