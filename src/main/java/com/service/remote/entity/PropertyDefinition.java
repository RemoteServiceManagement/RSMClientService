package com.service.remote.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by Dawid on 27.04.2018 at 11:30.
 */
@Entity
@Getter
@Setter
public class PropertyDefinition extends BaseEntity {
    private String code;
    private String name;
    private String measureUnit;
    @Enumerated(EnumType.STRING)
    private ValueType valueType;

}
