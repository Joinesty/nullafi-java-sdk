package com.joinesty.domains.staticVault.managers.dateOfBirth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DateOfBirthResponse {

    private String id;

    private String dateofbirthToken;

    private String dateofbirth;

    private String iv;

    private String authTag;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

    @JsonFormat
    private Date updatedAt;

}
