package com.replicacia.rest.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostInfoRequestDTO {
  private String ip;
  private Integer port;
  private String scheme;
  private String serviceName;
  private String name;
}
