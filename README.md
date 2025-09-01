🧬 Skin Cancer Detector (Hackathon Project)

Repository: SkinCancerDetectorHackathon

Authors: Ranya Prasad, Smriti Kumar, Meghana Mandava
Affiliation: Stevenson High School, Science REACH Program


📌 Project Overview

Skin cancer is the most common cancer in the United States, affecting 1 in 5 people by age 70. Early detection improves survival rates dramatically, yet access to affordable, non-invasive screening remains limited.

This project builds an AI-powered image classification system to detect skin cancer from dermatoscopic images. Using Convolutional Neural Networks (CNNs) and Transfer Learning models (EfficientNet-Lite), we tested multiple architectures to identify the most accurate model for early detection.

Our best model achieved ~90% accuracy, while also addressing bias in skin tone datasets, ensuring fairness across diverse populations


🚀 Features

Multi-model comparison: CNN, FFNN, ANN, and Transfer Learning.

Best performance: Transfer Learning (EfficientNet-Lite3) with 90.32% test accuracy.

Bias mitigation: Incorporated balanced image datasets across different skin tones
.

App prototype: Designed a mobile app interface for real-world use — allowing users to upload a photo, receive predictions, and access prevention resources.


⚙️ Methodology

Data Collection

Public skin cancer image datasets.

Preprocessed to [244 × 244] resolution.


Model Training

Implemented in TensorFlow/Keras on Google Colab.

Compared four approaches: CNN, FFNN, ANN, Transfer Learning.

Training with up to 50 epochs (optimized to 32 for EfficientNet-Lite3).


Evaluation

Accuracy, validation loss, false positives vs. false negatives.

Transfer Learning outperformed all baselines (CNN = 73.58%, FFNN = 64.2%, ANN = 31.2%).

📊 Results
Model	Avg. Accuracy
ANN	31.19%
FFNN	64.20%
CNN	73.58%
Transfer Learning (EfficientNet-Lite3)	84.68% – 90.32%


📱 Future Work

Expand dataset with hospital-sourced images for higher accuracy.

Deploy as a mobile app for affordable, accessible diagnosis.

Integrate with healthcare providers for confirmatory testing.


🔧 Tech Stack

Languages: Python

Libraries: TensorFlow, Keras, NumPy, Pandas, Matplotlib

Platform: Google Colab

Models: CNN, ANN, FFNN, EfficientNet-Lite (Transfer Learning)
