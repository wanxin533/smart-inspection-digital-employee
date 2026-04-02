package com.smartinspection.service;

import com.smartinspection.entity.DefectRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

public interface DefectRecordService extends IService<DefectRecord> {
    List<DefectRecord> listByTaskId(String taskId);
    Map<String, Object> statistics();
}
