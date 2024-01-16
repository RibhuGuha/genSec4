package com.replicacia.repo;

import com.replicacia.model.HostInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostInfoRepository extends JpaRepository<HostInfo, Long> {
  HostInfo findByServiceName(String serviceName);
}
