package com.example.llmtest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.llmtest.entity.DataInfo;
import com.example.llmtest.mapper.DataInfoMapper;
import com.example.llmtest.service.DataInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.baomidou.mybatisplus.extension.toolkit.SimpleQuery.list;


@Service
public class DataInfoServiceImpl extends ServiceImpl<DataInfoMapper, DataInfo> implements DataInfoService {

    @Autowired
    private DataInfoMapper dataInfoMapper;


    @Override
    public IPage<DataInfo> getDataInfoByPage(int pageNum) {
        Page<DataInfo> page = new Page<>(pageNum, 15);
        QueryWrapper<DataInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",0);
        //queryWrapper.lt("data_id",10);
        return this.page(page, queryWrapper);
    }

    @Override
    public IPage<DataInfo> getDataInfoByQuestionTypeByPage(int pageNum, String questionType) {
        Page<DataInfo> page = new Page<>(pageNum, 15);
        QueryWrapper<DataInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("data_id",150).eq("question_type", questionType);
        return this.page(page, queryWrapper);
    }

    @Override
    public DataInfo getDataInfoByDataId(Long dataId) {
        return dataInfoMapper.selectById(dataId);
    }

    @Override
    public DataInfo getDataInfoByDisplayId(Long displayId) {return dataInfoMapper.selectByDisplayId(displayId);}

    @Override
    public boolean updateDataInfo(DataInfo dataInfo) {
        return dataInfoMapper.updateById(dataInfo) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDataInfoByDisplayId(Long displayId) {
        QueryWrapper<DataInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("display_id", displayId);
        DataInfo dataInfo = dataInfoMapper.selectOne(wrapper);
        if (dataInfo == null) {
            return false;
        }
        UpdateWrapper<DataInfo> updateWrapper = new UpdateWrapper<>();updateWrapper.eq("display_id", displayId).eq("is_deleted",0).set("is_deleted", 1);int rows = dataInfoMapper.update(null, updateWrapper);
        // 调整 displayId 连续性
        if (rows > 0) {
            dataInfoMapper.adjustDisplayIdsAfterDelete(displayId);
        }
        return rows > 0;

    }
}
