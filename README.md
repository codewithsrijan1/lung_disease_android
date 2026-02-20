# Lung Disease Classification – Mobile Deployment

Deep learning–based lung cancer subtype classification using transfer learning (Xception) with real-time deployment on Android (TensorFlow Lite) and iOS (CoreML).

---

## Overview

This project implements a CT scan image classification system that identifies four lung conditions:

- Normal  
- Adenocarcinoma  
- Large Cell Carcinoma  
- Squamous Cell Carcinoma  

Final Model Accuracy: **92.4%**

The trained model is converted to TensorFlow Lite (.tflite) and CoreML (.mlmodel) for on-device inference.

---

## Model Architecture

Backbone: Xception (ImageNet pretrained, frozen)  
Input Size: 350 × 350 × 3 (RGB)

Head:
- Global Average Pooling
- Dense (4 units, Softmax)

Architecture Flow:

Input Image  
→ Xception Feature Extractor  
→ Global Average Pooling  
→ Dense (Softmax – 4 Classes)  
→ Prediction  

Why Global Average Pooling?
- Reduces parameters  
- Minimizes overfitting  
- Improves generalization  
- Keeps mobile model lightweight  

---

## Training Configuration

- Optimizer: Adam  
- Loss: Categorical Crossentropy  
- Metrics: Accuracy  
- Epochs: 50  
- Batch Size: 8  
- Image Size: 350 × 350  
- Normalization: 1./255  
- Data Augmentation: Horizontal Flip  

Callbacks Used:
- EarlyStopping  
- ReduceLROnPlateau  
- ModelCheckpoint  

---

## Dataset

Structure:dataset/train
dataset/validation
dataset/test


Source:  
https://www.kaggle.com/datasets/mohamedhanyyy/chest-ctscan-images

---

## Model Export

Saved Model:trained_lung_cancer_model.h5


Converted to:
- TensorFlow Lite (.tflite)
- CoreML (.mlmodel)

---

## Android Deployment

Language: Kotlin  
Framework: TensorFlow Lite  

Pipeline:

Image Input  
→ Resize (350×350)  
→ Normalize  
→ ByteBuffer  
→ TFLite Interpreter  
→ Softmax Output  
→ Argmax  
→ Display Prediction + Confidence  

Android Repository:  
https://github.com/codewithsrijan1/lung_disease_android

---

## iOS Deployment

Language: Swift  
Framework: CoreML + Vision  

Pipeline:

UIImage  
→ VNCoreMLRequest  
→ Model Inference  
→ Probability Extraction  
→ Class Display  

Displays:
- Confidence bar  
- Class probability breakdown  
- Real-time preview  

---

## Results

- Overall Accuracy: 92.4%  
- Stable convergence  
- No significant overfitting  
- Efficient mobile inference  
- On-device processing for privacy  

---

## Key Features

- Transfer Learning (Xception)  
- Lightweight architecture (GAP)  
- Cross-platform mobile deployment  
- Real-time inference  
- Privacy-preserving on-device AI  

---

## Future Improvements

- Fine-tune backbone layers  
- Add confusion matrix & F1-score  
- INT8 Quantization  
- Cloud analytics integration  
- 3D CT volume classification  
- Additional pulmonary disease classes  

---

## References

- TensorFlow Transfer Learning Documentation  
- Xception Architecture Paper  
- Medical imaging CNN research literature  

Colab Training Notebook:  
https://colab.research.google.com/drive/1YHln5ZwxcmxioxECZbeHThfETBQX6Sxd?usp=sharing  

---
