package com.example.llmtest.controller;


import com.example.llmtest.entity.DataInfo;
import com.example.llmtest.service.DataInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

@RestController
@RequestMapping("/dataInfo/select")
public class DataInfoController {

    @Autowired
    private DataInfoService dataInfoService;

    @GetMapping
    public List<DataInfo> getAllDataInfos() {

        return dataInfoService.getDataInfosWithModelName();
    }
}
