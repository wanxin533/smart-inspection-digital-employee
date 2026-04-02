# YOLOv10 缺陷检测模型

## 模型文件

- `yolov10x.pt` - 预训练权重 (52MB)
- `config.yaml` - 训练配置
- `classes.txt` - 缺陷类别列表

## 缺陷类别

1. 绝缘子破损
2. 金具锈蚀
3. 导线断股
4. 避雷器损坏
5. 套管裂纹
6. 油位异常
7. 温度异常
8. 异物悬挂

## 推理示例

```python
from ultralytics import YOLO

model = YOLO("yolov10x.pt")
results = model("image.jpg")
for result in results:
    boxes = result.boxes
    for box in boxes:
        print(f"类别：{box.cls}, 置信度：{box.conf}, 位置：{box.xyxy}")
```
