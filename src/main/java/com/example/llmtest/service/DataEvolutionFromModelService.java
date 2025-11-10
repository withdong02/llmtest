package com.example.llmtest.service;

import com.example.llmtest.pojo.entity.DataInfo;


public interface DataEvolutionFromModelService {
    /**
     * 题目变形
     * @param dataId
     * @param transformationType
     * @return
     */
    DataInfo processAndSave(Long dataId, String transformationType);
}
