package com.joinesty.domains.staticVault.managers.gender;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.util.Date;

/**
 * GenderResponse Class
 */
@Data
public class GenderResponse {

    private String id;

    private String genderToken;

    private String gender;

    private String iv;

    private String authTag;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

    @JsonFormat
    private Date updatedAt;

}
