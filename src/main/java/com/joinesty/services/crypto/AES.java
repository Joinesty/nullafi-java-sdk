package com.joinesty.services.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.params.KeyParameter;

public class AES {

    public static KeyGenerator createKeyGenerator() {
        try {
            return KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static SecretKey generateKey(final int keyBitLength, final SecureRandom random) {
        KeyGenerator aesKeyGenerator = createKeyGenerator();
        aesKeyGenerator.init(keyBitLength, random);
        return aesKeyGenerator.generateKey();
    }

    public static AESEngine createCipher(final SecretKey secretKey,
                                         final boolean forEncryption) {
        AESEngine cipher = new AESEngine();
        CipherParameters cipherParams = new KeyParameter(secretKey.getEncoded());
        cipher.init(forEncryption, cipherParams);
        return cipher;
    }

}