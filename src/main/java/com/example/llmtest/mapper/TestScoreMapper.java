package com.example.llmtest.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.llmtest.pojo.entity.TestScore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestScoreMapper extends BaseMapper<TestScore> {

    int insertBatch(List<TestScore> list);
}
