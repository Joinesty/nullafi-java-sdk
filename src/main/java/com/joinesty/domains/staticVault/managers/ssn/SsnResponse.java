package com.joinesty.domains.staticVault.managers.ssn;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * SsnResponse Class
 */
@Data
public class SsnResponse {

    private String id;

    private String ssnToken;

    private String ssn;

    private String iv;

    private String authTag;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

    @JsonFormat
    private Date updatedAt;

}
