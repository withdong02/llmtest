package com.example.llmtest.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.llmtest.mapper.DataInfoMapper;
import com.example.llmtest.pojo.entity.DataInfo;
import com.example.llmtest.pojo.enums.TransformationTypeEnum;
import com.example.llmtest.service.DataEvolutionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataEvolutionServiceImpl extends ServiceImpl<DataInfoMapper, DataInfo> implements DataEvolutionService {

    private static final String FLASK_URL = "https://www.u659522.nyat.app:26249/evolve";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;

    public DataEvolutionServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 题目变形
     * @param dataIds
     * @param transformationType
     * @return
     */
    @Override
    public List<DataInfo> evolveByModel(List<Long> dataIds, String transformationType) {
        List<DataInfo> returnVal = new ArrayList<>();
        for (Long dataId : dataIds) {
            DataInfo dataInfo = baseMapper.selectById(dataId);
            //构造请求体
            HashMap<String, Object> content = new HashMap<>();
            content.put("rowIdx", 0);
            content.put("question", dataInfo.getQuestion());
            if (dataInfo.getOptions() != null && !dataInfo.getOptions().isEmpty()) {
                String optionsString = dataInfo.getOptions();
                String[] options = optionsString.split("\\|");
                for (int i = 0; i < options.length; i++) {
                    options[i] = options[i].replaceFirst(":", ": ");
                }
                content.put("options", options);
            }
            content.put("answer", dataInfo.getAnswer());
            content.put("type_index", dataInfo.getQuestionType().getValue());
            System.out.println(content);
            HashMap<String, Object> requestBody = new HashMap<>();
            requestBody.put("question0", content);
            requestBody.put("evolve_type", transformationType);
            //构造请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);


            // 发送请求
            ResponseEntity<String> response = restTemplate.postForEntity(FLASK_URL, requestEntity, String.class);
            System.out.println(response);
            List<Map<String, Object>> allResult;
            DataInfo newData;
            try {
                // 解析响应
                //String innerJson = objectMapper.readValue(response.getBody(), String.class);
                allResult = objectMapper.readValue(response.getBody(), new TypeReference<>() {
                });
                String str = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(allResult);
                System.out.println(str);
                Map<String, Object> result = allResult.get(0);
                // 插入数据库
                newData = new DataInfo();
                BeanUtils.copyProperties(dataInfo, newData);
                newData.setDataId(null);
                newData.setQuestion(String.valueOf(result.get("question")));
                newData.setAnswer(String.valueOf(result.get("answer")));
                //只有选择题才有选项
                if (result.containsKey("options")) {
                    Object optionsObj = result.get("options");
                    if (optionsObj instanceof List<?>) {
                        List<?> optionsList = (List<?>) optionsObj;
                        String optionsStr = optionsList.stream()
                                .map(Object::toString)
                                .map(option -> option.replaceFirst(": ", ":")) // 去掉冒号后的第一个空格
                                .collect(Collectors.joining("|"));
                        newData.setOptions(optionsStr);
                    }
                } else {
                    newData.setOptions(null);
                }
                newData.setIsTransformed(1);
                newData.setTransformationType(TransformationTypeEnum.valueOf(transformationType.toUpperCase()));
                newData.setTransformationDescription(String.valueOf(result.get("process")));
                baseMapper.insert(newData);
                returnVal.add(newData);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("解析算法返回结果失败", e);
            } catch (Exception e) {
                throw new RuntimeException("解析或保存数据失败", e);
            }
        }
        return returnVal;
    }
}
