# MiniCryptoTracker

MiniCryptoTracker is an Android application designed to track cryptocurrency prices using the CoinGecko API. The project is built using Kotlin and Android Jetpack libraries, including Compose for UI development.

## Project Structure

The project is organized as follows:

- **app/src/main/java/dev/lucianosantos/minicryptotracker/**: Contains the main Kotlin source files for the application, organized into the following subfolders:
  - **data/**: Handles data-related operations, including repositories and data sources.
  - **model/**: Contains data models and entities used throughout the application.
  - **network/**: Manages API calls and network-related functionality, including Retrofit services.
  - **database/**: Contains Room database setup and related DAOs.
  - **ui/**: Includes UI components and screens built using Jetpack Compose.
  - **utils/**: Provides utility classes and helper functions.
- **app/src/main/res/**: Contains resources such as layouts, drawables, and values.
- **app/src/main/AndroidManifest.xml**: Application manifest file.
- **app/src/androidTest/**: Contains Android-specific test code.
- **app/src/test/**: Contains unit test code.
- **gradle/libs.versions.toml**: Defines library versions and dependencies.

## How to Build and Run

### Prerequisites

1. Install Android Studio.
2. Ensure you have the latest Android SDK and Kotlin plugin installed.
3. Set up a CoinGecko API token.

### Steps

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd MiniCryptoTracker
   ```

2. Open the project in Android Studio.
   - Wait for Gradle to sync and ensure all dependencies are downloaded before proceeding.

3. Set up a CoinGecko API token:
   - Create a `local.properties` file in the root of the project if it doesn't exist.
   - Add the following line to the file:
     ```
     COINGECKO_API_TOKEN=<your-api-token>
     ```
   - Follow [CoinGecko API documentation](https://www.coingecko.com/en/api/documentation) for instructions on obtaining an API token.

4. Build the project:
   - In Android Studio, click on "Build"

5. Run the application:
   - Connect an Android device or start an emulator.
   - Click on "Run"

## Dependencies

The project uses the following key dependencies:

- Android Jetpack libraries (Compose, Lifecycle, Room)
- Retrofit for network requests
- Coil for image loading
- Kotlin Coroutines for asynchronous programming

Refer to `gradle/libs.versions.toml` for the complete list of dependencies and their versions.
