# glAntiElytraTarget - Build

Requirements: JDK 21.

Build locally:
- Windows: `gradlew.bat clean build`
- Linux/macOS: `./gradlew clean build`

The plugin JAR is created in `build/libs/`.

GitHub Actions builds the project on every push and pull request. Download the JAR from the workflow run's Artifacts section.

Runtime dependency: PacketEvents must be installed on the Paper server. Other integrations are optional where declared in `plugin.yml`.

Compatibility note: this project is compiled against Paper API 1.21.4. A single JAR may work on later 1.21.x versions when the APIs it uses remain compatible, but no build configuration can guarantee compatibility with every current and future Paper release. Test each target server version.
