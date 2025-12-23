package com.example.llmtest.utils;

import com.example.llmtest.pojo.entity.DataInfo;
import com.example.llmtest.pojo.vo.DataInfoVO;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Component
public class CustomUtil {

    // 维度映射
    private final BiMap<String, String> dimensionMap = HashBiMap.create(Map.of(
            "性能","performance",
            "可靠性","reliability",
            "安全性","safety",
            "公平性","fairness"
    ));
    //变形方法映射
    private final BiMap<String, String> transformationTypeMap = HashBiMap.create(Map.of(
            "问法改写","rewrite",
            "添加噪音","add_noise",
            "反向极化","reverse_polarity",
            "表达复杂化","complicate",
            "同义替代","substitute"
    ));
    // 指标映射
    private final BiMap<String, String> metricMap = HashBiMap.create();
    // 子指标映射
    private final BiMap<String, String> subMetricMap = HashBiMap.create(Map.of(
            "数学推理","mathematical_reasoning",
            "常识逻辑推理","common_sense_logical_reasoning",
            "因果推理","casual_reasoning",
            "信息提取","information_extraction",
            "上下文关联","contextual_relevance",
            "记忆能力","memory_ability"
    ));
    // 题型映射
    private final BiMap<String, Integer> questionTypeMap = HashBiMap.create(Map.of(
            "choice",0,
            "judgment",1,
            "short_answer",2,
            "only_question",3,
            "compare_question",4
    ));

    // 测试指标映射（性能分为三个指标）
    private final BiMap<String, String> TestMap = HashBiMap.create(Map.of(
            "系统响应效率", "system_responsiveness",
            "复杂推理能力", "complex_reasoning_skill",
            "长文本理解能力", "long_text_comprehension_skill",
            "可靠性","reliability",
            "安全性","safety",
            "公平性","fairness"
    ));
    // 构造函数中初始化 metricMap
    public CustomUtil() {
        metricMap.put("系统响应效率", "system_responsiveness");
        metricMap.put("复杂推理能力", "complex_reasoning_skill");
        metricMap.put("长文本理解能力", "long_text_comprehension_skill");

        metricMap.put("准确性", "accuracy");
        metricMap.put("鲁棒性", "robustness");
        metricMap.put("一致性", "consistency");
        metricMap.put("稳定性", "stability");

        metricMap.put("随机生成样本", "randomly_generated_samples");
        metricMap.put("指令挟持", "command_hijacking");
        metricMap.put("越狱攻击", "jailbreak_attacks");
        metricMap.put("内容扭曲", "content_distortions");
        metricMap.put("提示屏蔽", "prompt_blocking");
        metricMap.put("干扰对话", "disrupt_conversations");
        metricMap.put("黑盒", "black_box");
        metricMap.put("白盒", "white_box");

        metricMap.put("性别", "gender");
        metricMap.put("种族", "race");
        metricMap.put("年龄", "age");
        metricMap.put("宗教", "religion");
        metricMap.put("政治", "politics");
    }

    //字符串转数组
    public String[] parseStringToArray(String optionsString) {
        if (optionsString == null || optionsString.isEmpty()) {
            return null;
        }
        String[] options = optionsString.split("\\|");
        for (int i = 0; i < options.length; i++) {
            options[i] = options[i].replaceFirst(":", ": ");
        }
        return options;
    }

    public String parseArrayToString(Object optionsObj) {
        if (optionsObj == null) return null;
        if (optionsObj instanceof String) return (String) optionsObj;
        if (optionsObj instanceof List<?> optionsList) {
            if (optionsList.isEmpty()) return null;
            return optionsList.stream()
                    .map(Object::toString)
                    .map(option -> option.replaceFirst(": ", ":"))
                    .collect(Collectors.joining("|"));
        }
        return null;
    }

    /**
     * 将DataInfo转换为DataInfoVO
     * @param dataInfo
     * @return DataInfoVO
     */
    public DataInfoVO convertToVO(DataInfo dataInfo) {
        return DataInfoVO.builder()
                .dataId(dataInfo.getDataId())
                .question(dataInfo.getQuestion())
                .options(dataInfo.getOptions())
                .answer(dataInfo.getAnswer())
                .dimension(dataInfo.getDimension())
                .questionType(dataInfo.getQuestionType())
                .dataSource(dataInfo.getDataSource())
                .updateTime(dataInfo.getUpdateTime())
                .transformationType(dataInfo.getTransformationType())
                .transformationDescription(dataInfo.getTransformationDescription())
                .originalDataId(dataInfo.getOriginalDataId())
                .build();
    }

/**
 * 根据给定的维度名称获取对应的指标列表
 * @param dimension 维度名称，如"performance"、"reliability"等
 * @return 返回与给定维度相关的指标列表，如果维度不存在则返回空列表
 */
    public List<String> getDimensionMetrics(String dimension) {
    // 使用switch表达式根据不同的维度返回对应的指标列表
        return switch (dimension) {
        // 性能维度对应的指标
            case "performance" -> List.of("system_responsiveness", "complex_reasoning_skill", "long_text_comprehension_skill");
        // 可靠性维度对应的指标
            case "reliability" -> List.of("accuracy", "robustness", "consistency", "stability");
        // 安全性维度对应的指标
            case "safety" -> List.of("randomly_generated_samples", "command_hijacking", "jailbreak_attacks",
                    "content_distortions", "prompt_blocking", "disrupt_conversations",
                    "black_box", "white_box");
        // 公平性维度对应的指标
            case "fairness" -> List.of("gender", "race", "age", "religion", "politics");
        // 默认情况返回空列表
            default -> Collections.emptyList();
        };
    }

}

