package com.example.llmtest.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.llmtest.exceptionhandler.BusinessException;
import com.example.llmtest.exceptionhandler.ReturnCode;
import com.example.llmtest.mapper.DataInfoMapper;
import com.example.llmtest.mapper.MetricMapper;
import com.example.llmtest.pojo.entity.DataInfo;
import com.example.llmtest.pojo.enums.DimensionEnum;
import com.example.llmtest.pojo.enums.QuestionTypeEnum;
import com.example.llmtest.pojo.vo.DataInfoVO;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<DataInfoVO> generateData(String dimension, String metric, Integer totalQuestions) {
        // 1. 参数校验
        if (StringUtils.isNotBlank(dimension)) {
            if (!customUtil.getDimensionMap().containsValue(dimension)) {
                throw new BusinessException(ReturnCode.RC400.getCode(), "维度不存在");
            }
        }
        if (StringUtils.isNotBlank(metric)) {
            if (!customUtil.getMetricMap().containsValue(metric)) {
                throw new BusinessException(ReturnCode.RC400.getCode(), "指标不存在");
            }
        }
        // 2. 如果是性能维度，必须指定具体指标
        if (DimensionEnum.PERFORMANCE.getValue().equals(dimension)) {
            if (metric == null || !customUtil.getMetricMap().containsValue(metric)) {
                throw new BusinessException(ReturnCode.RC400.getCode(), "性能维度未指定具体指标");
            }
        }
        Long metricId = null;
        if (metric != null) {
            metricId = metricMapper.selectIdByName(metric);
        }

        QueryWrapper<DataInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dimension", dimension);
        if (metricId != null) {
            queryWrapper.eq("metric_id", metricId);
        }

        // 4. 获取各类型题目数量
        Map<QuestionTypeEnum, Integer> typeCountMap = new HashMap<>();
        for (QuestionTypeEnum type : QuestionTypeEnum.values()) {
            QueryWrapper<DataInfo> typeQuery = new QueryWrapper<>();
            typeQuery.eq("dimension", dimension)
                    .eq("question_type", type);
            if (metricId != null) {
                typeQuery.eq("metric_id", metricId);
            }
            typeCountMap.put(type, Math.toIntExact(dataInfoMapper.selectCount(typeQuery)));
        }

        // 5. 计算每种类型应分配的题目数
        Map<QuestionTypeEnum, Integer> allocatedQuestions = new HashMap<>();
        Integer remainingQuestions = totalQuestions;

        // 计算总可用题目数
        int totalAvailable = typeCountMap.values().stream().mapToInt(Integer::intValue).sum();

        // 按比例分配题目
        for (Map.Entry<QuestionTypeEnum, Integer> entry : typeCountMap.entrySet()) {
            if (entry.getValue() > 0) {
                // 计算该类型应得的比例
                double ratio = (double) entry.getValue() / totalAvailable;
                int allocated = Math.max(1, (int) Math.round(ratio * totalQuestions));
                allocatedQuestions.put(entry.getKey(), allocated);
                remainingQuestions -= allocated;
            }
        }

        // 6. 随机抽取题目
        List<DataInfo> questionList = new ArrayList<>();
        for (Map.Entry<QuestionTypeEnum, Integer> entry : allocatedQuestions.entrySet()) {
            QueryWrapper<DataInfo> typeQuery = new QueryWrapper<>();
            typeQuery.select("data_id", "question", "options", "answer", "question_type", "data_source", "update_time")
                    .eq("dimension", dimension)
                    .eq("question_type", entry.getKey());
            if (metricId != null) {
                typeQuery.eq("metric_id", metricId);
            }
            typeQuery.last("ORDER BY RAND() LIMIT " + entry.getValue());

            questionList.addAll(dataInfoMapper.selectList(typeQuery));
        }

        return questionList.stream()
                .map(customUtil::convertToVO)
                .collect(Collectors.toList());
    }
}
