package com.joinesty.services.crypto;

import org.bouncycastle.util.encoders.Base64;

public class AESDTO {

  private byte[] encryptedData;

  private byte[] initializationVector;

  private byte[] authenticationTag;

  public void setEncryptedData(byte[] encryptedData) {
    this.encryptedData = encryptedData;
  }

  public String getEncryptedData() {
    return new String(Base64.encode(this.encryptedData));
  }

  public void setInitializationVector(byte[] initializationVector) {
    this.initializationVector = initializationVector;
  }

  public String getInitializationVector() {
    return new String(Base64.encode(this.initializationVector));
  }

  public void setAuthenticationTag(byte[] authenticationTag) {
    this.authenticationTag = authenticationTag;
  }

  public String getAuthenticationTag() {
    return new String(Base64.encode(this.authenticationTag));
  }
}