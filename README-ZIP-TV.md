# Zip-TV (OwnTV base)

IPTV-плеер для Android TV / приставок **x88/x96**, форк [OwnTV](https://github.com/ahXN00/OwnTV) (GPLv3).

## Отличия от OwnTV

- Брендинг **Zip-TV**, package `ru.zipdev.ziptv`
- Русский onboarding и навигация
- При первом запуске: **m3u.su** merged RU-плейлист (~6638 каналов, встроен в APK) + [iptv-org](https://github.com/iptv-org/iptv) кино и сериалы
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

### m3u.su merged playlist

Скрипт `scripts/build_m3u_su_playlist.py` собирает RU-каналы с [m3u.su](https://m3u.su) в `playlists/ru-merged.m3u`. Копия лежит в `app/src/main/assets/playlists/` и импортируется при bootstrap без сети; EPG — iptv-org `ru.xml`.

---

<p class="footer-credit"><a href="https://zip-dev.ru" target="_blank" rel="noopener noreferrer">сайт разработан zip-dev.ru</a></p>
