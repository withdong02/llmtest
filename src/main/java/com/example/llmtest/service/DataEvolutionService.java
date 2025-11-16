package com.example.llmtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.llmtest.pojo.entity.DataInfo;

import java.util.List;


public interface DataEvolutionService extends IService<DataInfo> {
    /**
     * 题目变形
     * @param dataIds
     * @param transformationType
     * @return
     */
    List<DataInfo> evolveByModel(List<Long> dataIds, String transformationType);
}
