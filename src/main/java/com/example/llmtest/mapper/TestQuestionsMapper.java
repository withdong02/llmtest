package com.example.llmtest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.llmtest.pojo.entity.TestQuestions;
import com.example.llmtest.pojo.vo.TestExactVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestQuestionsMapper extends BaseMapper<TestQuestions> {

    int insertBatch(List<TestQuestions> list);

    int insertBatchWithoutScore(List<TestQuestions> list);

    @Select("SELECT d.question, d.answer, ts.score, tq.model_output " +
            "FROM test_questions tq " +
            "JOIN data_info d ON tq.data_id = d.data_id " +
            "JOIN test_scores ts ON ts.item_id = d.data_id AND ts.test_id = tq.test_id " +
            "WHERE ts.item_type = 'question' AND tq.test_id = #{testId}")
    List<TestExactVO> findByTestId(@Param("testId") Long testId);

}
