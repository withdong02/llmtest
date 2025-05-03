package com.example.llmtest.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.llmtest.entity.enums.DataSourceEnum;
import com.example.llmtest.entity.enums.DimensionEnum;
import com.example.llmtest.entity.enums.QuestionTypeEnum;
import com.example.llmtest.entity.enums.TransformationTypeEnum;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("data_info")
public class DataInfo {

    @TableId(type = IdType.AUTO)
    @TableField("data_id")
    private Long dataId;

    @TableField("model_id")//foreign key
    private Long modelId;

    private String question;

    private String options;

    private String answer;

    private DimensionEnum dimension;

    @TableField("metric_id")
    private Long metricId;

    @TableField("sub_metric_id")
    private Long subMetricId;

    private QuestionTypeEnum questionType;


    private DataSourceEnum dataSource;

    private TransformationTypeEnum transformationType;

    @TableField("transformation_description")
    private String transformationDescription;

    @TableField("entry_date")
    private Timestamp entryDate;

    @TableField("last_updated")
    private Timestamp lastUpdated;

    @TableLogic
    private Integer is_deleted;

    private Integer is_transformed;
}
