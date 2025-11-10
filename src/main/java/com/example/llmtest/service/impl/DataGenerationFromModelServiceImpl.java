package com.example.llmtest.service.impl;

import com.example.llmtest.exceptionhandler.BusinessException;
import com.example.llmtest.exceptionhandler.ReturnCode;
import com.example.llmtest.pojo.dto.DGFromModelDTO;
import com.example.llmtest.service.DataGenerationFromModelService;
import com.example.llmtest.utils.ETCMappingUtil;
import com.example.llmtest.pojo.entity.DataInfo;
import com.example.llmtest.pojo.enums.DataSourceEnum;
import com.example.llmtest.pojo.enums.DimensionEnum;
import com.example.llmtest.pojo.enums.QuestionTypeEnum;
import com.example.llmtest.mapper.DataInfoMapper;
import com.example.llmtest.mapper.MetricMapper;
import com.example.llmtest.mapper.SubMetricMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataGenerationFromModelServiceImpl implements DataGenerationFromModelService {

    private static final String FLASK_URL = "https://www.u659522.nyat.app:26249/generate";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DataInfoMapper dataInfoMapper;
    private final MetricMapper  metricMapper;
    private final SubMetricMapper subMetricMapper;
    private final ETCMappingUtil mappingUtil;

    public DataGenerationFromModelServiceImpl(ETCMappingUtil mappingUtil, DataInfoMapper dataInfoMapper,
                                              MetricMapper metricMapper, SubMetricMapper subMetricMapper) {
        this.mappingUtil = mappingUtil;
        this.dataInfoMapper = dataInfoMapper;
        this.metricMapper = metricMapper;
        this.subMetricMapper = subMetricMapper;
    }

    @Transactional
    public List<DataInfo> processAndSave(DGFromModelDTO dto) {
        if ((dto.getMetric() == null || dto.getMetric().isEmpty())
                && (dto.getSubMetric() != null && !dto.getSubMetric().isEmpty())) {
            throw new BusinessException(ReturnCode.RC400.getCode(), "子指标存在，指标不存在");
        }
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("dimension", dto.getDimension());
        requestBody.put("metric", dto.getMetric());
        if (dto.getSubMetric() != null && !dto.getSubMetric().isEmpty()) {
            requestBody.put("sub_metric", dto.getSubMetric());
        }
        List<String> questionType = dto.getQuestionType();
        int[] weight = new int[5];
        if (questionType != null) {
            for (String type : questionType) {
                Integer index = mappingUtil.getQuestionTypeMap().get(type);
                if (index != null && index >= 0 && index < 5) {
                    weight[index] = 1;
                }
            }
        }
        requestBody.put("weights_set", weight);
        String example = "";
        if (dto.getExample() != null && !dto.getExample().isEmpty()) {
            example = dto.getExample();
        }
        requestBody.put("example", example);
        Long count = 10L;
        if (dto.getCount() != null) count = dto.getCount();
        requestBody.put("count", count);
        //构造请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        System.out.println(requestEntity);
        // 发送请求
        ResponseEntity<String> response = restTemplate.postForEntity(FLASK_URL, requestEntity, String.class);

        List<Map<String, Object>> result;
        List<DataInfo> returnVal = new ArrayList<>();
        try {
            // 解析响应
            //String innerJson = objectMapper.readValue(response.getBody(), String.class);
            result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
            /*String str = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            System.out.println(str);*/
            // 插入数据库
            for (Map<String, Object> item : result) {
                DataInfo dataInfo = new DataInfo();
                dataInfo.setDimension(DimensionEnum.valueOf(dto.getDimension().toUpperCase()));
                dataInfo.setDataSource(DataSourceEnum.MODEL_GENERATION);
                dataInfo.setModelId(1L);
                dataInfo.setMetricId(metricMapper.selectIdByName(dto.getMetric()));
                dataInfo.setSubMetricId(subMetricMapper.selectIdByName(dto.getSubMetric()));
                dataInfo.setQuestionType(QuestionTypeEnum.valueOf(((String)item.get("type_index")).toUpperCase()));
                dataInfo.setQuestion(String.valueOf(item.get("question")));
                dataInfo.setAnswer(String.valueOf(item.get("answer")));
                //只有选择题才有选项
                if (item.containsKey("options")) {
                    Object optionsObj = item.get("options");
                    if (optionsObj instanceof List<?>) {
                        List<?> optionsList = (List<?>) optionsObj;
                        String optionsStr = optionsList.stream()
                                .map(Object::toString)
                                .map(option -> option.replaceFirst(": ", ":")) // 去掉冒号后的第一个空格
                                .collect(Collectors.joining("|"));
                        dataInfo.setOptions(optionsStr);
                    }
                } else {
                    dataInfo.setOptions(null);
                }
                returnVal.add(dataInfo);
                dataInfoMapper.insert(dataInfo);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析算法返回结果失败", e);
        } catch (Exception e) {
            throw new RuntimeException("解析或保存数据失败", e);
        }
        return returnVal;
    }
}

