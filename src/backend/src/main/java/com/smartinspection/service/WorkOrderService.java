package com.smartinspection.service;

import com.smartinspection.entity.WorkOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

public interface WorkOrderService extends IService<WorkOrder> {
    List<WorkOrder> listByStatus(String status);
    Map<String, Object> statistics();
}
