package com.smartinspection.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartinspection.entity.DefectRecord;
import com.smartinspection.service.DefectRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/defects")
public class DefectController {

    @Autowired
    private DefectRecordService defectService;

    @GetMapping
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "20") Integer size) {
        Page<DefectRecord> pageInfo = new Page<>(page, size);
        Page<DefectRecord> result = defectService.page(pageInfo);
        
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
        DefectRecord defect = defectService.getById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("data", defect);
        return response;
    }
}
