package com.example.llmtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.llmtest.pojo.dto.TransformationDTO;
import com.example.llmtest.pojo.entity.DataInfo;

import java.util.List;


public interface DataTransformationService extends IService<DataInfo> {

    /**
     * 题目变形
     * @param dto
     * @return
     */
    List<DataInfo> transformByModel(TransformationDTO dto);
}
