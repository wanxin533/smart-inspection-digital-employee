# LLaVA 视觉语言模型

## 用途

- 视觉问答（VQA）
- 缺陷描述生成
- 巡检报告自动生成

## 模型配置

- **Base Model**: LLaMA-2-7B
- **Vision Encoder**: CLIP ViT-L/14
- **Projector**: 2-layer MLP
- **输入分辨率**: 336x336

## 推理示例

```python
from llava import LLaVA

model = LLaVA("liuhaotian/llava-v1.6-7b")
image = load_image("defect.jpg")
question = "请描述图像中的设备缺陷"
answer = model.generate(image, question)
print(answer)
```

## 典型输出

"图像中显示的是一个绝缘子，其表面有明显的破损和裂纹，破损区域位于绝缘子中部偏左位置，裂纹呈放射状分布，建议立即更换。"
