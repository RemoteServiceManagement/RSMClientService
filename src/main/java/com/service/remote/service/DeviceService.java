package com.service.remote.service;

import com.service.remote.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by Dawid on 03.06.2018 at 14:52.
 */

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository repository;

    public boolean deviceExist(String externalId) {
        return repository.existsDeviceByExternalId(externalId);
    }
}
