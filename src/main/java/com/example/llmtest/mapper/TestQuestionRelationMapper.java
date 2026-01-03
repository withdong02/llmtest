package com.example.llmtest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.llmtest.pojo.entity.TestQuestions;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestQuestionRelationMapper extends BaseMapper<TestQuestions> {

    int insertBatch(List<TestQuestions> list);

    int insertBatchWithoutScore(List<TestQuestions> list);
}
