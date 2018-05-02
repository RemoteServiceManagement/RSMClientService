package com.service.remote.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.List;

/**
 * Created by Dawid on 27.04.2018 at 11:54.
 */
@Entity
@Getter
@Setter
public class LogDeviceParameter extends BaseEntity {
    private Instant logDate;
    @ManyToOne
    private Device device;

    @OneToMany(mappedBy = "logDeviceParameter")
    private List<PropertyInstance> properties;
}
