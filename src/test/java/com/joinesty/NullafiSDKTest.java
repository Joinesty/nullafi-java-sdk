package com.joinesty;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NullafiSDKTest extends BaseMock {


  @Test
  public void GivenTheNeedToCreateClient_WhenUsingTheSDK_ShouldReturnNewClient() throws Exception {
    NullafiSDK nullafiSDK = new NullafiSDK(this.API_KEY);
    Client client = nullafiSDK.createClient();

    assertNotNull(client);
    assertEquals(client.getHashKey(), "some-hashKey");
  }
}