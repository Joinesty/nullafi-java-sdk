package com.joinesty.unit.services;

import com.joinesty.services.API;
import com.joinesty.unit.BaseMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class APITest extends BaseMock {

  @Test
  public void GivenNeedToCommunicateWithAPI_WhenSettingAURL_ThenShouldReturnWithPrefix() {
    API api = new API();
    String fullUrl = api.setUrl("/example");

    assertEquals(fullUrl, "http://0.0.0.0:8080/example");
  }
}