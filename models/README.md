# AI 模型配置

## 模型列表

### 1. YOLOv10 - 缺陷检测
- **用途**: 电力设备缺陷检测（绝缘子破损、金具锈蚀等）
- **输入**: 可见光图像
- **输出**: 缺陷类型 + 位置框 + 置信度
- **精度**: mAP@0.5 > 92%

### 2. PatchCore - 异常检测
- **用途**: 无监督异常检测（未知缺陷类型）
- **输入**: 可见光/红外图像
- **输出**: 异常热力图 + 异常分数
- **精度**: AUC > 95%

### 3. LLaVA-1.6-7B - 视觉语言模型
- **用途**: 视觉问答、缺陷描述生成
- **输入**: 图像 + 文本问题
- **输出**: 自然语言回答
- **参数量**: 7B

## 模型下载

```bash
# YOLOv10
wget https://github.com/THU-MIG/yolov10/releases/download/v1.1/yolov10x.pt

# PatchCore
pip install pytorch-patchcore

# LLaVA
git clone https://github.com/haotian-liu/LLaVA.git
```
