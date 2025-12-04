package com.example.llmtest.controller;

import com.example.llmtest.exceptionhandler.R;
import com.example.llmtest.pojo.dto.TestDTO;
import com.example.llmtest.pojo.vo.TestVO;
import com.example.llmtest.service.PerformanceTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "性能测试")
@RestController
@RequestMapping("/test/performance")
@Slf4j
public class PerformanceTestController {

    private final PerformanceTestService performanceTestService;
    public PerformanceTestController(PerformanceTestService performanceTestService) {
        this.performanceTestService = performanceTestService;
    }

    @Operation(summary = "系统响应效率测试")
    @PostMapping("/metric1")
    public R<TestVO> metricOneTest(@RequestBody TestDTO dto) {
        TestVO vo = performanceTestService.metricOneTest(dto);
        return R.success(vo);
    }
}
