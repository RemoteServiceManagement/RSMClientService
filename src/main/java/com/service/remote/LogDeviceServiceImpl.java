package com.service.remote;

import com.querydsl.core.types.Predicate;
import com.service.remote.entity.LogDeviceParameter;
import com.service.remote.entity.QLogDeviceParameter;
import com.service.remote.grpc.DateRange;
import com.service.remote.grpc.DateRangeQuery;
import com.service.remote.grpc.LogBundle;
import com.service.remote.grpc.LogDeviceQuery;
import com.service.remote.mapers.DateRangeMapper;
import com.service.remote.mapers.LogDevicesToLogBundle;
import com.service.remote.service.LogDeviceParameterService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Instant;

import static org.springframework.data.domain.PageRequest.of;

/**
 * Created by Dawid on 02.05.2018 at 11:28.
 */
@GRpcService
@RequiredArgsConstructor
public class LogDeviceServiceImpl extends com.service.remote.grpc.LogDeviceServiceGrpc.LogDeviceServiceImplBase {
    private final LogDeviceParameterService logDeviceParameterService;

    @Override
    public void getDateRangeByDeviceExternalId(DateRangeQuery request, StreamObserver<DateRange> responseObserver) {
        com.service.remote.dto.DateRange dateRange = logDeviceParameterService.getLogRangeByDeviceExternalId(request.getDeviceExternalId());
        responseObserver.onNext(new DateRangeMapper().map(dateRange));
    }

    @Override
    public void getLogByDeviceExternalIdAndRange(LogDeviceQuery request, StreamObserver<LogBundle> responseObserver) {
        DateRange dateRange = request.getDateRange();
        Predicate predicate = QLogDeviceParameter.logDeviceParameter.device.externalId.eq(request.getDeviceExternalId())
                .and(QLogDeviceParameter.logDeviceParameter.logDate.between(getTimeFromTimeEpoch(dateRange.getFrom()), getTimeFromTimeEpoch(dateRange.getTo())));

        PageRequest pageRequest = of(0, 500, Sort.Direction.DESC, "logDate");
        Page<LogDeviceParameter> logDevice = logDeviceParameterService.getAll(predicate, pageRequest);

        if (logDevice.hasContent()) {
            sendLogs(logDevice, responseObserver, predicate);
        }

        responseObserver.onCompleted();
    }

    private void sendLogs(Page<LogDeviceParameter> logDevice, StreamObserver<LogBundle> responseObserver, Predicate predicate) {
        responseObserver.onNext(new LogDevicesToLogBundle().map(logDevice.getContent()));

        while (logDevice.hasNext()) {
            Pageable pageable = logDevice.nextPageable();
            logDevice = logDeviceParameterService.getAll(predicate, pageable);
            responseObserver.onNext(new LogDevicesToLogBundle().map(logDevice.getContent()));
        }
    }

    private Instant getTimeFromTimeEpoch(long from) {
        return Instant.ofEpochSecond(from);
    }
}
