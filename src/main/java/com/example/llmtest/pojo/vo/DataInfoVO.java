package com.example.llmtest.pojo.vo;

import com.example.llmtest.pojo.enums.DataSourceEnum;
import com.example.llmtest.pojo.enums.QuestionTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataInfoVO implements Serializable {

    private Long dataId;

    private String question;

    private String options;

    private String answer;

    private QuestionTypeEnum questionType;

    private DataSourceEnum dataSource;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;
}
