package com.joinesty.services.crypto;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import javax.crypto.Cipher;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class RSA {

  public RSA() {
  }

  public KeyPair generateKey() {
    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
      keyGen.initialize(2048);
      KeyPair key = keyGen.generateKeyPair();
      return key;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
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

  public String toPEM(byte[] encoded) throws Exception {
    StringWriter stringWriter = new StringWriter();
    PemWriter pemWriter = new PemWriter(stringWriter);
    PemObject pemObject = new PemObject("PUBLIC KEY", encoded);
    pemWriter.writeObject(pemObject);
    pemWriter.flush();
    return stringWriter.toString();
  }
}
