package com.service.remote;

import com.querydsl.core.types.Predicate;
import com.service.remote.dto.BasicPropertyDefinitionDto;
import com.service.remote.entity.LogDeviceParameter;
import com.service.remote.entity.QLogDeviceParameter;
import com.service.remote.grpc.DateRange;
import com.service.remote.grpc.DeviceBasicQuery;
import com.service.remote.grpc.DeviceExistResponse;
import com.service.remote.grpc.LogBundle;
import com.service.remote.grpc.LogDeviceQuery;
import com.service.remote.grpc.PropertyDefinition;
import com.service.remote.grpc.PropertyDefinitionBundle;
import com.service.remote.mapers.DateRangeMapper;
import com.service.remote.mapers.LogDevicesToLogBundle;
import com.service.remote.service.DeviceService;
import com.service.remote.service.LogDeviceParameterService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.service.remote.grpc.DeviceExistResponse.newBuilder;
import static org.springframework.data.domain.PageRequest.of;

/**
 * Created by Dawid on 02.05.2018 at 11:28.
 */
@GRpcService
@RequiredArgsConstructor
@Transactional
public class LogDeviceServiceImpl extends com.service.remote.grpc.LogDeviceServiceGrpc.LogDeviceServiceImplBase {
    private final LogDeviceParameterService logDeviceParameterService;
    private final DeviceService deviceService;

    @Override
    public void getDateRangeByDeviceExternalId(DeviceBasicQuery request, StreamObserver<DateRange> responseObserver) {
        com.service.remote.dto.DateRange dateRange = logDeviceParameterService.getLogRangeByDeviceExternalId(request.getDeviceExternalId());
        responseObserver.onNext(new DateRangeMapper().map(dateRange));
        responseObserver.onCompleted();
    }

    @Override
    public void getLogs(LogDeviceQuery request, StreamObserver<LogBundle> responseObserver) {
        DateRange dateRange = request.getDateRange();
        Predicate predicate = QLogDeviceParameter.logDeviceParameter.device.externalId.eq(request.getDeviceExternalId())
                .and(QLogDeviceParameter.logDeviceParameter.logDate.between(getTimeFromTimeEpoch(dateRange.getFrom()), getTimeFromTimeEpoch(dateRange.getTo())));

        PageRequest pageRequest = of(0, 500, Sort.Direction.DESC, "logDate");
        Page<LogDeviceParameter> logDevice = logDeviceParameterService.getAll(predicate, pageRequest);

        if (logDevice.hasContent()) {
            sendLogs(logDevice, responseObserver, predicate, request);
        }

        responseObserver.onCompleted();
    }

    @Override
    public void getDevicePropertyNames(DeviceBasicQuery request, StreamObserver<PropertyDefinitionBundle> responseObserver) {
        List<BasicPropertyDefinitionDto> propertyDefinitions = logDeviceParameterService.getDevicePropertiesDefinition(request.getDeviceExternalId());
        List<PropertyDefinition> definitions = propertyDefinitions.stream().map(this::toPropertyDefinition).collect(Collectors.toList());
        responseObserver.onNext(PropertyDefinitionBundle.newBuilder().addAllPropertyDefinition(definitions).build());
        responseObserver.onCompleted();
    }

    @Override
    public void deviceExist(DeviceBasicQuery request, StreamObserver<DeviceExistResponse> responseObserver) {
        boolean exist = deviceService.deviceExist(request.getDeviceExternalId());
        responseObserver.onNext(newBuilder().setExist(exist).build());
        responseObserver.onCompleted();
    }

    private PropertyDefinition toPropertyDefinition(BasicPropertyDefinitionDto basicPropertyDefinitionDto) {
        return PropertyDefinition.newBuilder()
                .setName(basicPropertyDefinitionDto.getName())
                .setCode(basicPropertyDefinitionDto.getCode())
                .build();
    }

    private void sendLogs(Page<LogDeviceParameter> logDevice, StreamObserver<LogBundle> responseObserver, Predicate predicate, LogDeviceQuery request) {
        responseObserver.onNext(getLogBundle(logDevice, request));

        while (logDevice.hasNext()) {
            Pageable pageable = logDevice.nextPageable();
            logDevice = logDeviceParameterService.getAll(predicate, pageable);
            responseObserver.onNext(getLogBundle(logDevice, request));
        }
    }

    private LogBundle getLogBundle(Page<LogDeviceParameter> logDevice, LogDeviceQuery request) {
        LogDevicesToLogBundle logDevicesToLogBundle = new LogDevicesToLogBundle(that -> request.getPropertiesCodesList().contains(that.getDefinition().getCode()));
        return logDevicesToLogBundle.map(logDevice.getContent());
    }

    private Instant getTimeFromTimeEpoch(long from) {
        return Instant.ofEpochSecond(from);
    }
}
