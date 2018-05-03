package com.service.remote.mapers;

import com.service.remote.entity.LogDeviceParameter;
import com.service.remote.entity.PropertyInstance;
import com.service.remote.grpc.Property;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Dawid on 02.05.2018 at 13:38.
 */
@RequiredArgsConstructor
public class LogDevicesToLogBundle implements Mapper<com.service.remote.grpc.LogBundle, List<LogDeviceParameter>> {
    private final Predicate<PropertyInstance> predicate;

    @Override
    public com.service.remote.grpc.LogBundle map(List<LogDeviceParameter> logDeviceParameters) {
        return com.service.remote.grpc.LogBundle.newBuilder().addAllLogs(toLogs(logDeviceParameters)).build();
    }

    private List<com.service.remote.grpc.Log> toLogs(List<LogDeviceParameter> logDeviceParameters) {
        return logDeviceParameters.stream().map(this::toLog).collect(Collectors.toList());
    }

    private com.service.remote.grpc.Log toLog(LogDeviceParameter logDeviceParameter) {
        List<Property> properties = logDeviceParameter.getProperties()
                .stream()
                .filter(predicate)
                .map(that -> new PropertyInstanceToPropertyMapper().map(that))
                .collect(Collectors.toList());
        return com.service.remote.grpc.Log.newBuilder()
                .setDateTime(logDeviceParameter.getLogDate().getEpochSecond())
                .addAllProperties(properties)
                .build();
    }
}
