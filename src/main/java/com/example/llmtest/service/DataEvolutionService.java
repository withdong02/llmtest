package com.example.llmtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.llmtest.pojo.dto.EvolutionDTO;
import com.example.llmtest.pojo.entity.DataInfo;

import java.util.List;


public interface DataEvolutionService extends IService<DataInfo> {
    /**
     * 题目变形
     * @param dto
     * @return
     */
    List<DataInfo> evolveByModel(EvolutionDTO dto);
}
