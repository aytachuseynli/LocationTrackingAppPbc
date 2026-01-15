# Location Tracker App

An Android application for tracking and syncing location data with Firebase. Built with Jetpack Compose, MVVM architecture, Hilt for dependency injection, and Room for local persistence.

## Features

- User authentication with Firebase Auth
- Real-time location tracking with foreground service
- Local storage with Room database
- Cloud sync with Firebase Realtime Database
- Background location tracking that persists across reboots
- Modern UI with Jetpack Compose

## Tech Stack

- Kotlin
- Jetpack Compose
- MVVM Architecture
- Hilt (Dependency Injection)
- Room (Local Database)
- Firebase Auth & Realtime Database
- WorkManager (Background Sync)
- Foreground Service (Location Tracking)

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/LocationTrackerApp.git
cd LocationTrackerApp
```

### 2. Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or use an existing one
3. Add an Android app with package name: `com.aytachuseynli.locationtrackerapp`
4. Download the `google-services.json` file
5. Place it in the `app/` directory

#### Enable Firebase Services:
- **Authentication**: Enable Email/Password sign-in method
- **Realtime Database**: Create a database and set up rules

Example Realtime Database Rules:
```json
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    }
  }
}
```

### 3. Keystore Setup (For Release Builds)

1. Copy `keystore.properties.example` to `keystore.properties`
2. Fill in your keystore details:

```properties
storePassword=YOUR_STORE_PASSWORD
keyAlias=YOUR_KEY_ALIAS
keyPassword=YOUR_KEY_PASSWORD
storeFile=keystore/release-keystore.jks
```

3. Create your keystore file:
```bash
keytool -genkey -v -keystore app/keystore/release-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias YOUR_KEY_ALIAS
```

### 4. Build and Run

```bash
./gradlew assembleDebug
```

Or open the project in Android Studio and run it directly.

## Project Structure

```
app/src/main/java/com/aytachuseynli/locationtrackerapp/
├── data/
│   ├── local/          # Room database, DAOs, entities
│   └── remote/         # Firebase repositories
├── di/                 # Hilt dependency injection modules
├── domain/
│   ├── model/          # Domain models
│   └── repository/     # Repository interfaces
├── receiver/           # Broadcast receivers (Boot)
├── service/            # Foreground location service
├── ui/
│   ├── auth/           # Authentication screens
│   ├── dashboard/      # Main dashboard
│   ├── permission/     # Permission request flow
│   ├── components/     # Reusable UI components
│   ├── navigation/     # Navigation setup
│   └── theme/          # Material theme
├── util/               # Utility classes
└── worker/             # WorkManager workers
```

## Permissions Required

- `ACCESS_FINE_LOCATION` - Precise location access
- `ACCESS_COARSE_LOCATION` - Approximate location access
- `ACCESS_BACKGROUND_LOCATION` - Background location tracking
- `FOREGROUND_SERVICE` - Foreground service for tracking
- `RECEIVE_BOOT_COMPLETED` - Resume tracking after reboot
- `INTERNET` - Firebase sync
- `POST_NOTIFICATIONS` - Show tracking notifications

## License

This project is open source and available under the MIT License.
