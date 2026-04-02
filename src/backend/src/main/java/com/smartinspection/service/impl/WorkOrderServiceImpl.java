package com.smartinspection.service.impl;

import com.smartinspection.entity.WorkOrder;
import com.smartinspection.mapper.WorkOrderMapper;
import com.smartinspection.service.WorkOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder> implements WorkOrderService {

    @Override
    public List<WorkOrder> listByStatus(String status) {
        return list(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<WorkOrder>()
            .eq(WorkOrder::getStatus, status));
    }

    @Override
    public Map<String, Object> statistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("total", count());
        result.put("pending", count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<WorkOrder>()
            .eq(WorkOrder::getStatus, "pending")));
        result.put("in_progress", count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<WorkOrder>()
            .eq(WorkOrder::getStatus, "in_progress")));
        result.put("completed", count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<WorkOrder>()
            .eq(WorkOrder::getStatus, "completed")));
        return result;
    }
}
