package com.example.llmtest.controller;

import com.example.llmtest.exceptionhandler.R;
import com.example.llmtest.pojo.dto.DGFromModelDTO;
import com.example.llmtest.pojo.entity.DataInfo;
import com.example.llmtest.service.DataGenerationFromModelService;
import com.example.llmtest.service.impl.DataGenerationFromModelServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;

@Tag(name = "DataGenerationFromModelController:有关智能生成所有操作")
@RestController
@RequestMapping("/dataInfo/dataGeneration")
@Slf4j
public class DataGenerationFromModelController {
    private final DataGenerationFromModelService dataGenerationFromModelService;

    public DataGenerationFromModelController(DataGenerationFromModelService dataGenerationFromModelService) {
        this.dataGenerationFromModelService = dataGenerationFromModelService;
    }

    @Operation(summary = "智能生成")
    @PostMapping
    public R<List<DataInfo>> handleUserInput(@RequestBody DGFromModelDTO dto) {
        log.info("dto参数为{}", dto);
        List<DataInfo> dataInfo = dataGenerationFromModelService.processAndSave(dto);
        return R.success(dataInfo);
    }
}
