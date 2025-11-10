package com.example.llmtest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.llmtest.exceptionhandler.BusinessException;
import com.example.llmtest.exceptionhandler.ReturnCode;
import com.example.llmtest.pojo.dto.DataInfoPageQueryDTO;
import com.example.llmtest.pojo.entity.DataInfo;
import com.example.llmtest.mapper.DataInfoMapper;
import com.example.llmtest.mapper.MetricMapper;
import com.example.llmtest.mapper.SubMetricMapper;
import com.example.llmtest.pojo.enums.QuestionTypeEnum;
import com.example.llmtest.service.DataInfoService;
import com.example.llmtest.utils.ETCMappingUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.baomidou.mybatisplus.extension.toolkit.SimpleQuery.list;


@Service
public class DataInfoServiceImpl extends ServiceImpl<DataInfoMapper, DataInfo> implements DataInfoService {

    private final DataInfoMapper dataInfoMapper;
    private final MetricMapper metricMapper;
    private final SubMetricMapper subMetricMapper;
    private final ETCMappingUtil mappingUtil;

    public DataInfoServiceImpl(DataInfoMapper dataInfoMapper, MetricMapper metricMapper,
                               SubMetricMapper subMetricMapper, ETCMappingUtil mappingUtil) {
        this.dataInfoMapper = dataInfoMapper;
        this.metricMapper = metricMapper;
        this.subMetricMapper = subMetricMapper;
        this.mappingUtil = mappingUtil;
    }

    /**
     * 根据条件分页查询题目
     * @param queryDTO
     * @return
     */
    @Override
    public IPage<DataInfo> getDataInfoByConditions(DataInfoPageQueryDTO queryDTO) {

        if (queryDTO.getSubMetric() != null && !queryDTO.getSubMetric().isEmpty()) {
            if (queryDTO.getMetric() == null || queryDTO.getMetric().isEmpty()) {
                throw new BusinessException(ReturnCode.RC400.getCode(), "当指定子指标时，指标不能为空");
            }
        }
        if (queryDTO.getQuestionType() != null && !queryDTO.getQuestionType().isEmpty()) {
            if (!QuestionTypeEnum.contains(queryDTO.getQuestionType())) {
                throw new BusinessException(ReturnCode.RC400.getCode(), "题型不存在");
            }
        }
        Map<String, String> dimensionMap = mappingUtil.getDimensionMap();
        Map<String, String> metricMap = mappingUtil.getMetricMap();
        Map<String, String> subMetricMap = mappingUtil.getSubMetricMap();
        if (queryDTO.getDimension() != null && !queryDTO.getDimension().isEmpty()) {
            if (!dimensionMap.containsValue(queryDTO.getDimension())) {
                throw new BusinessException(ReturnCode.RC400.getCode(), "维度不存在");
            }
        }
        if (queryDTO.getMetric() != null && !queryDTO.getMetric().isEmpty()) {
            if (!metricMap.containsValue(queryDTO.getMetric())) {
                throw new BusinessException(ReturnCode.RC400.getCode(), "指标不存在");
            }
        }
        if (queryDTO.getSubMetric() != null && !queryDTO.getSubMetric().isEmpty()) {
            if (!subMetricMap.containsValue(queryDTO.getSubMetric())) {
                throw new BusinessException(ReturnCode.RC400.getCode(), "子指标不存在");
            }
        }

        // 正常查询

        long pageNum = queryDTO.getPageNum() == null ? 1 : queryDTO.getPageNum();
        Page<DataInfo> page = new Page<>(pageNum, 15);
        QueryWrapper<DataInfo> queryWrapper = new QueryWrapper<>();
        if (queryDTO.getQuestionType() != null && !queryDTO.getQuestionType().isEmpty()) {
            queryWrapper.eq("question_type", queryDTO.getQuestionType());
        }
        if (queryDTO.getDimension() != null && !queryDTO.getDimension().isEmpty()) {
            queryWrapper.eq("dimension", queryDTO.getDimension());
        }
        if (queryDTO.getMetric() != null && !queryDTO.getMetric().isEmpty()) {
            Long metricId = metricMapper.selectIdByName(queryDTO.getMetric());
            if (metricId != null) {
                queryWrapper.eq("metric_id", metricId);
            }
        }
        if (queryDTO.getSubMetric() != null && !queryDTO.getSubMetric().isEmpty()) {
            Long subMetricId = subMetricMapper.selectIdByName(queryDTO.getSubMetric());
            if (subMetricId != null) {
                queryWrapper.eq("sub_metric_id", subMetricId);
            }
        }
        return this.page(page, queryWrapper);
    }

    /**
     * 根据da题目Id查询题目
     * @param dataId
     * @return
     */
    @Override
    public DataInfo getDataInfoByDataId(Long dataId) {
        return dataInfoMapper.selectById(dataId);
    }


    @Override
    public boolean updateDataInfo(DataInfo dataInfo) {
        return dataInfoMapper.updateById(dataInfo) > 0;
    }

    /**
     * 根据题目id删除题目
     * @param dataId
     * @return
     */
    @Override
    public boolean deleteDataInfoByDataId(Long dataId) {
        return dataInfoMapper.deleteById(dataId) > 0;
    }

}
