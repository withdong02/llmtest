package com.example.llmtest.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.llmtest.entity.DataInfo;
import com.example.llmtest.entity.enums.QuestionTypeEnum;
import com.example.llmtest.exceptionhandler.R;
import com.example.llmtest.exceptionhandler.ReturnCode;
import com.example.llmtest.service.DataInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "DataInfoController:有关题库的所有操作")
@RestController
@RequestMapping("/dataInfo")
public class DataInfoController {

    @Autowired
    private DataInfoService dataInfoService;


    @Operation(summary = "分页查询所有题目，有一个参数")
    @GetMapping(value = "/select/page")
    public R<IPage<DataInfo>> getDataInfoByPage(
            @Parameter(name = "pageNum", description = "分页页码")
            @RequestParam() int pageNum) {
        return R.success(dataInfoService.getDataInfoByPage(pageNum));
    }

    @Operation(summary = "根据题目类型分页查询所有题目，有两个参数")
    @GetMapping(value = "/select/pageByType")
    public R<IPage<DataInfo>> getDataInfoByQuestionTypeByPage(

            @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(name = "questionType", description = "题目类型")
            @RequestParam String questionType) {
        if (!QuestionTypeEnum.contains(questionType)|| questionType == null) {
            return R.error(ReturnCode.RC400.getCode(), ReturnCode.RC400.getMsg());
        }
        return R.success(dataInfoService.getDataInfoByQuestionTypeByPage(pageNum,questionType));
    }

    @Operation(summary = "根据题目displayId来查询某道题，有一个参数")
    @GetMapping("/select/{displayId}")
    public R<DataInfo> getDataInfoByDisplayId(
            @Parameter(name = "displayId", description = "题目displayId")
            @PathVariable Long displayId) {
        DataInfo dataInfo = dataInfoService.getDataInfoByDisplayId(displayId);
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

    @Operation(summary = "根据题目displayId删除某道题")
    @DeleteMapping("/delete/{displayId}")
    public R<Boolean>deleteDataInfoByDisplayId(@PathVariable Long displayId) {
        if (!dataInfoService.deleteDataInfoByDisplayId(displayId)) {
            return R.error(ReturnCode.RC404.getCode(), ReturnCode.RC404.getMsg());
        }
        return R.success(true);
    }

}
