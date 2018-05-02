package com.service.remote.dto;

import lombok.Value;

import java.time.Instant;

/**
 * Created by Dawid on 01.05.2018 at 20:14.
 */

@Value
public class DateRange {
    private Instant from;
    private Instant to;
}
