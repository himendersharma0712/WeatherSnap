# WeatherSnap 🌦️📸

**WeatherSnap** is a high-fidelity Android application built with Jetpack Compose that allows users to capture live weather reports with integrated camera evidence and automated image optimization.

---

## 🔗 Download & Install
**[Latest Release: WeatherSnap v1.0](https://github.com/himendersharma0712/WeatherSnap/releases/tag/v1.0)**

*Download the latest APK to test the live weather telemetry and camera evidence features on your device.*

---

## 🚀 The Approach
This project was developed with a **Mobile-First, Performance-Heavy** mindset, focusing on three core pillars:
* **Thread Safety**: Offloading heavy database and image processing to background worker pools (`Dispatchers.IO`).
* **UI Fidelity**: Pixel-perfect implementation of the design spec, including custom camera HUDs and dynamic gradients.
* **Efficiency**: Native JPEG compression to reduce storage footprint without sacrificing evidence quality.

---

## 🛠️ Technical Stack & Architecture
The app follows the **MVVM (Model-View-ViewModel)** architectural pattern to ensure a clean separation of concerns and lifecycle-aware state management.

* **UI Layer**: 100% Jetpack Compose with custom navigation transitions (Horizontal Slides).
* **Networking**: Retrofit & OKHttp for fetching real-time weather telemetry from the Open-Meteo API.
* **Local Persistence**: Room Database for offline storage of weather snapshots and metadata.
* **Hardware Interop**: CameraX for a custom, integrated viewfinder experience.
* **Dependency Injection**: Hilt/Dagger for modular and testable code.

---

## 📸 Visual Showcase

### 1. Dashboard & Discovery
The home screen features an **IdleStateWidget** with custom gradients and a reactive search bar with "pill-row" city suggestions.

<img width="400" height="800" alt="Dashboard Idle State" src="https://github.com/user-attachments/assets/8c0efabc-90e7-41ca-9212-3c89c50d8d30" />

<img width="400" height="800" alt="City Search Suggestions" src="https://github.com/user-attachments/assets/8db643c2-b269-42f9-99e9-854f3154f712" />


### 2. Live Weather Telemetry
Once a city is selected, the app fetches real-time coordinates and current conditions (Humidity, Wind, Pressure) displayed with distinct, color-coded backgrounds for better data visualization.

<img width="400" height="800" alt="Weather Details Loading" src="https://github.com/user-attachments/assets/81049583-e931-47a4-86f0-9da3df7992bb" />


### 3. Evidence Capture & Compression
The custom camera interface allows for immediate "snap" actions. Post-capture, the app executes a **JPEG Discrete Cosine Transform compression algorithm** at a 70% quality factor, shrinking raw assets (e.g., 5612 KB -> 1588 KB) before saving to the Room DB.

<img width="400" height="800" alt="Camera Viewfinder HUD" src="https://github.com/user-attachments/assets/b4d2825c-f158-409d-8de7-d1ccfc897465" />

<img width="400" height="800" alt="Create Report Form" src="https://github.com/user-attachments/assets/1a1537b8-8dd5-4f24-a72a-ee801f3a3a31" />

---

## 🏗️ Key Engineering Wins
* **KSP/Room Signature Fix**: Overcame Javac/KSP compiler clashes by optimizing DAO return types for Kotlin 2.0 compatibility.
* **Single-Page UX**: Utilized `Modifier.weight()` and `imePadding` to ensure the entire report form—including the "Save" button—stays visible even when the keyboard is active.
* **Dynamic Weather Mapping**: Translated raw WMO Weather Codes into human-readable descriptions (e.g., "Clear Sky", "Thunderstorm").
* **Background Efficiency**: All File I/O and Bitmap operations are strictly constrained to `Dispatchers.IO` to prevent UI jank.
* **Clean Navigation**: Implemented `popBackStack` logic to ensure successful saves return the user to the dashboard rather than stale draft pages.

---

## 📖 How to Run
1. Clone the repository.
2. Open in **Android Studio Ladybug** (or newer).
3. Ensure you have an active internet connection for the Weather API.
4. Run on a physical device to test the CameraX integration.

---

**Developed with ❤️ by Himender Sharma**
