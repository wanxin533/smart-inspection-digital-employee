package com.smartinspection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("defect_record")
public class DefectRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String defectNo;

    private String type;

    private String level;

    private String status;

    private Long deviceId;

    private String deviceName;

    private String description;

    private BigDecimal temperature;

    private String photos;

    private Long reporterId;

    private String reporterName;

    private LocalDateTime reportTime;

    private Long auditorId;

    private String auditOpinion;

    private LocalDateTime auditTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
