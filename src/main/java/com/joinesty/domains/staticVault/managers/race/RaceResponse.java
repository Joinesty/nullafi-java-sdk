package com.joinesty.domains.staticVault.managers.race;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * RaceResponse Class
 */
@Data
public class RaceResponse {

    private String id;

    private String raceToken;

    private String race;

    private String iv;

    private String authTag;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

    @JsonFormat
    private Date updatedAt;

}
