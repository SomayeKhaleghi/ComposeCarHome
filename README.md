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
  - *Page 0*: Minimal Clock with "Zen" animated background (floating leaves + water ripples).
  - *Page 1*: Full app drawer grid.
- **System Integration**: Signed with AOSP `platform` keys and pushed to `/system/priv-app/` to act as the default `HOME` activity.
- **Pure Compose Canvas**: The animated background uses zero external libraries (no Lottie, no videos)—just raw `drawOval` and `rotate` transforms.
- **Back Navigation**: `BackHandler` ensures users always return to the Home page before exiting.

## 🛠️ Tech Stack (The Modern Way)
- **Kotlin** 2.0.21 with the `kotlin-compose` compiler plugin (no more `kotlinCompilerExtensionVersion` hacks!).
- **Compose BOM** 2024.09.00 (`ui`, `material3`, `foundation`).
- **HorizontalPager** from `androidx.compose.foundation` for swipe gestures.
- **AOSP 14** build environment (API 34).

## 🧪 How to Run (The AOSP Integration)
*(i will bring the ADB/pushing commands later here )*
