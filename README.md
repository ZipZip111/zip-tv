# Zip-TV

<p align="center">
  <strong>IPTV-плеер для Android TV</strong><br/>
  Форк <a href="https://github.com/khalilbenaz/ultra-tv">Ultra TV</a> · Kotlin · Jetpack Compose · Media3
</p>

<p align="center">
  <a href="https://github.com/ZipZip111/zip-tv/releases/latest/download/ZipTV-debug.apk">
    <img src="https://img.shields.io/badge/Download-APK-9DFF4F?style=for-the-badge&labelColor=080808" alt="Download APK" />
  </a>
  <a href="https://zip-dev.ru"><img src="https://img.shields.io/badge/zip--dev.ru-080808?style=for-the-badge&labelColor=9DFF4F" alt="zip-dev.ru" /></a>
</p>

---

## Возможности

- **M3U / M3U8** — URL или локальный файл
- **Xtream Codes** и **Stalker Portal**
- **Live TV**, фильмы, сериалы, EPG
- **Избранное**, история, продолжить просмотр
- **Русский интерфейс** (+ EN, FR, ES, AR)
- Дизайн в стиле [zip-dev.ru](https://zip-dev.ru) — тёмный фон `#080808`, accent `#9DFF4F`

## Установка на приставку (x88 / x96)

1. Скачайте [ZipTV-debug.apk](https://github.com/ZipZip111/zip-tv/releases/latest/download/ZipTV-debug.apk)
2. Установите через USB, Downloader или `adb install ZipTV-debug.apk`
3. **Настройки → + M3U URL** — вставьте ссылку на плейлист
4. Язык: **Настройки → Язык → Русский** (или «System» для автоопределения)

## Сборка из исходников

```bash
cd android-native
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

Требуется **JDK 17** и Android SDK (Android Studio).

## Релизы

Релизы публикуются автоматически при push тега `v*`:

```bash
echo "1.0.1" > VERSION
git add VERSION && git commit -m "Bump version to 1.0.1"
git tag v1.0.1 && git push origin main --tags
```

GitHub Actions соберёт APK и опубликует его в [Releases](https://github.com/ZipZip111/zip-tv/releases).

## Package ID

`ru.zipdev.ziptv` — можно установить рядом с оригинальным Ultra TV.

## Credits

Основано на [Ultra TV](https://github.com/khalilbenaz/ultra-tv) by [khalilbenaz](https://github.com/khalilbenaz) (MIT).

Разработка и брендинг: [zip-dev.ru](https://zip-dev.ru)

## Disclaimer

Zip-TV — **плеер**, не источник контента. Используйте только плейлисты и потоки, к которым у вас есть законный доступ.
