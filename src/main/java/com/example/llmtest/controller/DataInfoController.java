package com.example.llmtest.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.llmtest.pojo.dto.DataInfoPageQueryDTO;
import com.example.llmtest.pojo.vo.DataInfoVO;
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
    public DataInfoController(DataInfoService dataInfoService) {
        this.dataInfoService = dataInfoService;
    }

    @Operation(summary = "根据条件分页查询题目")
    @GetMapping("/select/pages")
    public R<IPage<DataInfo>> getDataInfoByConditionsByPage(
            @Parameter(description = "查询条件")
            DataInfoPageQueryDTO queryDTO) {
        IPage<DataInfo> result = dataInfoService.getDataInfoByConditions(queryDTO);
        return R.success(result);
    }

    @Operation(summary = "根据dataId来查询某道题")
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
