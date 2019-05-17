package com.joinesty.domains.staticVault.managers.passport;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * PassportResponse Class
 */
@Data
public class PassportResponse {

    private String id;

    private String passportToken;

    private String passport;

    private String iv;

    private String authTag;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

    @JsonFormat
    private Date updatedAt;

}
