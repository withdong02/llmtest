package com.example.llmtest.controller;

import com.example.llmtest.exceptionhandler.R;
import com.example.llmtest.pojo.dto.DGFromModelDTO;
import com.example.llmtest.pojo.entity.DataInfo;
import com.example.llmtest.service.DataEvolutionFromModelService;
import com.example.llmtest.service.DataGenerationFromModelService;
import com.example.llmtest.service.impl.DataGenerationFromModelServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "DataEvolutionFromModelController:题目变形")
@RestController
@RequestMapping("/dataInfo/dataEvolution")
@Slf4j
public class DataEvolutionFromModelController {
    private final DataEvolutionFromModelService dataEvolutionFromModelService;

    public DataEvolutionFromModelController(DataEvolutionFromModelService dataEvolutionFromModelService) {
        this.dataEvolutionFromModelService = dataEvolutionFromModelService;
    }

    @Operation(summary = "题目变形")
    @PostMapping
    public R<DataInfo> handleUserInput(Long dataId, String transformationType) {
        DataInfo dataInfo = dataEvolutionFromModelService.processAndSave(dataId, transformationType);
        return R.success(dataInfo);
    }
}
