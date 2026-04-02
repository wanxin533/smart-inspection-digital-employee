package com.smartinspection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("inspection_task")
public class InspectionTask implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String taskNo;

    private String name;

    private String type;

    private String status;

    private Long deviceId;

    private String deviceName;

    private Long assigneeId;

    private String assigneeName;

    private LocalDateTime planTime;

    private LocalDateTime actualTime;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
