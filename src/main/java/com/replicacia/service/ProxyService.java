package com.replicacia.service;

import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public interface ProxyService {

  ResponseEntity<String> processProxyRequest(String body, HttpMethod method,
      HttpServletRequest request, HttpServletResponse response, String traceId)
      throws URISyntaxException;
}
