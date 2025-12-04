package com.example.llmtest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.llmtest.exceptionhandler.BusinessException;
import com.example.llmtest.exceptionhandler.ReturnCode;
import com.example.llmtest.mapper.*;
import com.example.llmtest.pojo.dto.TestDTO;
import com.example.llmtest.pojo.entity.DataInfo;
import com.example.llmtest.pojo.entity.TestInfo;
import com.example.llmtest.pojo.entity.TestQuestionRelation;
import com.example.llmtest.pojo.enums.DimensionEnum;
import com.example.llmtest.pojo.vo.TestVO;
import com.example.llmtest.service.PerformanceTestService;
import com.example.llmtest.utils.CustomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class PerformanceTestServiceImpl extends ServiceImpl<TestInfoMapper, TestInfo> implements PerformanceTestService {

    private static final String FLASK_URL = "https://www.u659522.nyat.app:26249/evaluation/general_process";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;
    private CustomUtil customUtil;
    private DataInfoMapper dataInfoMapper;
    private final ModelMapper modelMapper;
    private final MetricMapper metricMapper;
    private final TestQuestionRelationMapper testQuestionRelationMapper;

    public PerformanceTestServiceImpl(RestTemplate restTemplate, CustomUtil customUtil,
                                      DataInfoMapper dataInfoMapper, ModelMapper modelMapper,
                                      MetricMapper metricMapper,
                                      TestQuestionRelationMapper testQuestionRelationMapper) {
        this.restTemplate = restTemplate;
        this.customUtil = customUtil;
        this.dataInfoMapper = dataInfoMapper;
        this.modelMapper = modelMapper;
        this.metricMapper = metricMapper;
        this.testQuestionRelationMapper = testQuestionRelationMapper;
    }
    /**
     * 系统响应效率测试
     * @param dto
     * @return
     */
    @Override
    public TestVO metricOneTest(TestDTO dto) {
        String dimension = dto.getDimension();
        String metric = dto.getMetric();
        if (dimension.isEmpty() || !customUtil.getDimensionMap().containsValue(dimension)) {
            throw new BusinessException(ReturnCode.RC400.getCode(), "维度不存在");
        }
        if (!customUtil.getMetricMap().containsValue(metric)) {
            throw new BusinessException(ReturnCode.RC400.getCode(), "指标不存在");
        }

        //构建请求体
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", dto.getModelName());
        if (metric != null && !metric.isEmpty()) {
            requestBody.put("domain", metric);
        } else {
            requestBody.put("domain", dimension);
        }
        List<Long> questionList = dto.getQuestionList();
        List<Object> qsList = new ArrayList<>();
        for (int i = 0; i < questionList.size(); i++) {
            Long dataId = questionList.get(i);
            DataInfo data = dataInfoMapper.selectById(dataId);
            if (!data.getDimension().getValue().equals(dimension)) {
                log.warn("id为{}的题目维度不符合，已自动跳过", dataId);
                continue;
            }
            if (!metricMapper.selectIdByName(metric).equals(data.getMetricId())) {
                log.warn("id为{}的题目指标不符合，已自动跳过", dataId);
                continue;
            }
            HashMap<String, Object> everyData = new HashMap<>();
            everyData.put("rowIdx", i);
            everyData.put("question", data.getQuestion());
            everyData.put("answer", data.getAnswer());
            everyData.put("type_index", data.getQuestionType());
            if (data.getOptions() != null && !data.getOptions().isEmpty()) {
                String optionsString = data.getOptions();
                String[] options = optionsString.split("\\|");
                for (int j = 0; j < options.length; j++) {
                    options[j] = options[j].replaceFirst(":", ": ");
                }
                everyData.put("options", options);
            }
            qsList.add(everyData);
        }
        requestBody.put("qs_list", qsList);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.info("发送请求");
        LocalDateTime startTime = LocalDateTime.now();
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        //接受响应
        ResponseEntity<String> response = restTemplate.postForEntity(FLASK_URL, requestEntity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BusinessException(500, "调用 Flask 接口失败，状态码：" + response.getStatusCode());
        }

        TestVO vo = new TestVO();
        try {
            Map<String, Object> result = objectMapper.readValue(response.getBody(),
                    new TypeReference<Map<String, Object>>() {});

            Map<String, Object> score = (Map<String, Object>) result.get("result");
            Double finalScore = (double)score.get("final_score");
            vo.setFinalScore(finalScore);
            List<Double> singleScoreList = (List<Double>) score.get("single_score");
            Double[] singleScore = singleScoreList.toArray(new Double[0]);
            vo.setSingleScore(singleScore);
            String resultDescription = "finalScore is " + finalScore + "singleScore is " + Arrays.toString(singleScore);
            log.info(resultDescription);
            TestInfo testInfo = TestInfo.builder()
                    .name(dto.getTestName())
                    .modelId(modelMapper.selectIdByName(dto.getModelName()))
                    .dimension(DimensionEnum.valueOf(dimension.toUpperCase()))
                    .metricId(metricMapper.selectIdByName(dto.getMetric()))
                    .os(dto.getOs())
                    .cpu(dto.getCpu())
                    .gpu(dto.getGpu())
                    .count((long) questionList.size())
                    .resultDescription(resultDescription)
                    .build();
            this.save(testInfo);

            //批量插入testId和dataId
            List<TestQuestionRelation> relations = new ArrayList<>();
            for (Long dataId : questionList) {
                TestQuestionRelation relation = new TestQuestionRelation();
                relation.setTestId(testInfo.getTestId());
                relation.setDataId(dataId);
                relations.add(relation);
            }
            testQuestionRelationMapper.insertBatch(relations);

            LocalDateTime endTime = LocalDateTime.now();
            Duration duration = Duration.between(startTime, endTime);
            log.info("批量处理{}道题目总耗时: {}分{}秒", questionList.size(), duration.toMinutes(), duration.getSeconds() % 60);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return vo;
    }
}
