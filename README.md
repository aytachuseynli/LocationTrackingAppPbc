# Location Tracker App

An Android application for tracking and syncing location data with Firebase. Built with Jetpack Compose, MVVM architecture, Hilt for dependency injection, and Room for local persistence.

## Features

- User authentication with Firebase Auth
- Real-time location tracking with foreground service
- Local storage with Room database
- Cloud sync with Firebase Realtime Database
- Background location tracking that persists across reboots
- Modern UI with Jetpack Compose

## 🖼 Screenshots

<div style="text-align: center;">
  <img src="https://github.com/user-attachments/assets/66df69d8-a01d-4a27-ba1d-749b918c3c6b" width="19%" alt="App Screenshot 1" />
  <img src="https://github.com/user-attachments/assets/f57633b7-21e5-49cb-8c1e-8705f7154980" width="19%" alt="App Screenshot 2" />
  <img src="https://github.com/user-attachments/assets/332dae4d-7142-404d-a898-92974a1829fe" width="19%" alt="App Screenshot 3" />
  <img src="https://github.com/user-attachments/assets/903374d0-b8f7-4e85-bb42-534abc12c4cf" width="19%" alt="App Screenshot 4" />
  <img src="https://github.com/user-attachments/assets/3edf4308-5ac3-4cbc-b169-22c26080e487" width="19%" alt="App Screenshot 5" />
</div>



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

### Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/LocationTrackerApp.git
cd LocationTrackerApp
```


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
