package com.joinesty.services.crypto;

import java.security.SecureRandom;

import javax.crypto.SecretKey;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;

public class AESGCM {

    public final int AUTH_TAG_BIT_LENGTH = 128;

    public final int SECRET_KEY_BIT_LENGTH = 256;

    public final int IV_BIT_LENGTH = 128;


    public byte[] generateIV() {

        final SecureRandom randomGen = new SecureRandom();
        byte[] bytes = new byte[IV_BIT_LENGTH / 8];
        randomGen.nextBytes(bytes);
        return bytes;
    }

    public SecretKey generateMasterKey() {
        final SecureRandom randomGen = new SecureRandom();
        SecretKey secretKey = AES.generateKey(SECRET_KEY_BIT_LENGTH, randomGen);
        return secretKey;
    }

    private GCMBlockCipher createAESGCMCipher(final SecretKey secretKey,
                                              final boolean forEncryption,
                                              final byte[] iv) {

        // Initialize AES cipher
        BlockCipher cipher = AES.createCipher(secretKey, forEncryption);

        // Create GCM cipher with AES
        GCMBlockCipher gcm = new GCMBlockCipher(cipher);

        AEADParameters aeadParams = new AEADParameters(
                new KeyParameter(secretKey.getEncoded()),
                AUTH_TAG_BIT_LENGTH,
                iv
        );

        gcm.init(forEncryption, aeadParams);

        return gcm;
    }

    public AESDTO encrypt(final SecretKey secretKey, final byte[] iv, final byte[] plainText) {

        // Initialise AES/GCM cipher for encryption
        GCMBlockCipher cipher = createAESGCMCipher(secretKey, true, iv);

        // Prepare output buffer
        int outputLength = cipher.getOutputSize(plainText.length);
        byte[] output = new byte[outputLength];

        // Produce cipher text
        int outputOffset = cipher.processBytes(plainText, 0, plainText.length, output, 0);

        // Produce authentication tag
        try {
            outputOffset += cipher.doFinal(output, outputOffset);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException("Couldn't generate GCM authentication tag: " + e.getMessage(), e);
        }

        // Split output into cipher text and authentication tag
        int authTagLength = AUTH_TAG_BIT_LENGTH / 8;

        byte[] cipherText = new byte[outputOffset - authTagLength];
        byte[] authTag = new byte[authTagLength];

        System.arraycopy(output, 0, cipherText, 0, cipherText.length);
        System.arraycopy(output, outputOffset - authTagLength, authTag, 0, authTag.length);

        AESDTO aesDTO = new AESDTO();
        aesDTO.setEncryptedData(cipherText);
        aesDTO.setAuthenticationTag(authTag);
        aesDTO.setInitializationVector(iv);

        return aesDTO;

    }

    public byte[] decrypt(final SecretKey secretKey, final byte[] iv, final byte[] authTag, final byte[] cipherText) {

        // Initialise AES/GCM cipher for decryption
        GCMBlockCipher cipher = createAESGCMCipher(secretKey, false, iv);

        // Join cipher text and authentication tag to produce cipher input
        byte[] input = new byte[cipherText.length + authTag.length];

        System.arraycopy(cipherText, 0, input, 0, cipherText.length);
        System.arraycopy(authTag, 0, input, cipherText.length, authTag.length);

        int outputLength = cipher.getOutputSize(input.length);

        byte[] output = new byte[outputLength];

        // Decrypt
        int outputOffset = cipher.processBytes(input, 0, input.length, output, 0);

        // Validate authentication tag
        try {
            outputOffset += cipher.doFinal(output, outputOffset);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException("Couldn't validate GCM authentication tag: " + e.getMessage(), e);
        }
        return output;
    }
}