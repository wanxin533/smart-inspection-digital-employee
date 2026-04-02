# 配置文件

## 环境配置

### 开发环境 (config/dev.env)
```
DATABASE_URL=mysql://localhost:3306/smart_inspection
REDIS_URL=redis://localhost:6379
MODEL_SERVER_URL=http://localhost:8000
```

### 生产环境 (config/prod.env)
```
DATABASE_URL=mysql://prod-db:3306/smart_inspection
REDIS_URL=redis://prod-redis:6379
MODEL_SERVER_URL=http://model-server:8000
```

## Nginx 配置

```nginx
server {
    listen 80;
    server_name smart-inspection.example.com;
    
    location / {
        proxy_pass http://frontend:80;
    }
    
    location /api {
        proxy_pass http://backend:8080;
    }
    
    location /model-api {
        proxy_pass http://model-server:8000;
    }
}
```

## SSL 证书配置

```bash
# 申请 Let's Encrypt 证书
certbot --nginx -d smart-inspection.example.com
```
