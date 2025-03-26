package com.example.llmtest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.llmtest.entity.DataInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DataInfoMapper extends BaseMapper<DataInfo> {

    @Select("SELECT * FROM data_info WHERE display_id = #{displayId} AND is_deleted = 0")
    DataInfo selectByDisplayId(Long displayId);

    @Update("UPDATE data_info SET display_id = display_id - 1 WHERE display_id > #{deletedDisplayId}")
    void adjustDisplayIdsAfterDelete(Long deletedDisplayId);
}
