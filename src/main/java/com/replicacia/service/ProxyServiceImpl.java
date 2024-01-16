package com.replicacia.service;

import com.replicacia.model.HostInfo;
import com.replicacia.rest.admin.service.HostInfoService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@AllArgsConstructor
public class ProxyServiceImpl implements ProxyService {

  private final RestTemplate restTemplate;
  private final HostInfoService hostResolverService;
  @Override
  public ResponseEntity<String> processProxyRequest(String body, HttpMethod method,
      HttpServletRequest request, HttpServletResponse response, String traceId)
      throws URISyntaxException {

    if(StringUtils.isEmpty(request.getRequestURI())){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    String requestUrl = request.getRequestURI().replaceFirst("/gateway", "");
    HostInfo hostInfo = hostResolverService.getHostByServiceName(requestUrl.split("/")[1]);

    if (Objects.isNull(hostInfo)){
      log.error("Host info is/are not added in DB.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("Host info Not Found in database. Please use admin APIs to configure host in DB.");
    }

    URI uri = new URI(hostInfo.getScheme(), null, hostInfo.getIp(), hostInfo.getPort(), null, null, null);

    uri = UriComponentsBuilder.fromUri(uri)
        .path(requestUrl)
        .query(request.getQueryString())
        .build(true).toUri();

    HttpHeaders headers = new HttpHeaders();
    Enumeration<String> headerNames = request.getHeaderNames();

    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      headers.set(headerName, request.getHeader(headerName));
    }

    headers.remove(HttpHeaders.ACCEPT_ENCODING);
    HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

    try {
      ResponseEntity<String> serverResponse = restTemplate.exchange(uri, method, httpEntity,
          String.class);
      log.info(serverResponse.toString());
      return serverResponse;
    } catch (HttpStatusCodeException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(e.getRawStatusCode())
          .headers(e.getResponseHeaders())
          .body(e.getResponseBodyAsString());
    }


  }
}
