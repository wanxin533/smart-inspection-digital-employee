-- 智能巡视数字员工系统 - 数据库初始化脚本
-- MySQL 8.0+

CREATE DATABASE IF NOT EXISTS smart_inspection DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smart_inspection;

-- 1. 系统用户表
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    status TINYINT DEFAULT 1,
    dept_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 设备台账表
CREATE TABLE device_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_no VARCHAR(50) NOT NULL UNIQUE,
    device_name VARCHAR(100) NOT NULL,
    device_type VARCHAR(50),
    voltage_level VARCHAR(20),
    manufacturer VARCHAR(100),
    install_date DATE,
    install_location VARCHAR(200),
    status TINYINT DEFAULT 1,
    health_score INT DEFAULT 100,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 巡视任务表
CREATE TABLE inspection_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_no VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50),
    status VARCHAR(20) DEFAULT '待执行',
    device_id BIGINT,
    device_name VARCHAR(100),
    assignee_id BIGINT,
    assignee_name VARCHAR(50),
    plan_time DATETIME,
    actual_time DATETIME,
    remark VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 缺陷记录表
CREATE TABLE defect_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    defect_no VARCHAR(50) NOT NULL UNIQUE,
    type VARCHAR(50),
    level VARCHAR(20),
    status VARCHAR(20) DEFAULT '待审核',
    device_id BIGINT,
    device_name VARCHAR(100),
    description VARCHAR(1000),
    temperature DECIMAL(5,2),
    reporter_id BIGINT,
    reporter_name VARCHAR(50),
    report_time DATETIME,
    auditor_id BIGINT,
    audit_opinion VARCHAR(500),
    audit_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 工单表
CREATE TABLE work_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) NOT NULL UNIQUE,
    defect_id BIGINT,
    type VARCHAR(50),
    priority VARCHAR(20),
    status VARCHAR(20) DEFAULT '待处理',
    assignee_id BIGINT,
    assignee_name VARCHAR(50),
    description VARCHAR(1000),
    require_time DATETIME,
    process_result VARCHAR(1000),
    processor_id BIGINT,
    process_time DATETIME,
    accept_opinion VARCHAR(500),
    acceptor_id BIGINT,
    accept_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入初始数据
INSERT INTO sys_user (username, password, nickname, status) VALUES 
('admin', 'admin123', '系统管理员', 1),
('zhangsan', '123456', '张三', 1),
('lisi', '123456', '李四', 1);

INSERT INTO device_info (device_no, device_name, device_type, voltage_level, manufacturer, health_score) VALUES
('DEV001', '#1 主变', '变压器', '220kV', '特变电工', 95),
('DEV002', '#2 主变', '变压器', '220kV', '特变电工', 92),
('DEV003', '110kV 开关柜', '开关柜', '110kV', '平高电气', 88);
