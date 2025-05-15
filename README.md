# ❤️ Blood App

Una aplicación Android desarrollada en Kotlin para medir la **frecuencia cardíaca (PPM)** y la **saturación de oxígeno (SpO₂)** utilizando la cámara del dispositivo. Los datos se almacenan en Firebase y se muestran gráficamente para el usuario en tiempo real.

---

## 📱 Características

- 📷 Medición de **frecuencia cardíaca** y **SpO₂** mediante la cámara trasera.
- 🔒 Autenticación de usuarios con **Firebase Authentication**.
- ☁️ Almacenamiento de datos en **Firebase Firestore** por usuario.
- 📊 Visualización en tiempo real de:
  - Última medición de SpO₂.
  - Gráfica en tiempo real de SpO₂ (LineChart).
  - Historial de PPM.
- 💾 Descarga de la gráfica del historial como imagen.
- 🎨 Interfaz moderna usando Jetpack Compose y XML combinados.

---

## 🛠️ Tecnologías

- **Kotlin**
- **Jetpack Compose** y **XML UI**
- **CameraX API**
- **Firebase Authentication**
- **Firebase Firestore**
- **MPAndroidChart** (para gráficas)
- **MediaStore** (para guardar imágenes)

---

## 📸 Cómo funciona la medición

1. El usuario otorga permiso a la cámara.
2. La aplicación activa la cámara trasera con `CameraX`.
3. Se calcula la frecuencia cardíaca y SpO₂ con base en variaciones de color en el dedo (método fotopletismografía).
4. Los valores se simulan actualmente con números aleatorios (entre 60-100 PPM y 95-100 SpO₂) en `CameraViewModel.kt` mientras se prueba la interfaz.

---

## 🔥 Estructura en Firebase

/ppmData/{UID}/registros/{auto_id} => { ppm: Int, timestamp: Long }
/spo2Data/{UID}/registros/{auto_id} => { spo2: Int, timestamp: Long }


- Cada usuario tiene su propio documento `UID`.
- Las mediciones se almacenan en subcolecciones `registros`.

---

## 🧪 Pantallas principales

- **CameraActivity.kt**  
  Mide los valores y los guarda en Firebase.

- **PrincipalPageActivity.kt**  
  Muestra el último valor de SpO₂ y una gráfica en tiempo real de las últimas 5 mediciones.

- **HistorialActivity.kt**  
  Muestra el historial completo de PPM y permite descargar la gráfica.

---

## 🚀 Cómo compilar

1. Clona el repositorio:
https://github.com/kingslayer2075/Blood_app

```bash

git clonehttps://github.com/kingslayer2075/Blood_app
