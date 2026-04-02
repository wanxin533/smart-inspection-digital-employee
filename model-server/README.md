# 模型服务 - FastAPI

提供 AI 模型推理 API 服务

## 快速启动

```bash
cd model-server
pip install -r requirements.txt
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

## API 接口

- POST /api/v1/detect - 缺陷检测
- POST /api/v1/anomaly - 异常检测
- POST /api/v1/vqa - 视觉问答
- GET /health - 健康检查
