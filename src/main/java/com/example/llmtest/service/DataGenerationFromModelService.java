package com.example.llmtest.service;

import com.example.llmtest.exceptionhandler.R;
import com.example.llmtest.pojo.dto.DGFromModelDTO;
import com.example.llmtest.pojo.entity.DataInfo;

import java.util.List;

public interface DataGenerationFromModelService {
    /**
     * 智能生成接口
     * @param dto
     * @return
     */
    public List<DataInfo> processAndSave(DGFromModelDTO dto);
}
