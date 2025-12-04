/*
package com.example.llmtest.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.llmtest.exceptionhandler.BusinessException;
import com.example.llmtest.exceptionhandler.ReturnCode;
import com.example.llmtest.mapper.DataInfoMapper;
import com.example.llmtest.mapper.MetricMapper;
import com.example.llmtest.pojo.entity.DataInfo;
import com.example.llmtest.pojo.enums.DimensionEnum;
import com.example.llmtest.pojo.enums.QuestionTypeEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TestDataGenerator {
    private final DataInfoMapper dataInfoMapper;
    private final MetricMapper metricMapper;
    private final CustomUtil customUtil;

    public TestDataGenerator(DataInfoMapper dataInfoMapper,
                                 MetricMapper metricMapper,
                                 CustomUtil customUtil) {
        this.dataInfoMapper = dataInfoMapper;
        this.metricMapper = metricMapper;
        this.customUtil = customUtil;
    }
    public List<DataInfo> generateData(String dimension, String metric, Integer totalQuestions) {
        // 1. 参数校验
        if (!dimension.isBlank()) {
            customUtil.getDimensionMap().containsValue(dimension)) {

        }
            throw new BusinessException(ReturnCode.RC400.getCode(), "维度不存在");
        }

        // 2. 如果是性能维度，必须指定具体指标
        if (DimensionEnum.PERFORMANCE.getValue().equals(dimension)) {
            if (metric == null || !customUtil.getMetricMap().containsValue(metric)) {
                throw new BusinessException(ReturnCode.RC400.getCode(), "性能维度未指定具体指标");
            }
        }

        // 3. 构建查询条件
        QueryWrapper<DataInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dimension", dimension);
        if (metric != null) {
            Long metricId = metricMapper.selectIdByName(metric);
            queryWrapper.eq("metric_id", metricId);
        }

        // 4. 获取各类型题目数量
        Map<QuestionTypeEnum, Integer> typeCountMap = new HashMap<>();
        for (QuestionTypeEnum type : QuestionTypeEnum.values()) {
            QueryWrapper<DataInfo> typeQuery = new QueryWrapper<>();
            typeQuery.eq("dimension", dimension)
                    .eq("question_type", type);
            if (metric != null) {
                Long metricId = metricMapper.selectIdByName(metric);
                typeQuery.eq("metric_id", metricId);
            }
            typeCountMap.put(type, Math.toIntExact(dataInfoMapper.selectCount(typeQuery)));
        }

        // 5. 计算每种类型应分配的题目数
        Map<QuestionTypeEnum, Integer> allocatedQuestions = new HashMap<>();
        Integer remainingQuestions = totalQuestions;

        // 首先确保每种存在的类型至少有一道题
        typeCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .forEach(entry -> {
                    allocatedQuestions.put(entry.getKey(), 1);
                    remainingQuestions--;
                });

        // 按比例分配剩余题目
        if (remainingQuestions > 0) {
            Integer totalAvailable = typeCountMap.values().stream()
                    .mapToInt(Integer::intValue)
                    .sum();

            for (Map.Entry<QuestionTypeEnum, Integer> entry : typeCountMap.entrySet()) {
                if (entry.getValue() > 0) {
                    QuestionTypeEnum type = entry.getKey();
                    Integer availableCount = entry.getValue();

                    // 计算该类型应得的比例
                    double ratio = (double) availableCount / totalAvailable;
                    Integer additionalQuestions = (int) Math.round(ratio * remainingQuestions);

                    // 确保不超过可用题目数
                    additionalQuestions = Math.min(additionalQuestions, availableCount - 1);
                    allocatedQuestions.put(type, allocatedQuestions.get(type) + additionalQuestions);
                }
            }
        }

        // 6. 随机抽取题目
        List<DataInfo> questionList = new ArrayList<>();
        for (Map.Entry<QuestionTypeEnum, Integer> entry : allocatedQuestions.entrySet()) {
            QueryWrapper<DataInfo> typeQuery = new QueryWrapper<>();
            typeQuery.eq("dimension", dimension)
                    .eq("question_type", entry.getKey());
            if (metric != null) {
                Long metricId = metricMapper.selectIdByName(metric);
                typeQuery.eq("metric_id", metricId);
            }
            typeQuery.last("ORDER BY RAND() LIMIT " + entry.getValue());

            List<DataInfo> questions = dataInfoMapper.selectList(typeQuery);
            questionList.addAll(questions);
        }

        return questionList;
}
*/
