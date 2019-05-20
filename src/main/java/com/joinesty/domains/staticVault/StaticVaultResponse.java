package com.joinesty.domains.staticVault;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class StaticVaultResponse {

    private String id;

    private String name;

    private String[] tags;

    @JsonFormat
    private Date createdAt;

}
