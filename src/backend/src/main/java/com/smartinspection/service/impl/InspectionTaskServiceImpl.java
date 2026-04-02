package com.smartinspection.service.impl;

import com.smartinspection.entity.InspectionTask;
import com.smartinspection.mapper.InspectionTaskMapper;
import com.smartinspection.service.InspectionTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class InspectionTaskServiceImpl extends ServiceImpl<InspectionTaskMapper, InspectionTask> implements InspectionTaskService {

    @Override
    public List<InspectionTask> listByStatus(String status) {
        return list(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<InspectionTask>()
            .eq(InspectionTask::getStatus, status));
    }

    @Override
    public Map<String, Object> statistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("total", count());
        result.put("pending", count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<InspectionTask>()
            .eq(InspectionTask::getStatus, "pending")));
        result.put("in_progress", count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<InspectionTask>()
            .eq(InspectionTask::getStatus, "in_progress")));
        result.put("completed", count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<InspectionTask>()
            .eq(InspectionTask::getStatus, "completed")));
        return result;
    }
}
