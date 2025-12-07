package com.example.llmtest.controller;

import com.example.llmtest.exceptionhandler.R;
import com.example.llmtest.pojo.dto.TestDTO;
import com.example.llmtest.pojo.entity.DataInfo;
import com.example.llmtest.pojo.vo.DataInfoVO;
import com.example.llmtest.pojo.vo.TestVO;
import com.example.llmtest.service.PerformanceTestService;
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
@RequestMapping("/test/performance")
@Slf4j
public class PerformanceTestController {

    private final PerformanceTestService performanceTestService;
    private final TestDataGenerator testDataGenerator;
    public PerformanceTestController(PerformanceTestService performanceTestService,
                                     TestDataGenerator testDataGenerator) {
        this.performanceTestService = performanceTestService;
        this.testDataGenerator = testDataGenerator;
    }

    @Operation(summary = "系统响应效率测试")
    @PostMapping("/metric1")
    public R<TestVO> metricOneTest(@RequestBody TestDTO dto) {
        TestVO vo = performanceTestService.metricOneTest(dto);
        return R.success(vo);
    }

    @PostMapping("/testDataGenerator")
    public R<List<DataInfoVO>> generate(String dimension, String metric, Integer totalQuestions) {
        List<DataInfoVO> list = testDataGenerator.generateData(dimension, metric, totalQuestions);
        return R.success(list);
    }
}
