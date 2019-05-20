package com.joinesty.services;

import com.joinesty.services.crypto.AESGCM;
import com.joinesty.services.crypto.RSA;
import lombok.Getter;

public class Security {

    @Getter
    private AESGCM aes;

    @Getter
    private RSA rsa;

    public Security() {
        aes = new AESGCM();
        rsa = new RSA();
    }

}
