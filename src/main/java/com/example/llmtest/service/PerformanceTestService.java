package com.example.llmtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.llmtest.pojo.dto.TestDTO;
import com.example.llmtest.pojo.entity.TestInfo;
import com.example.llmtest.pojo.vo.TestVO;

public interface PerformanceTestService extends IService<TestInfo> {

    /**
     * 系统响应效率测试
     * @param dto
     * @return
     */
    TestVO metricOneTest(TestDTO dto);
}
