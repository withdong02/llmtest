package com.example.llmtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.llmtest.pojo.dto.TestDTO;
import com.example.llmtest.pojo.entity.TestInfo;
import com.example.llmtest.pojo.vo.TestResultVO;

public interface TestService extends IService<TestInfo> {

    /**
     * 系统响应效率测试
     * @param dto
     * @return
     */
    TestResultVO srTest(TestDTO dto);

    /**
     * 其他指标测试
     * @param dto
     * @return
     */
    TestResultVO otherTest(TestDTO dto);
}
