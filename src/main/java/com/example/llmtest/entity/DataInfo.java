package com.example.llmtest.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.llmtest.entity.data_info_enum.DataSourceType;
import com.example.llmtest.entity.data_info_enum.QuestionType;
import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.type.BlobTypeHandler;

import java.time.LocalDateTime;

@Data
@TableName("data_info")
public class DataInfo {

    @TableId(type = IdType.AUTO)
    private Long dataId;

    @TableField("question")
    private String question;

    @TableField("answer")
    private String answer;

    @TableField("question_type")
    private String questionType;

    @TableField("applicable_scenario")
    private String applicableScenario;

    @TableField("data_source")
    private String dataSource;

    @TableField("model_id")
    private Long modelId;

    @TableField("entry_date")
    private LocalDateTime entryDate;

    @TableLogic
    private Integer is_deleted;
}
