package com.service.remote.mapers;

import com.service.remote.dto.DateRange;

import java.time.Instant;

import static java.util.Optional.ofNullable;

/**
 * Created by Dawid on 02.05.2018 at 11:42.
 */
public class DateRangeMapper implements Mapper<com.service.remote.grpc.DateRange, DateRange> {
    @Override
    public com.service.remote.grpc.DateRange map(DateRange dateRange) {
        return ofNullable(dateRange)
                .map(this::toDateRange)
                .orElse(com.service.remote.grpc.DateRange.newBuilder().build());
    }

    private com.service.remote.grpc.DateRange toDateRange(DateRange dateRange) {
        return com.service.remote.grpc.DateRange.newBuilder()
                .setFrom(getEpochTime(dateRange.getFrom()))
                .setTo(getEpochTime(dateRange.getTo()))
                .build();
    }

    private long getEpochTime(Instant date) {
        return date.getEpochSecond();
    }
}
