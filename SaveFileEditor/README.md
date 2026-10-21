# Save File Editor — Android Studio starter

## Included
- Native Kotlin app; manual file selection through Android Storage Access Framework.
- Detects ZIP archives, JSON, readable UTF-8 text, and unknown binary data.
- Lists ZIP entries; recognizes a likely Ren'Py archive by common entry names.
- Edits JSON/text and validates JSON before export.
- Exports to a new file; does not overwrite the selected original.
- No broad storage permissions, no pickle execution, and no writes to unknown binary formats.

## Important limitations
This is a buildable starter, not yet a complete House of Shinobi editor. It does not interpret or rewrite Ren'Py's serialized `log` state, infer variable semantics, regenerate signatures/checksums, or edit arbitrary binary formats. Ren'Py saves contain serialized Python/game state; treat pickle content as untrusted. See https://www.renpy.org/doc/html/save_load_rollback.html and https://www.renpy.org/doc/html/security.html.

A format-specific adapter should be added only after the target game's save format/version is verified. Do not label a field as currency, relationship, inventory, or progress solely from its variable name.

## Build on Windows
1. Install Android Studio and Android SDK Platform 35.
2. Extract the ZIP and open the `SaveFileEditor` folder in Android Studio.
3. Let Gradle sync; use Android Studio's embedded JDK if prompted.
4. Choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. APK output: `app/build/outputs/apk/debug/app-debug.apk`.

The project uses Android Gradle Plugin 8.7.3 and Kotlin 2.0.21. Android Studio may prompt you to install a compatible Gradle distribution/SDK.

## Planned extensions
1. Structured editors for XML, INI, and other known formats.
2. A validated, non-executing parser for supported serialized formats.
3. Game-specific field aliases only after meanings are verified.
4. Round-trip tests and automatic backup/restore.


## Optional: build APK in GitHub Actions
This project includes `.github/workflows/build-apk.yml`.
1. Upload the extracted project to a GitHub repository.
2. Open the repository's **Actions** tab and run **Build Android APK** (or push to main/master).
3. Open the completed workflow run and download the `SaveFileEditor-debug-apk` artifact.
4. Extract the artifact ZIP to get `app-debug.apk`.
