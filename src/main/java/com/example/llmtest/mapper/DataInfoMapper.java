package com.example.llmtest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.llmtest.entity.DataInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DataInfoMapper extends BaseMapper<DataInfo> {
    @Select("SELECT di.question,answer,data_id,data_source FROM data_info di where data_id < 5")
    List<DataInfo> selectDataInfosWithModelName();
}
