import pandas as pd #csv dosyasını okumak ve veri işlemek için
import numpy as np #dizi işlemleri için
from sklearn.ensemble import IsolationForest #anomali tespiiti yapar, uç noktaları bulur
from sklearn.preprocessing import MinMaxScaler #verileri 0 ile 1 arasına ölçekler, LSTM için önemlidir
import matplotlib.pyplot as plt 
#LSTM tabanlı zaman serisi tahmini yapmak için
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dense
import json

df = pd.read_csv(r"C:\Users\dilar\Desktop\vibration_data_sample.csv")
print(df.head()) #ilk 5 satır

#normalizasyon, veriyi ölçekleme
#LSTM gibi sinir ağları verilerde büyük değer farklarını sevmez
#bu yüzden tüm değeleri 0-1 aralığına indirmek gerekiyor
scaler = MinMaxScaler()
data = df[['vibration_x', 'vibration_y', 'vibration_z']].values
data_scaled = scaler.fit_transform(data)

#isolation forest modeli
#100 decision tree kullanarak model eğitilir
model = IsolationForest(n_estimators=100, contamination=0.05, random_state=42)
model.fit(data_scaled)
#contamination = 0.05 -> veri %5 anormal olabilir.

df['anomaly'] = model.predict(data_scaled)
# predict sonucu, -1 = anomali , 1 = normal
df['score'] = model.decision_function(data_scaled)

plt.figure(figsize=(12,6))
plt.plot(df['score'], label = "Anomali Skoru")
plt.axhline(y=np.percentile(df['score'], 5), color = 'r', linestyle = '--', label ="Eşik Değeri")
plt.title("Anomali Tespiti - Isolation Forest")
plt.xlabel("Zaman Adımı")
plt.ylabel("Skor")
plt.legend()
plt.show()

anomalies = df[df['anomaly'] == -1]
print(anomalies[['timestamp', 'vibration_x', 'vibration_y', 'vibration_z']])
#90.snden sonraki anomaliler

#Time Series Forecasting
#LSTM (RNN)	 Long Short Term Memory
def create_sequences(data, input_len, output_len):
    X, y = [], []
    for i in range(len(data) - input_len - output_len):
        X.append(data[i:i+input_len])
        y.append(data[i+input_len:i+input_len+output_len])
    return np.array(X), np.array(y)

X,y = create_sequences(data_scaled, input_len= 60, output_len=30)

X = X.reshape((X.shape[0], X.shape[1], X.shape[2])) #örnek, zaman, özellik

model_ltsm = Sequential([
    LSTM(64, activation='relu', input_shape=(X.shape[1], X.shape[2])),
    Dense(y.shape[1] * y.shape[2] if len(y.shape)== 3 else y.shape[1])    
])

model_ltsm.compile(optimizer='adam', loss='mse')
model_ltsm.fit(X, y.reshape(y.shape[0], -1), epochs=20, batch_size=16, validation_split=0.1)

#son 60 veriyle tahmin
last_input = data_scaled[-60:]
last_input = last_input.reshape((1, 60, data_scaled.shape[1]))
prediction = model_ltsm.predict(last_input)

future_pred = scaler.inverse_transform(prediction.reshape(-1, data_scaled.shape[1]))

plt.figure(figsize=(12,6))
past_data = scaler.inverse_transform(data_scaled[-60:])
for i, label in enumerate(['vibration_x', 'vibration_y', 'vibration_z']):
    plt.plot(range(60), past_data[:, i], label=f"{label} geçmiş")
    plt.plot(range(60,90), future_pred[:, i], label=f"{label} tahmin")
plt.title("LTSM ile Titreşim Tahmini")
plt.xlabel("Zaman(saniye)")
plt.ylabel("Titreşim")
plt.legend()
plt.grid()
plt.show()

print(json.dumps({
    "timestamp": "2025-05-01T12:00:00",
    "vibration_x": 0.003,
    "vibration_y": 0.002,
    "vibration_z": 0.005,
    "score": 0.0847,
    "anomaly": True
}))

# Anomalileri JSON olarak yazdır
for _, row in anomalies.iterrows():
    output = {
        "timestamp": row["timestamp"],
        "vibration_x": row["vibration_x"],
        "vibration_y": row["vibration_y"],
        "vibration_z": row["vibration_z"],
        "score": row["score"],
        "anomaly": True
    }
    print(json.dumps(output))

