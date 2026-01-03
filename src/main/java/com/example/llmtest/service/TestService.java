package com.example.llmtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.llmtest.pojo.dto.TestDTO;
import com.example.llmtest.pojo.entity.TestInfo;
import com.example.llmtest.pojo.vo.TestResultVO;

public interface TestService extends IService<TestInfo> {

    /**
     * 系统响应效率和公平性测试
     * @return vo
     */
    TestResultVO questionTest(TestDTO dto);

    /**
     * 剩余指标测试
     * @return vo
     */
    TestResultVO metricTest(TestDTO dto);
}
