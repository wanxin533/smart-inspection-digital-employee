package com.smartinspection.service.impl;

import com.smartinspection.entity.DefectRecord;
import com.smartinspection.mapper.DefectRecordMapper;
import com.smartinspection.service.DefectRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DefectRecordServiceImpl extends ServiceImpl<DefectRecordMapper, DefectRecord> implements DefectRecordService {

    @Override
    public List<DefectRecord> listByTaskId(String taskId) {
        // 缺陷记录不直接关联任务 ID，按设备 ID 查询
        return list(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<DefectRecord>()
            .eq(DefectRecord::getDeviceId, taskId));
    }

    @Override
    public Map<String, Object> statistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("total", count());
        result.put("pending", count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<DefectRecord>()
            .eq(DefectRecord::getStatus, "pending")));
        result.put("resolved", count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<DefectRecord>()
            .eq(DefectRecord::getStatus, "resolved")));
        return result;
    }
}
