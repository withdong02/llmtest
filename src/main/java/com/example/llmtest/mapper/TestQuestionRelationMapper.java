package com.example.llmtest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.llmtest.pojo.entity.TestQuestionRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestQuestionRelationMapper extends BaseMapper<TestQuestionRelation> {

    int insertBatch(List<TestQuestionRelation> list);

    int insertBatchWithoutScore(List<TestQuestionRelation> list);
}
