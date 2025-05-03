package com.example.llmtest.service;

import com.example.llmtest.entity.DataInfo;

import java.util.List;

public interface DataInfoService {
    List<DataInfo> getAllDataInfos();

    List<DataInfo> getDataInfosWithModelName();
}
