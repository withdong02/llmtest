package com.example.llmtest.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.llmtest.entity.enums.DataSourceEnum;
import com.example.llmtest.entity.enums.DimensionEnum;
import com.example.llmtest.entity.enums.QuestionTypeEnum;
import com.example.llmtest.entity.enums.TransformationTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataInfo {

    @TableId(type = IdType.AUTO)
    private Long dataId;

    //逻辑外键
    private Long modelId;
    //逻辑外键
    private Long metricId;

    private String question;

    private String options;

    private String answer;

    private DimensionEnum dimension;

    @TableField("sub_metric_id")
    private Long subMetricId;

    private QuestionTypeEnum questionType;

    private DataSourceEnum dataSource;

    private TransformationTypeEnum transformationType;

    private String transformationDescription;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;

    @TableLogic
    private Integer isDeleted;

    private Integer isTransformed;
}
