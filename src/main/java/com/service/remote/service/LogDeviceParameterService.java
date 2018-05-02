package com.service.remote.service;

import com.querydsl.core.types.Predicate;
import com.service.remote.dto.DateRange;
import com.service.remote.entity.LogDeviceParameter;
import com.service.remote.repository.LogDeviceParameterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by Dawid on 29.04.2018 at 15:01.
 */
@Service
@RequiredArgsConstructor
public class LogDeviceParameterService {
    private final LogDeviceParameterRepository repository;

    public DateRange getLogRangeByDeviceExternalId(String deviceExternalId) {
        return repository.findLogDeviceDateRange(deviceExternalId);
    }

    public Page<LogDeviceParameter> getAll(Predicate predicate, Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }
}