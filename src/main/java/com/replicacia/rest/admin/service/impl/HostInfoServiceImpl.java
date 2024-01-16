package com.replicacia.rest.admin.service.impl;

import com.replicacia.model.HostInfo;
import com.replicacia.repo.HostInfoRepository;
import com.replicacia.rest.admin.dto.HostInfoRequestDTO;
import com.replicacia.rest.admin.dto.HostInfoResponseDTO;
import com.replicacia.rest.admin.service.HostInfoService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class HostInfoServiceImpl implements HostInfoService {
  private final HostInfoRepository hostInfoRepository;
  private final ModelMapper mapper;

  @Override
  public HostInfoResponseDTO addHost(HostInfoRequestDTO hostInfoRequestDTO) {
    HostInfo hostInfo = mapper.map(hostInfoRequestDTO, HostInfo.class);
    HostInfo savedHostInfo = hostInfoRepository.save(hostInfo);
    return mapper.map(savedHostInfo, HostInfoResponseDTO.class);
  }

  @Override
  public HostInfo getHostByServiceName(String serviceName) {
    if (StringUtils.isEmpty(serviceName)) {
      return null;
    }
    return hostInfoRepository.findByServiceName(serviceName);
  }

  @Override
  public HostInfoResponseDTO getHostById(Long id) {
    return mapper.map(hostInfoRepository.getOne(id), HostInfoResponseDTO.class);
  }

  @Override
  public void deleteHostById(Long id) {
    hostInfoRepository.deleteById(id);
  }

  @Override
  public List<HostInfoResponseDTO> getAllHost() {
    List<HostInfo> hostInfos = hostInfoRepository.findAll();
    return hostInfos.stream()
        .map(hostInfo -> mapper.map(hostInfo, HostInfoResponseDTO.class))
        .collect(Collectors.toList());
  }
}
