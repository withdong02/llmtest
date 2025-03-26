package com.example.llmtest.entity.data_info_enum;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum DataSourceType {
    INPUT("input"),
    CRAWLER("crawler"),
    MODEL_GENERATION("model_generation");

    @EnumValue
    private final String value;

    DataSourceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DataSourceType fromValue(String value) {
        for (DataSourceType type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid dataSourceType value: " + value);
    }
}
