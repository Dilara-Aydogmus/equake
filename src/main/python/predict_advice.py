import os
import sys
import pandas as pd
import joblib
import json

if len(sys.argv) != 4:
    print(json.dumps({"error": "3 parametre gerekli: age, location, floor_type"}))
    sys.exit(1)

age = int(sys.argv[1])
location = sys.argv[2]
floor_type = sys.argv[3]

# 🔧 predict_advice.py ile aynı dizindeki .pkl dosyalarını dinamik bul
script_dir = os.path.dirname(os.path.abspath(__file__))
model_path = os.path.join(script_dir, "earthquake_advice_model.pkl")
features_path = os.path.join(script_dir, "model_features.pkl")

# ✅ Model dosyaları okunur
model = joblib.load(model_path)
feature_columns = joblib.load(features_path)

# Veri hazırlanır
input_data = pd.DataFrame([{
    "age": age,
    "location": location,
    "floor_type": floor_type
}])

input_encoded = pd.get_dummies(input_data)

# Eksik olan feature'lar sıfırla tamamlanır
for col in feature_columns:
    if col not in input_encoded.columns:
        input_encoded[col] = 0

input_encoded = input_encoded[feature_columns]

prediction = model.predict(input_encoded)[0]
advice = "Anomali riski yüksek" if prediction == 1 else "Riskli bir durum tespit edilmedi"

print(json.dumps({"prediction": int(prediction), "advice": advice}))
