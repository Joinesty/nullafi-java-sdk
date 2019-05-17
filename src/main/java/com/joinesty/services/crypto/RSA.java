package com.joinesty.services.crypto;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class RSA {

    public KeyPair rsaGenerateEphemeralKeypairs() throws NoSuchAlgorithmException {
        final int keySize = 2048;

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);

        return keyPairGenerator.genKeyPair();
    }

    public byte[] privateDecrypt(PrivateKey key, byte[] text) throws Exception {
        byte[] dectyptedText = null;
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        dectyptedText = cipher.doFinal(text);
        return dectyptedText;

    }
}
