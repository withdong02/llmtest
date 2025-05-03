package com.example.llmtest.service.impl;

import com.example.llmtest.entity.DataInfo;
import com.example.llmtest.mapper.DataInfoMapper;
import com.example.llmtest.service.DataInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.baomidou.mybatisplus.extension.toolkit.SimpleQuery.list;


@Service
public class DataInfoServiceImpl implements DataInfoService {

    @Autowired
    private DataInfoMapper mapper;

    @Override
    public List<DataInfo> getAllDataInfos() {
        return mapper.selectList(null);
    }

    @Override
    public List<DataInfo> getDataInfosWithModelName() {
        return mapper.selectDataInfosWithModelName();
    }



}
