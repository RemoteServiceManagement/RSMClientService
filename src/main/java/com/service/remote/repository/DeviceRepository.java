package com.service.remote.repository;

import com.service.remote.entity.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Dawid on 03.06.2018 at 14:51.
 */
@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {
    boolean existsDeviceByExternalId(String externalId);
}
