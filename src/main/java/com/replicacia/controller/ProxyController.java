package com.replicacia.controller;

import com.replicacia.service.ProxyService;
import java.net.URISyntaxException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/gateway/")
public class ProxyController {

  @Autowired
  ProxyService proxyService;

  @RequestMapping("/**")
  public ResponseEntity<String> handle(@RequestBody(required = false) String body,
      HttpMethod method, HttpServletRequest request, HttpServletResponse response)
      throws URISyntaxException {
    //
    return proxyService.processProxyRequest(body, method, request, response,
        UUID.randomUUID().toString());
  }
}

