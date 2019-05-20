package com.joinesty.domains.staticVault.managers.race;

import lombok.Data;

@Data
public class RaceRequest {

    private String race;

    private String iv;

    private String authTag;

    private String[] tags;

}
