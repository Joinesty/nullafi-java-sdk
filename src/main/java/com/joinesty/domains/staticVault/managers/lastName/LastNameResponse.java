package com.joinesty.domains.staticVault.managers.lastName;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class LastNameResponse {

    private String id;

    private String lastNameToken;

    private String lastName;

    private String iv;

    private String authTag;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

    @JsonFormat
    private Date updatedAt;

}
