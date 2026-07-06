# Zip-TV (OwnTV base)

IPTV-плеер для Android TV / приставок **x88/x96**, форк [OwnTV](https://github.com/ahXN00/OwnTV) (GPLv3).

## Отличия от OwnTV

- Брендинг **Zip-TV**, package `ru.zipdev.ziptv`
- Русский onboarding и навигация
- Автоплейлисты [iptv-org](https://github.com/iptv-org/iptv) при первом запуске (RU + кино + сериалы)
- Акцент `#9DFF4F`, обновления с [ZipZip111/zip-tv](https://github.com/ZipZip111/zip-tv)

Плеер, синхронизация и парсеры **не изменялись** — отдельный APK, OwnTV на устройстве можно оставить.

## Сборка

```bash
.\gradlew.bat assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

## Установка на приставку

1. Скопируйте APK на USB или скачайте с GitHub Releases.
2. Установите через файловый менеджер (неизвестные источники).
3. При первом запуске: **Начать** → профиль «Гость» → автозагрузка плейлистов.

---

<p class="footer-credit"><a href="https://zip-dev.ru" target="_blank" rel="noopener noreferrer">сайт разработан zip-dev.ru</a></p>
