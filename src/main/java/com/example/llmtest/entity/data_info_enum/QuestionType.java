package com.example.llmtest.entity.data_info_enum;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum QuestionType {
    CHOICE("choice"),
    JUDGMENT("judgment"),
    SHORT_ANSWER("short_answer");

    @EnumValue
    private final String value;

    QuestionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // 可以添加一个静态方法，用于根据字符串值获取对应的枚举常量
    public static QuestionType fromValue(String value) {
        for (QuestionType type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No matching QuestionType for value: " + value);
    }
}
