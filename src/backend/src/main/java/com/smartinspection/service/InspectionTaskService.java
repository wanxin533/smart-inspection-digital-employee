package com.smartinspection.service;

import com.smartinspection.entity.InspectionTask;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

public interface InspectionTaskService extends IService<InspectionTask> {
    List<InspectionTask> listByStatus(String status);
    Map<String, Object> statistics();
}
