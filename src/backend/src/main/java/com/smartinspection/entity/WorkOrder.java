package com.smartinspection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("work_order")
public class WorkOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private Long defectId;

    private String type;

    private String priority;

    private String status;

    private Long assigneeId;

    private String assigneeName;

    private String description;

    private LocalDateTime requireTime;

    private String processResult;

    private String photos;

    private Long processorId;

    private LocalDateTime processTime;

    private String acceptOpinion;

    private Long acceptorId;

    private LocalDateTime acceptTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
