package com.joinesty.domains.staticVault.managers.random;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RandomResponse {

    private String id;

    private String token;

    private String data;

    private String iv;

    private String authTag;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

    @JsonFormat
    private Date updatedAt;

}
