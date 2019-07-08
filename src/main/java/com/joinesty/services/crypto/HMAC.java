package com.joinesty.services.crypto;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMAC {

  public HMAC() {
  }

  public String hash(String value, String secret) {
    try {
      Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
      sha256_HMAC.init(secret_key);

      return Base64.encodeBase64String(sha256_HMAC.doFinal(value.getBytes()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }
}
