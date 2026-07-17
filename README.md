# 🚗 ComposeCarHome

> **A custom launcher for Android Automotive OS 14. Built with Jetpack Compose and Kotlin 2.0. Replaces the default AOSP home screen.**

This project demonstrates how to replace the default AOSP launcher with a declarative, animated, and rotary-focused UI using the latest Compose Compiler Plugin.

![Android 14](https://img.shields.io/badge/Platform-AAOS_14-green)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple)
![Compose](https://img.shields.io/badge/Jetpack_Compose-2024.09-blue)
![Compose Compiler](https://img.shields.io/badge/Compose_Compiler-Kotlin_2.0-orange)

---

## 🎯 Why Compose for Automotive?
Traditional Automotive UIs relied on legacy XML and the CarUI library. This project proves that **Jetpack Compose** is the future for AAOS because:
- **Rotary Input Ready**: Uses `Modifier.focusable()` and `FocusRequester` to handle D-Pad/rotary controllers without bloated dependencies.
- **High Performance**: The Compose Compiler Plugin (Kotlin 2.0) enables direct skipping of redundant recompositions, keeping the Canvas animations (leaves/ripples) running at a smooth 60fps.
- **Declarative State**: Swapping between Home and App Drawer is a single `HorizontalPager` state change.

## ✨ Core Features
- **Dual-Page Layout**: 
  - *Page 0*: Minimal Clock with "Starry" animated background (floating leaves + water ripples).
  - *Page 1*: Full app drawer grid.
- **System Integration**: Signed with AOSP `platform` keys and pushed to `/system/priv-app/` to act as the default `HOME` activity.
- **Pure Compose Canvas**: The animated background uses zero external libraries (no Lottie, no videos)—just raw `drawOval` and `rotate` transforms.
- **Back Navigation**: `BackHandler` ensures users always return to the Home page before exiting.

## 🛠️ Tech Stack (The Modern Way)
- **Kotlin** 2.0.21 with the `kotlin-compose` compiler plugin (no more `kotlinCompilerExtensionVersion` hacks!).
- **Compose BOM** 2024.09.00 (`ui`, `material3`, `foundation`).
- **HorizontalPager** from `androidx.compose.foundation` for swipe gestures.
- **AOSP 14** build environment (API 34).

## 🧪 How to Build Each Project

### ComposeCarHome (Android Automotive Launcher)

A complete guide to building, deploying, and embedding the custom launcher into Android 14 Automotive.

---
## 🧪 How to Build Each Project

### ComposeCarHome (Android Automotive Launcher)

A complete guide to building, deploying, and embedding the custom launcher into Android 14 Automotive.

---
#### 📋 Prerequisites

Before you start, ensure you have:

- **AOSP 14 source tree** (Android 14) synced on your machine.
- **Build target set** (e.g., `lunch sdk_car_x86_64-ap2a-eng`).
- **Platform signing keys** (`platform.pk8`, `platform.x509.pem`) available in your build environment.
- **KVM acceleration** enabled on your Linux machine (for fast emulator performance).

---


#### ⚙️ Option 1: Copy to AOSP and Build (Fastest)

**Step 1: Copy the module to your AOSP source**

```bash
cp -r ComposeCarHome/ ~/aosp-ssd/device/aosp_lab
```
<img width="529" height="420" alt="ls_project pas" src="https://github.com/user-attachments/assets/daec5936-6b21-4e15-b5d4-3b1d5faf3822" />

<img width="1619" height="1032" alt="source_vscode" src="https://github.com/user-attachments/assets/5a829123-ccdc-48e4-87f0-246fcf33f177" />

** Step 2: Set up the build environment
```bash
cd ~/aosp-ssd
source build/envsetup.sh
lunch sdk_car_x86_64-ap2a-eng
```
<img width="534" height="394" alt="source_launch" src="https://github.com/user-attachments/assets/dd4c9e08-339f-4017-ab4a-f66d8cd52546" />


**Step 3: Build the APK
Navigate to the module directory and build with mm:
```bash
cd ~/aosp-ssd/device/aosp_lab/ComposeCarHome
mm -j4
```

** Step 4: Locate the output APK
The compiled APK will be placed at:
```bash
~/aosp-ssd/out/target/product/emulator_car64_x86_64/system/priv-app/ComposeCarHome/ComposeCarHome.apk
```



    
## 🏗️ Build a Permanent System Image (For ROM Integration)
This method bakes your launcher directly into system-qemu.img, making it the default even after -wipe-data.

### Step 1: Ensure your Android.bp includes the overrides and certificate: "platform"

(Your module should have these already; if not, add them to Android.bp).

### Step 2: Build the system image

From your AOSP root, run:
```bash
cd ~/aosp-ssd
source build/envsetup.sh
lunch sdk_car_x86_64-ap2a-eng
m systemimage -j4
```

<img width="615" height="449" alt="image" src="https://github.com/user-attachments/assets/5a0cc4a8-2960-49c6-9337-a4f955565412" />


###Step 3: Launch the emulator with the new image
```bash
emulator -memory 6144 -cores 4 -gpu host -accel on
```
| Flag | Purpose |
| :--- | :--- |
| `-memory 6144` | Allocates 6GB RAM to the emulator (smooth UI). |
| `-cores 4` | Uses 4 CPU cores. |
| `-gpu host` | Uses your dedicated GPU (hardware acceleration). |
| `-accel on` | Enables KVM virtualization (critical for speed). |

**Recommended command:**
```bash
emulator -memory 6144 -cores 4 -gpu host -accel on -no-snapshot

