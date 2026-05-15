# Jan Aushadhi Finder

Jan Aushadhi Finder is an Android application designed to help users find affordable generic medicines and locate nearby Jan Aushadhi stores (PMBJK). The app provides a platform to search for medicines, compare prices, and navigate to the nearest pharmacy providing high-quality generic drugs at affordable prices.

## Features

- **Search Medicines:** Look up medicines by brand name or generic name, search 500+ Medicines.
- **Price Comparison:** Compare prices between branded medicines and their Jan Aushadhi generic counterparts.
- **Store Locator:** Find Jan Aushadhi stores on an interactive map or in a list view.
- **Store Details:** Get detailed information about stores including address, opening hours, and contact details.
- **Navigation:** Get directions to stores using Google Maps integration.
- **User Authentication:** Secure login and registration using Firebase Authentication.
- **Cloud Database:** Real-time data synchronization for medicines and stores using Firebase Firestore.

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Modern Android UI)
- **Design System:** Material 3
- **Architecture:** MVVM (recommended pattern)
- **Backend:** 
    - Firebase Authentication
    - Firebase Cloud Firestore
- **Maps:** Google Maps SDK for Android & Maps Compose
- **Other Libraries:**
    - Kotlin Coroutines & Flow
    - Firebase KTX

## Getting Started

### Prerequisites

- Android Studio Koala or newer.
- A Firebase project configured for Android.
- A Google Maps API Key.

### Setup

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/JanAushadhiFinder.git
    ```
2.  **Firebase Configuration:**
    - Create a project in the [Firebase Console](https://console.firebase.google.com/).
    - Add an Android app with the package name `com.example.janaushadhifinder`.
    - Download `google-services.json` and place it in the `app/` directory.
    - Enable **Email/Password** Authentication.
    - Enable **Cloud Firestore**.
3.  **Google Maps API Key:**
    - Obtain an API key from the [Google Cloud Console](https://console.cloud.google.com/).
    - Add the key to your `local.properties` file or `AndroidManifest.xml`:
      ```xml
      <meta-data
          android:name="com.google.android.geo.API_KEY"
          android:value="YOUR_API_KEY"/>
      ```
4.  **Build and Run:**
    - Open the project in Android Studio.
    - Sync Gradle.
    - Run the app on an emulator or a physical device.

## App Screens

- **Home Screen:** Quick search,Find stores,categories,and navigation to major features.
- **Login/Register:** User onboarding.
- **Search Results:** 500+ List of medicines with price information and Filtering of medicines based on categories and Price.
- **Store Locator:** Google Map and list views of PMBJK stores with filters for State and City.

## APK Download
Download the APK file from the repository to install and test the application.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
