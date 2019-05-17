package com.joinesty.domains.staticVault.managers.firstName;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FirstNameResponse {

    private String id;

    private String firstnameToken;

    private String firstname;

    private String iv;

    private String authTag;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

    @JsonFormat
    private Date updatedAt;

}
