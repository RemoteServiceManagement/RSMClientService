package com.service.remote.mapers;

import com.service.remote.entity.PropertyDefinition;
import com.service.remote.entity.PropertyInstance;
import com.service.remote.grpc.Property;

/**
 * Created by Dawid on 02.05.2018 at 13:52.
 */
public class PropertyInstanceToPropertyMapper implements Mapper<com.service.remote.grpc.Property, PropertyInstance> {
    @Override
    public Property map(PropertyInstance propertyInstance) {
        PropertyDefinition definition = propertyInstance.getDefinition();
        Property property = Property.newBuilder()
                .setName(definition.getName())
                .setCode(definition.getCode())
                .setUnit(definition.getMeasureUnit())
                .setValueType(Property.ValueType.valueOf(definition.getValueType().name()))
                .buildPartial();

        switch (definition.getValueType()) {
            case LONG:
                return property.toBuilder().setLongValue(propertyInstance.getLongValue()).build();
            case DOUBLE:
                return property.toBuilder().setDoubleValue(propertyInstance.getDoubleValue()).build();
            case STRING:
                return property.toBuilder().setStringValue(propertyInstance.getStringValue()).build();
        }

        return property;
    }
}
