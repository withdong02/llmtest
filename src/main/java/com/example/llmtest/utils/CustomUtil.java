package com.example.llmtest.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Configuration
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

}

