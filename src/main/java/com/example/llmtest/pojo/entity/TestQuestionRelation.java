package com.example.llmtest.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class TestQuestionRelation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long testId;
    private Long dataId;
}
