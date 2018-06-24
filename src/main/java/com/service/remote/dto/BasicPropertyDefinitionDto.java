package com.service.remote.dto;

import com.service.remote.entity.ValueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Dawid on 04.05.2018 at 00:23.
 */
@Data
@NoArgsConstructor
public class BasicPropertyDefinitionDto {
    private String name;
    private String code;
    private String unit;
    private boolean numerical;

    public BasicPropertyDefinitionDto(String name, String code, String unit, ValueType valueType) {
        this.name = name;
        this.code = code;
        this.unit = unit;
        this.numerical = valueType.isNumerical();
    }
}
