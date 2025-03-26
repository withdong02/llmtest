package com.example.llmtest.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.llmtest.entity.DataInfo;

public interface DataInfoService extends IService<DataInfo> {
    IPage<DataInfo> getDataInfoByPage(int pageNum);
    IPage<DataInfo> getDataInfoByQuestionTypeByPage(int pageNum, String questionType);
    DataInfo getDataInfoByDataId(Long dataId);
    DataInfo getDataInfoByDisplayId(Long displayId);
    boolean updateDataInfo(DataInfo dataInfo);

    boolean deleteDataInfoByDisplayId(Long displayId);
}
