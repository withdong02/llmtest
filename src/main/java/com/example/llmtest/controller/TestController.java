package com.example.llmtest.controller;

import com.example.llmtest.exceptionhandler.R;
import com.example.llmtest.pojo.dto.TestDTO;
import com.example.llmtest.pojo.vo.DataInfoVO;
import com.example.llmtest.pojo.vo.TestResultVO;
import com.example.llmtest.service.TestService;
import com.example.llmtest.utils.TestDataGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "性能测试")
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    private final TestService testService;
    private final TestDataGenerator testDataGenerator;
    public TestController(TestService testService,
                          TestDataGenerator testDataGenerator) {
        this.testService = testService;
        this.testDataGenerator = testDataGenerator;
    }

    @Operation(summary = "系统响应效率测试")
    @PostMapping("/performance/metric1")
    public R<TestResultVO> metricOneTest(@RequestBody TestDTO dto) {
        TestResultVO vo = testService.metricOneTest(dto);
        return R.success(vo);
    }

    @PostMapping("/autoGenerate")
    public R<List<DataInfoVO>> generate(String dimension, String metric, Integer totalQuestions) {
        List<DataInfoVO> list = testDataGenerator.generateData(dimension, metric, totalQuestions);
        return R.success(list);
    }
}
