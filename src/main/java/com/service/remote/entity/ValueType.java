package com.service.remote.entity;

/**
 * Created by Dawid on 27.04.2018 at 11:48.
 */
public enum ValueType {
    LONG(true),
    DOUBLE(true),
    STRING(false);

    private final boolean numerical;

    ValueType(boolean numerical) {
        this.numerical = numerical;
    }

    public boolean isNumerical() {
        return numerical;
    }
}
