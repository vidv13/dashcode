# Android File Organization Standards

## Project Structure

```
dashcode/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── kotlin/com/vidv13/dashcode/
│       │   │   ├── DashCodeApplication.kt
│       │   │   ├── MainActivity.kt
│       │   │   ├── ui/
│       │   │   │   ├── app/          # Root composable
│       │   │   │   ├── theme/        # Material3 theme
│       │   │   │   └── <feature>/    # Feature screens
│       │   │   ├── data/
│       │   │   │   ├── local/        # Room DAOs, entities
│       │   │   │   └── repository/   # Repository implementations
│       │   │   ├── domain/
│       │   │   │   └── model/        # Domain models
│       │   │   └── di/              # Hilt modules
│       │   └── res/
│       │       ├── values/           # strings, colors, themes
│       │       └── mipmap-*/         # launcher icons
│       ├── test/                     # Unit tests (JUnit 5 + MockK)
│       └── androidTest/              # Instrumented tests (JUnit 4)
├── gradle/
│   ├── libs.versions.toml            # Version catalog
│   └── wrapper/
├── build.gradle.kts                  # Root build (plugins with apply false)
├── settings.gradle.kts               # Module includes, repositories
└── gradle.properties                 # Build configuration
```

## Conventions

### Package by Feature
Group related classes by feature rather than by layer. Each feature package
may contain its own screen composables, ViewModel, and repository.

### Source Sets
- `src/main/kotlin/` — production Kotlin code (no Java)
- `src/main/res/` — Android resources
- `src/test/` — local unit tests (JUnit 5, MockK, no Android framework)
- `src/androidTest/` — instrumented tests (JUnit 4, Compose test rules)

### Dependencies
All dependency versions are centralized in `gradle/libs.versions.toml`.
Use version catalog aliases in build files (`libs.some.dependency`).
