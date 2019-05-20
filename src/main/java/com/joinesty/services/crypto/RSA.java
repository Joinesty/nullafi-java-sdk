package com.joinesty.services.crypto;

import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

public class RSA {

    public RSA() { }

    public KeyPair generateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair key = keyGen.generateKeyPair();
        return key;
    }

    public byte[] encrypt(Key key, byte[] text) throws Exception {
        byte[] cipherText = null;
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");

        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(text);
        return cipherText;
    }

    public byte[] decrypt(Key key, byte[] text) throws Exception {
        byte[] dectyptedText = null;
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        dectyptedText = cipher.doFinal(text);
        return dectyptedText;
    }

    public String toPEM(byte[] encoded) throws IOException {
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        PemObject pemObject = new PemObject("PUBLIC KEY", encoded);
        pemWriter.writeObject(pemObject);
        pemWriter.flush();
        return stringWriter.toString();
    }

    public  PublicKey getPemPublicKey(String pem) throws Exception {
        String publicKeyPEM = pem.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");

        byte [] decoded = Base64.decode(publicKeyPEM);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("AES");
        return kf.generatePublic(spec);
    }

}
