package com.service.remote.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by Dawid on 27.04.2018 at 11:52.
 */
@Entity
@Getter
@Setter
public class PropertyInstance extends BaseEntity {
    @ManyToOne
    private PropertyDefinition definition;
    @ManyToOne
    @JsonIgnore
    private LogDeviceParameter logDeviceParameter;
    private Long longValue;
    private String stringValue;
    private Double doubleValue;
}
