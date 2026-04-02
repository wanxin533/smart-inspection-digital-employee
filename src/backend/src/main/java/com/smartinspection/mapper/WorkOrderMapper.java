package com.smartinspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartinspection.entity.WorkOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {
}
