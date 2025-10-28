package com.example.llmtest.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.llmtest.utils.ETCMappingUtil;
import com.example.llmtest.pojo.entity.DataInfo;
import com.example.llmtest.pojo.enums.QuestionTypeEnum;
import com.example.llmtest.exceptionhandler.R;
import com.example.llmtest.exceptionhandler.ReturnCode;
import com.example.llmtest.service.DataInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "DataInfoController", description = "有关题库的所有操作")
@RestController
@RequestMapping("/dataInfo")

public class DataInfoController {

    private final DataInfoService dataInfoService;
    private final ETCMappingUtil mappingUtil;
    public DataInfoController(DataInfoService dataInfoService,  ETCMappingUtil mappingUtil) {
        this.dataInfoService = dataInfoService;
        this.mappingUtil = mappingUtil;
    }

    @Operation(summary = "分页查询题目")
    @GetMapping("/select/page")
    public R<IPage<DataInfo>> getDataInfoByPage(
            @Parameter(description = "分页页码")
            @RequestParam int pageNum) {
        return R.success(dataInfoService.getDataInfoByPage(pageNum));
    }

    @Operation(summary = "根据题目类型分页查询题目")
    @GetMapping("/select/pageByType")
    public R<IPage<DataInfo>> getDataInfoByQuestionTypeByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "题目类型")
            @RequestParam String questionType) {
        if (!QuestionTypeEnum.contains(questionType)|| questionType == null) {
            return R.error(ReturnCode.RC400.getCode(), "题目类型不支持");
        }
        return R.success(dataInfoService.getDataInfoByQuestionTypeByPage(pageNum,questionType));
    }

    @Operation(summary = "根据题目的维度和指标分页查询题目")
    @GetMapping(value = "/select/pageByContent")
    public R<IPage<DataInfo>> getDataInfoByContentByType(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam String dimension,
            @RequestParam String metric,
            @RequestParam(required = false) String subMetric) {
        if (dimension == null || metric == null) {
            return R.error(ReturnCode.RC400.getCode(), "维度或指标不能不应为空");
        }
        Map<String, String> dimensionMap = mappingUtil.getDimensionMap();
        Map<String, String> metricMap = mappingUtil.getMetricMap();
        Map<String, String> subMetricMap = mappingUtil.getSubMetricMap();
        if (!dimensionMap.containsValue(dimension)||  !metricMap.containsValue(metric)) {
            return R.error(ReturnCode.RC400.getCode(), "维度或指标不支持");
        }
        if (subMetric != null && !subMetric.isEmpty() && !subMetricMap.containsValue(subMetric)) {
            return R.error(ReturnCode.RC400.getCode(), "子指标不支持");
        }
        IPage<DataInfo> result;
        //String dimensionEn = dimensionMap.get(dimension);
        //String metricEn = metricMap.get(metric);
        if (subMetric == null) {
            result = dataInfoService.getDataInfoByPartContentByPage(pageNum, dimension, metric);
        } else {
            //String subMetricEn = subMetricMap.get(subMetric);
            result = dataInfoService.getDataInfoByAllContentByPage(pageNum, dimension, metric, subMetric);
        }
        return  R.success(result);
    }

    @Operation(summary = "根据题目displayId来查询某道题")
    @GetMapping("/select/{dataId}")
    public R<DataInfo> getDataInfoByDataId(@PathVariable Long dataId) {
        DataInfo dataInfo = dataInfoService.getDataInfoByDataId(dataId);
        if (dataInfo == null) {
            return R.error(ReturnCode.RC404.getCode(), ReturnCode.RC404.getMsg()); // 如果未找到数据，返回404
        }
        return R.success(dataInfo);
    }

    @Operation(summary = "对题目编辑操作，待定")
    @PutMapping
    public boolean updateDataInfo(@RequestBody DataInfo dataInfo) {
        return dataInfoService.updateDataInfo(dataInfo);
    }

    @Operation(summary = "根据题目dataId删除某道题")
    @DeleteMapping("/delete/{dataId}")
    public R<Boolean>deleteDataInfoByDataId(@PathVariable Long dataId) {
        DataInfo dataInfo = dataInfoService.getDataInfoByDataId(dataId);
        if (dataInfo == null) {
            return R.error(ReturnCode.RC404.getCode(), ReturnCode.RC404.getMsg()); // 如果未找到数据，返回404
        } else  {
            return R.success(dataInfoService.deleteDataInfoByDataId(dataId));
        }
    }
}
