package com.joinesty.services;

import com.joinesty.services.crypto.AESGCM;
import com.joinesty.services.crypto.HMAC;
import com.joinesty.services.crypto.RSA;

public class Security {

  private AESGCM aes;

  private RSA rsa;

  private HMAC hmac;

  public Security() {
    this.aes = new AESGCM();
    this.rsa = new RSA();
    this.hmac = new HMAC();
  }

  public AESGCM getAes() {
    return aes;
  }

  public RSA getRsa() {
    return rsa;
  }

  public HMAC getHmac() {
    return hmac;
  }
}
