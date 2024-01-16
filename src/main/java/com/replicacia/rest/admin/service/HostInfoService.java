package com.replicacia.rest.admin.service;

import com.replicacia.model.HostInfo;
import com.replicacia.rest.admin.dto.HostInfoRequestDTO;
import com.replicacia.rest.admin.dto.HostInfoResponseDTO;
import java.util.List;

public interface HostInfoService {

  HostInfoResponseDTO addHost(HostInfoRequestDTO hostInfoRequestDTO);
  HostInfo getHostByServiceName(String serviceName);

  HostInfoResponseDTO getHostById(Long id);

  void deleteHostById(Long id);

  List<HostInfoResponseDTO> getAllHost();
}
