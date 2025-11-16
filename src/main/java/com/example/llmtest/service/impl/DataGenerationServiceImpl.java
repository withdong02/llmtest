package com.example.llmtest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.llmtest.exceptionhandler.BusinessException;
import com.example.llmtest.exceptionhandler.ReturnCode;
import com.example.llmtest.mapper.ModelMapper;
import com.example.llmtest.pojo.dto.GenerationByHandDTO;
import com.example.llmtest.pojo.dto.GenerationByModelDTO;
import com.example.llmtest.service.DataGenerationService;
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
public class DataGenerationServiceImpl extends ServiceImpl<DataInfoMapper, DataInfo> implements DataGenerationService {

    private static final String FLASK_URL = "https://www.u659522.nyat.app:26249/generate";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;
    private final ETCMappingUtil mappingUtil;
    private final MetricMapper  metricMapper;
    private final SubMetricMapper subMetricMapper;
    private final ModelMapper modelMapper;

    public DataGenerationServiceImpl(RestTemplate restTemplate, ETCMappingUtil mappingUtil,
                                     MetricMapper metricMapper,
                                     SubMetricMapper subMetricMapper, ModelMapper modelMapper) {
        this.restTemplate = restTemplate;
        this.mappingUtil = mappingUtil;
        this.metricMapper = metricMapper;
        this.subMetricMapper = subMetricMapper;
        this.modelMapper = modelMapper;

    }

    /**
     * 模型智能生成
     * @param dto
     * @return
     */
    @Transactional
    public List<DataInfo> generateByModel(GenerationByModelDTO dto) {
        if ((dto.getMetric() == null || dto.getMetric().isEmpty())
                && (dto.getSubMetric() != null && !dto.getSubMetric().isEmpty())) {
            throw new BusinessException(ReturnCode.RC400.getCode(), "子指标存在，指标不存在");
        }
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("modelName", dto.getModelName());
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
                DataInfo dataInfo = DataInfo.builder()
                        .dimension(DimensionEnum.valueOf(dto.getDimension().toUpperCase()))
                        .dataSource(DataSourceEnum.MODEL_GENERATION)
                        .modelId(modelMapper.selectIdByName(dto.getModelName()))
                        .metricId(metricMapper.selectIdByName(dto.getMetric()))//没有就传入空
                        .subMetricId(subMetricMapper.selectIdByName(dto.getSubMetric()))
                        .questionType(QuestionTypeEnum.valueOf(((String)item.get("type_index")).toUpperCase()))
                        .question(String.valueOf(item.get("question")))
                        .answer(String.valueOf(item.get("answer")))
                        .build();
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
            }
            this.saveBatch(returnVal);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析算法返回结果失败", e);
        } catch (Exception e) {
            throw new RuntimeException("解析或保存数据失败", e);
        }
        return returnVal;
    }

    /**
     * 手动录入
     * @param dto
     * @return
     */
    @Transactional
    public Boolean generateByHand(GenerationByHandDTO dto) {
        String options = null;
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) options = dto.getOptions();
        String subMetric = null;
        if (dto.getSubMetric() != null && !dto.getSubMetric().isEmpty()) subMetric = dto.getSubMetric();
        DataInfo dataInfo = DataInfo.builder()
                .question(dto.getQuestion())
                .options(options)
                .answer(dto.getAnswer())
                .dimension(DimensionEnum.valueOf(dto.getDimension().toUpperCase()))
                .questionType(QuestionTypeEnum.valueOf(dto.getQuestionType().toUpperCase()))
                .dataSource(DataSourceEnum.INPUT)
                .metricId(metricMapper.selectIdByName(dto.getMetric()))
                .subMetricId(subMetricMapper.selectIdByName(subMetric))
                .build();
        return this.save(dataInfo);
    }
}

