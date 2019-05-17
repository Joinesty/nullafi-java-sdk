package com.joinesty.domains.staticVault.managers.placeOfBirth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PlaceOfBirthResponse {

    private String id;

    private String placeOfBirthToken;

    private String placeOfBirth;

    private String iv;

    private String authTag;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

    @JsonFormat
    private Date updatedAt;

}
