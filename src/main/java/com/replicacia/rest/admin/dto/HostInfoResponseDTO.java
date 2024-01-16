package com.replicacia.rest.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostInfoResponseDTO {
  private Long id;
  private String ip;
  private String port;
  private String scheme;
  private String serviceName;
  private String name;
}
