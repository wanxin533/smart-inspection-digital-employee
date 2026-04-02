package com.smartinspection.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartinspection.entity.WorkOrder;
import com.smartinspection.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @GetMapping
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "20") Integer size) {
        Page<WorkOrder> pageInfo = new Page<>(page, size);
        Page<WorkOrder> result = workOrderService.page(pageInfo);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("data", Map.of(
            "list", result.getRecords(),
            "total", result.getTotal()
        ));
        return response;
    }

    @GetMapping("/{id}")
    public Map<String, Object> detail(@PathVariable Long id) {
        WorkOrder order = workOrderService.getById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("data", order);
        return response;
    }
}
