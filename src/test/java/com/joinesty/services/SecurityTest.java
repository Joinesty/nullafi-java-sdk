package com.joinesty.services;

import com.joinesty.BaseMock;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SecurityTest extends BaseMock {

  @Test
  public void GivenNeedToUseEncryption_WhenSecurityClassIsInstantiated_ShouldHaveAllCryptoPropsInstantiaded() {
    Security security = new Security();

    assertNotNull(security.getAes());
    assertNotNull(security.getRsa());
    assertNotNull(security.getHmac());
  }
}