Lung Cancer Detector (Android)

How to use:
1. Open this project in Android Studio.
2. Copy your trained model to:
   app/src/main/assets/lung_cancer_xception.tflite
3. Sync Gradle and Run.

Features:
- Clean Material UI
- Gallery input (camera stub included)
- Confidence scores
- Model info section

Notes:
- Input expected: 350x350 RGB normalized to [0,1]
- Output expected: 4-class softmax
