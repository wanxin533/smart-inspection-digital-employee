# PatchCore 异常检测模型

## 用途

无监督异常检测，用于发现训练数据中未出现的新型缺陷。

## 原理

使用预训练 backbone 提取特征，通过记忆库匹配计算异常分数。

## 训练

```python
from patchcore import PatchCore

model = PatchCore(backbone="wide_resnet50_2")
model.fit(train_images)  # 仅需正常样本
```

## 推理

```python
anomaly_map, score = model.predict(test_image)
if score > 0.6:
    print("发现异常！")
```
