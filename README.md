# WeatherApp

An Android weather app showing current conditions, hourly forecasts, and 7-day forecasts for multiple cities. Users search for a city, add it to their list, and swipe between their cities with a horizontal pager.

Built with **Jetpack Compose** and a layered **MVVM** architecture (data / domain / ui).

## Features

- 🔍 City search with autocomplete (LocationIQ)
- 📍 Manage multiple cities, navigate with a horizontal pager
- 🌡️ Current weather: temperature, feels-like, wind (speed and direction), precipitation
- ⏱️ Hour-by-hour forecast
- 📅 7-day forecast (min/max, sunrise and sunset)
- 🎨 Dynamic day/night theme based on each city's conditions
- 💾 Persistence of the user's cities (DataStore Proto)
- ⚙️ Settings screen to delete saved cities

## Tech stack

| Area | Technologies |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, domain layer (repositories, use cases) |
| Dependency injection | Hilt |
| Networking | Retrofit, OkHttp, Gson |
| Async | Coroutines, Flow |
| Persistence | DataStore (Protobuf) |
| Images | Coil |
| Navigation | Navigation Compose |
| Dates | kotlinx-datetime |

## APIs used

- **[Open-Meteo](https://open-meteo.com/)** — weather data (current, hourly, daily). No key required.
- **[LocationIQ](https://locationiq.com/)** — city autocomplete and geocoding. Requires an access key.

## Requirements

- Android Studio (recent version, Ladybug or newer)
- JDK 17
- Minimum SDK: Android 8.0 (API 26) — Target SDK: API 34

## Installation

```bash
git clone <repository-url>
cd WeatherApp
```

Open the project in Android Studio, let Gradle sync, then run the app on an emulator or a device.

Or from the command line:

```bash
./gradlew installDebug
```

### API key configuration

City autocomplete uses LocationIQ, which requires an access key. Create a free account at [locationiq.com](https://locationiq.com/) to get yours, then set it in `LOCATION_IQ_ACCESS_TOKEN` (`app/src/main/java/com/example/weatherapp/di/AppModule.kt`).

> ⚠️ The key is currently hardcoded in the source. For a public repository or a production release, it's recommended to move it into `local.properties` / `BuildConfig` so it isn't exposed in version control.

## Project structure

```
app/src/main/java/com/example/weatherapp/
├── data/           # Retrofit APIs, DTOs, mappers, DataStore
├── domain/         # Repositories and business models
├── di/             # Hilt modules
├── navigation/     # Compose navigation graph
├── ui/
│   ├── composable/     # Reusable components (items, error/loading screens…)
│   ├── home/           # Root Activity and ViewModel
│   ├── pager_screen/   # Main screen with multi-city pager
│   ├── search_city/    # City search and add
│   ├── settings/       # Settings dialog
│   └── theme/          # Colors, typography, gradients
└── util/           # Date formatting, dispatchers, weather types
```

## Build

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

## License

Personal project — no license specified.
