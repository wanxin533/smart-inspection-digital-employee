# 智能巡视数字员工系统 - 部署指南

## 环境要求

### 硬件要求
- **CPU**: 8 核以上
- **内存**: 16GB 以上
- **GPU**: NVIDIA GTX 1060 以上 (AI 推理)
- **硬盘**: 100GB 以上 SSD
- **网络**: 100Mbps 以上

### 软件要求
- **操作系统**: Ubuntu 20.04+ / CentOS 7+ / Windows Server 2019+
- **Docker**: 20.10+
- **Docker Compose**: 2.0+

## 快速部署

### 1. 克隆项目
```bash
git clone https://github.com/wanxin533/smart-inspection-digital-employee.git
cd smart-inspection-digital-employee
```

### 2. 启动所有服务
```bash
docker-compose -f docker-compose.full.yml up -d
```

### 3. 查看服务状态
```bash
docker-compose ps
```

### 4. 访问系统
- **前端**: http://localhost
- **后端 API**: http://localhost:8080/api
- **模型服务**: http://localhost:8000
- **Neo4j**: http://localhost:7474

### 5. 默认账号
- **用户名**: admin
- **密码**: admin123

## 生产环境部署

### 1. 修改配置
编辑 `config/prod.env` 文件，配置生产环境参数。

### 2. SSL 证书
```bash
# 安装 certbot
apt install certbot python3-certbot-nginx

# 申请证书
certbot --nginx -d your-domain.com
```

### 3. 配置 Nginx
使用 `config/nginx.conf` 配置文件。

### 4. 启动服务
```bash
docker-compose -f docker-compose.full.yml up -d
```

### 5. 配置防火墙
```bash
# 开放必要端口
ufw allow 80/tcp
ufw allow 443/tcp
ufw allow 8080/tcp
```

## 服务管理

### 启动服务
```bash
docker-compose up -d
```

### 停止服务
```bash
docker-compose down
```

### 重启服务
```bash
docker-compose restart
```

### 查看日志
```bash
docker-compose logs -f backend
```

### 数据备份
```bash
# 备份 MySQL 数据
docker exec si-mysql mysqldump -u root -padmin123 smart_inspection > backup.sql

# 备份 Neo4j 数据
docker exec si-neo4j neo4j-admin dump --database=neo4j --to-path=/backups
```

### 数据恢复
```bash
# 恢复 MySQL 数据
docker exec -i si-mysql mysql -u root -padmin123 smart_inspection < backup.sql
```

## 故障排查

### 服务无法启动
```bash
# 查看日志
docker-compose logs backend

# 检查端口占用
netstat -tlnp | grep 8080
```

### 数据库连接失败
```bash
# 检查 MySQL 状态
docker-compose ps mysql

# 测试连接
docker exec si-mysql mysql -u root -padmin123 -e "SHOW DATABASES;"
```

### GPU 无法使用
```bash
# 检查 NVIDIA 驱动
nvidia-smi

# 检查 Docker GPU 支持
docker run --gpus all nvidia/cuda:11.0-base nvidia-smi
```

## 性能优化

### 1. 调整 JVM 参数
编辑 `src/backend/Dockerfile`:
```dockerfile
ENV JAVA_OPTS="-Xms512m -Xmx2g"
```

### 2. 调整数据库连接池
编辑 `application.yml`:
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
```

### 3. 启用 Redis 缓存
```yaml
spring:
  cache:
    type: redis
```

---

**技术支持**: support@smart-inspection.com
