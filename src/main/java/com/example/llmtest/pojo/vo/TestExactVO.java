package com.example.llmtest.pojo.vo;

import lombok.Data;

@Data
public class TestExactVO {

    private String question;
    private String answer;
    private String modelOutput;

    //针对按题目给分的测试
    private Double score;
}
