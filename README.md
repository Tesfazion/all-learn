# All Learn

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android" alt="Android" />
  <img src="https://img.shields.io/badge/Kotlin-1.9%2B-7F52FF?style=for-the-badge&logo=kotlin" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Compose-UI-4285F4?style=for-the-badge" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/Status-Working%20Build-success?style=for-the-badge" alt="Status" />
</p>

All Learn is a mobile-first Android learning platform that helps users build personalized study plans, stay organized with weekly goals, and complete structured learning journeys through lessons, quizzes, AI support, and progress tracking.

## Why All Learn?

Learning often breaks down because plans are too generic, deadlines are vague, and progress is hard to sustain. All Learn solves this by combining:

- personalized course generation from user goals and available study hours
- realistic deadline-based learning plans
- structured weekly learning flows
- lesson-by-lesson guidance and content review
- AI helper support for context-aware learning questions
- weekly check-ins and plan adjustments

## Core Features

- Personalized onboarding flow for topic, skill level, deadline, and weekly study hours
- AI-generated curriculum built around the user’s available time and target date
- Course outline preview before starting a learning path
- Weekly lesson progression with milestone tracking
- Lesson detail screens with trusted source references
- Quiz-based knowledge checks
- AI assistant for contextual support during the learning process
- Weekly progress review and plan adjustment recommendations
- Firebase-powered authentication and app data flow

## Tech Stack

- Kotlin
- Jetpack Compose
- Navigation Compose
- Room Database
- DataStore Preferences
- WorkManager
- Firebase Authentication
- Firebase Firestore
- Firebase Analytics
- Firebase Crashlytics
- Gradle
- Android SDK

## Architecture

The project follows a practical feature-oriented Android structure:

- `app/src/main/java/com/alllearn/app/data` — app models, repositories, local persistence, and remote data access
- `app/src/main/java/com/alllearn/app/ui` — screens, navigation, view models, and Compose UI
- `app/src/main/java/com/alllearn/app/worker` — background tasks and reminders
- `app/src/main/res` — UI resources, strings, themes, and icons

## Project Structure

```text
All Learn/
├── app/
│   ├── src/main/java/com/alllearn/app/
│   ├── src/main/res/
│   ├── build.gradle.kts
│   └── google-services.json
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── gradlew
├── README.md
├── .gitignore
└── gradle/
```

## Prerequisites

Before running the app locally, install:

- Android Studio
- JDK 17
- Android SDK with platform tools
- Gradle 8.9 or compatible version
- An Android emulator or physical device

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/Tesfazion/all-learn.git
cd all-learn
```

### 2. Open in Android Studio

Open the project root in Android Studio and allow Gradle to sync.

### 3. Run the app

From Android Studio:

- choose an emulator or connected Android device
- click Run > app

From terminal:

```bash
./gradlew app:assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.alllearn.app/.MainActivity
```

### 4. Install on a physical device

1. Enable Developer Options and USB Debugging on your phone.
2. Connect the device to your PC.
3. Confirm detection with:

```bash
adb devices
```

4. Install and run the app:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.alllearn.app/.MainActivity
```

## Repository Metadata

Repository name:

```text
all-learn
```

Repository description:

```text
AI-powered Android learning app for personalized study plans, guided lessons, quizzes, and progress tracking.
```

## Release Tag

This project is currently tagged as:

```text
v1.0.0
```

## Current Status

The project is in a working Android app state and has been validated for debug builds in the current environment.

## Roadmap

Planned improvements include:

- broader testing coverage
- UI consistency improvements
- stronger runtime validation on real devices
- feature expansion and app stability refinements

## License

This project is currently under active development and can be adapted for personal or team use based on the repository owner’s preferences.

## Contributing

Contributions, bug reports, and feature requests are welcome. Please open an issue or submit a pull request with a clear summary of the change.
