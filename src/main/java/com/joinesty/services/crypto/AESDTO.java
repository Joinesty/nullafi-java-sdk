package com.joinesty.services.crypto;

import lombok.Setter;
import org.bouncycastle.util.encoders.Base64;

public class AESDTO {

    @Setter
    private byte[] encryptedData;

    @Setter
    private byte[] initializationVector;

    @Setter
    private byte[] authenticationTag;

    public String getEncryptedData() {
        return new String(Base64.encode(this.encryptedData));
    }

    public String getInitializationVector() {
        return new String(Base64.encode(this.initializationVector));
    }

    public String getAuthenticationTag() {
        return new String(Base64.encode(this.authenticationTag));
    }
}