# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Brixie is a modern Android application that integrates with the Rebrickable API to provide comprehensive LEGO set browsing and searching functionality. The app features a cool, elegant Material 3 design optimized for both phones and tablets, with German localization.

### Core Features
- **Categories List**: Browse all LEGO categories
- **Sets List**: View and filter LEGO sets
- **Search Mode**: Advanced search functionality for sets
- **Detailed Set View**: Complete set information with cached images
- **Image Caching**: Efficient download and caching of set images
- **Responsive Design**: Optimized for phones and tablets
- **German UI**: Full German localization

### Design Philosophy
- Material 3 design system with Material You theming
- Modern, cool, and elegant user interface
- Responsive layouts for various screen sizes
- Performance-optimized with efficient caching

## Key Architecture Components

### Package Structure
- `eu.mpwg.android.brixie` - Main app package with UI components (MainActivity, fragments)
- `eu.mpwg.android.rebrickable` - Rebrickable API integration package
  - `api/` - API client and service definitions
  - `cache/` - API response caching mechanisms
  - `config/` - API configuration management

### Navigation & UI
- Uses Bottom Navigation with three main destinations: Home, Dashboard, Notifications
- Implements MVVM pattern with ViewModels and LiveData
- Uses View Binding for type-safe view references
- Fragment-based navigation with Navigation Component

### Networking Stack
- **Retrofit 3.0.0** for REST API calls
- **OkHttp 5.1.0** for HTTP client with logging interceptor
- **Gson 2.13.1** for JSON serialization
- Custom API client (`RebrickableApiClient`) with authentication interceptor

## Development Commands

### Building
```bash
./gradlew build                    # Build the project
./gradlew app:assembleDebug       # Build debug APK
./gradlew app:assembleRelease     # Build release APK
```

### Testing
```bash
./gradlew test                    # Run unit tests
./gradlew app:testDebug          # Run debug unit tests
./gradlew connectedAndroidTest   # Run instrumented tests
```

### Installation & Running
```bash
./gradlew installDebug           # Install debug APK on connected device
./gradlew app:installDebug       # Install debug APK (specific module)
```

### Code Quality
```bash
./gradlew lint                   # Run lint checks
./gradlew app:lintDebug         # Run lint on debug variant
```

### Gradle Management
```bash
./gradlew clean                  # Clean build artifacts
./gradlew --refresh-dependencies # Refresh dependencies
```

## Key Configuration Files

- **gradle/libs.versions.toml** - Centralized dependency version management
- **app/build.gradle.kts** - Main app module configuration
- **build.gradle.kts** - Project-level build configuration
- Target SDK: 36, Min SDK: 31, Compile SDK: 36
- Java 17 compatibility

## API Integration Notes

The Rebrickable API client uses:
- API key authentication via header: `authorization: key {API_KEY}`
- Base URL configuration through `RebrickableApiConfiguration`
- HTTP logging for debugging (BasicLevel)
- Gson for JSON conversion

When working with API endpoints, ensure proper error handling and consider the existing caching mechanisms in the `cache/` package.
- It is an App that uses Rebrickable-swift to show and search for Lego Sets
- This App should offer a List of all Categories
- This App should offer a List of all Sets
- This App should offer a Search mode
- It should be possible to have an detailed view of a Set
- It should download and cache the Images of a Set and view it
- the website is brixie.net
- It has a modern, cool, easy to use and elegant user interface
- The design is based on Material 3 and Material for you
- Allways ensure that the project compiles
- It works on phones and tablets
- The language is German
- Always use the most modern version of tools and libraries