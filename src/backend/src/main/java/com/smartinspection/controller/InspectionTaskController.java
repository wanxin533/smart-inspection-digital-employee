package com.smartinspection.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartinspection.entity.InspectionTask;
import com.smartinspection.service.InspectionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/inspection/tasks")
public class InspectionTaskController {

    @Autowired
    private InspectionTaskService taskService;

    @GetMapping
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "20") Integer size) {
        Page<InspectionTask> pageInfo = new Page<>(page, size);
        Page<InspectionTask> result = taskService.page(pageInfo);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("data", Map.of(
            "list", result.getRecords(),
            "total", result.getTotal(),
            "page", result.getCurrent(),
            "size", result.getSize()
        ));
        return response;
    }

    @GetMapping("/{id}")
    public Map<String, Object> detail(@PathVariable Long id) {
        InspectionTask task = taskService.getById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("data", task);
        return response;
    }
}
