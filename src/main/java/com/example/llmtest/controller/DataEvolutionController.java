package com.example.llmtest.controller;

import com.example.llmtest.exceptionhandler.R;
import com.example.llmtest.pojo.entity.DataInfo;
import com.example.llmtest.service.DataEvolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "DataEvolutionController:题目变形")
@RestController
@RequestMapping("/dataInfo/evolve")
@Slf4j
public class DataEvolutionController {
    private final DataEvolutionService dataEvolutionService;

    public DataEvolutionController(DataEvolutionService dataEvolutionService) {
        this.dataEvolutionService = dataEvolutionService;
    }

    @Operation(summary = "题目变形")
    @PostMapping
    public R<List<DataInfo>> evolve(List<Long> dataIds, String transformationType) {
        List<DataInfo> dataInfos = dataEvolutionService.evolveByModel(dataIds, transformationType);
        return R.success(dataInfos);
    }
}
