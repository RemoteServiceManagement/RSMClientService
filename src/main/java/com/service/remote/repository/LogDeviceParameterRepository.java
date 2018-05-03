package com.service.remote.repository;

import com.service.remote.dto.BasicPropertyDefinitionDto;
import com.service.remote.dto.DateRange;
import com.service.remote.entity.LogDeviceParameter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Dawid on 29.04.2018 at 14:53.
 */
@Repository
public interface LogDeviceParameterRepository extends QuerydslPredicateExecutor<LogDeviceParameter>, CrudRepository<LogDeviceParameter, Long> {
    @Query("SELECT new com.service.remote.dto.DateRange(MIN(ldp.logDate), MAX(ldp.logDate)) FROM LogDeviceParameter ldp WHERE ldp.device.externalId = ?1")
    DateRange findLogDeviceDateRange(String deviceExternalId);

    @Query("SELECT DISTINCT new com.service.remote.dto.BasicPropertyDefinitionDto(pi.definition.name, pi.definition.code) FROM PropertyInstance pi WHERE pi.logDeviceParameter.device.externalId = ?1")
    List<BasicPropertyDefinitionDto> getPropertyDefinition(String deviceExternalId);
}
