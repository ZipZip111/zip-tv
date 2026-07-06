#!/usr/bin/env python3
"""
Fetch all playlists from https://m3u.su/, merge, deduplicate, and filter Russian channels.

Usage:
    python scripts/build_m3u_su_playlist.py
    python scripts/build_m3u_su_playlist.py --output playlists/ru-merged.m3u
"""

from __future__ import annotations

import argparse
import json
import logging
import re
import sys
import time
import unicodedata
from concurrent.futures import ThreadPoolExecutor, as_completed
from dataclasses import dataclass, field
from datetime import datetime, timezone
from pathlib import Path
from typing import Iterable
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen

BASE_URL = "https://m3u.su"
USER_AGENT = (
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
)
DEFAULT_OUTPUT = Path(__file__).resolve().parent.parent / "playlists" / "ru-merged.m3u"
DEFAULT_STATS = Path(__file__).resolve().parent.parent / "playlists" / "ru-merged.stats.json"

PLAYLIST_CODE_RE = re.compile(r"copyPlaylistUrl\('([a-z0-9]+)'\)")
PAGE_LINK_RE = re.compile(r'href="/page/(\d+)"')
EXTINF_ATTR_RE = re.compile(r'([a-zA-Z0-9_-]+)="([^"]*)"')
CYRILLIC_RE = re.compile(r"[\u0400-\u04FF]")
RESOLUTION_HINTS = (
    (100, re.compile(r"\b(8K|4320P)\b", re.I)),
    (90, re.compile(r"\b(4K|2160P|UHD)\b", re.I)),
    (80, re.compile(r"\b(FHD|1080P|FULL\s*HD)\b", re.I)),
    (70, re.compile(r"\b(HD|720P)\b", re.I)),
    (50, re.compile(r"\b(SD|576P|480P)\b", re.I)),
)

RUSSIAN_GROUP_KEYWORDS = (
    "россия", "россий", "русск", "общеросс", "федеральн",
    "москва", "петербург", "санкт", "регион", "област", "край", "сибир",
    "урал", "волг", "дальн", "север", "новост", "спорт", "детск", "мульт",
    "кино", "радио", "первый", "нтв", "матч", "звезда", "культура",
    "карусель", "отр", "рентv", "тнт", "стс", "пятниц", "домашн",
    "смотрим", "zabava", "забava", "забава", "снг", "совет", "ссср",
    "веб-камер", "харьков", "белгород", "воронеж", "краснодар", "новосиб",
    "екатерин", "нижний", "самara", "ростов", "перм", "омск", "тюмен",
    "калинин", "твер", "ярослав", "владимир", "курск", "бryansk", "липецк",
    "тамбov", "пенza", "саратов", "ульяnov", "ивanovo", "кostroma",
    "smolnp", "telekarta", "iptv-org] росс", "iptv-org] russian",
    "wink", "триколор", "беларус", "беларуск", "[by]", "🇷🇺",
)

FOREIGN_GROUP_KEYWORDS = (
    "uk ", " uk", "united kingdom", "britain", "england", "usa", " u.s.",
    "america", "german", "deutsch", " france", "france ", "français",
    "italy", "italia", "spain", "espa", "turkey", "türk", "turk", "poland",
    "polska", "ukraine", "украин", "ukr ", "china", "chinese", "japan",
    "korea", "india", "arab", "israel", "iran", "mexico", "brazil", "canada",
    "australia", "австрал", "австрия", "netherlands", "belgium", "бельги",
    "болгар", "бразил", "великобрит", "англи", "герман", "испан", "итал",
    "китай", "коре", "индия", "израил", "иран", "мексик", "канада",
    "нидерланд", "голланд", "швеци", "норвег", "финлянд", "дания", "швейцар",
    "чехия", "венгри", "румын", "серб", "хорват", "словак", "словен",
    "латви", "литв", "эстон", "грузия", "армен", "молдав", "узбек",
    "кыргыз", "таджик", "туркмен", "азербайдж", "казахстан",
    "🇦🇺", "🇦🇹", "🇧🇪", "🇧🇬", "🇧🇷", "🇨🇦", "🇨🇳", "🇩🇪", "🇪🇸", "🇫🇷",
    "🇬🇷", "🇪🇬", "🇮🇪", "🇮🇸", "🇵🇹", "🇨🇭", "🇨🇿", "🇭🇺", "🇷🇴",
    "world music", "sweden", "norway", "finland",
    "denmark", "austria", "switzerland", "czech", "hungary", "romania",
    "bulgaria", "serbia", "croatia", "slovak", "sloven", "latvia",
    "lithuania", "estonia", "georgia", "armenia", "moldova", "uzbek",
    "кыргыз", "таджик", "туркмен", "zarub", "зарубеж", "foreign",
    "international", "world", "global", "europe", "euro", "latino", "africa",
    "asia", "middle east", "весь мир", "мир (", "world cam",
    "webcam world", "камеры мир",
)

RUSSIAN_BRAND_KEYWORDS = (
    "первый", "россия", "нтв", "матч", "звезда", "культура", "карусель",
    "отр", "рен ", "рентv", "тнт", "стс", "пятниц", "домашн", "спас",
    "союз", "мир ", "cgtn рус", "rt рус", "rt russian", "смотрим",
    "viju", "more tv", "кинопрем", "кинохит", "киносем", "киносер",
    "киносвид", "киномик", "кинопок", "кино tv", "tv3", "tv 3", "пятница",
    "мужской", "женский", "che!", "che ", "суббота", "воскресенье",
    "europa plus", "europa+", "радио ", "vesti", "вести", "россия 1",
    "россия 24", "360", "дождь", "доккino", "mult", "мульти",
)

FOREIGN_COUNTRY_CODES = frozenset({
    "US", "GB", "UK", "DE", "FR", "IT", "ES", "TR", "PL", "UA", "CN", "JP",
    "KR", "IN", "IL", "IR", "MX", "BR", "CA", "AU", "NL", "BE", "SE", "NO",
    "FI", "DK", "AT", "CH", "CZ", "HU", "RO", "BG", "RS", "HR", "SK", "SI",
    "LV", "LT", "EE", "GE", "AM", "MD", "UZ", "KG", "TJ", "TM", "AZ", "KZ",
})

RUSSIAN_LANG_CODES = frozenset({"ru", "rus", "russian", "ru-ru", "ru_ru"})


@dataclass
class Channel:
    name: str
    url: str
    group: str = ""
    tvg_id: str = ""
    tvg_logo: str = ""
    tvg_country: str = ""
    tvg_language: str = ""
    extinf_line: str = ""
    source: str = ""
    extra_lines: list[str] = field(default_factory=list)

    @property
    def normalized_name(self) -> str:
        return normalize_name(self.name)

    @property
    def normalized_url(self) -> str:
        return self.url.strip().rstrip("/")

    def quality_score(self) -> int:
        score = 0
        if self.url.lower().startswith("https://"):
            score += 20
        elif self.url.lower().startswith("http://"):
            score += 5
        for points, pattern in RESOLUTION_HINTS:
            if pattern.search(self.name) or pattern.search(self.extinf_line):
                score += points
                break
        if self.tvg_logo:
            score += 3
        if self.tvg_id:
            score += 2
        if self.group:
            score += 1
        return score


def normalize_name(name: str) -> str:
    text = unicodedata.normalize("NFKC", name)
    text = re.sub(r"\s+", " ", text).strip().lower()
    text = re.sub(r"\s*(hd|fhd|4k|uhd|sd)\s*$", "", text, flags=re.I)
    return text


def fetch_url(url: str, timeout: int = 30, retries: int = 3) -> str:
    last_error: Exception | None = None
    for attempt in range(retries):
        try:
            req = Request(url, headers={"User-Agent": USER_AGENT})
            with urlopen(req, timeout=timeout) as resp:
                raw = resp.read()
            for encoding in ("utf-8", "cp1251", "latin-1"):
                try:
                    return raw.decode(encoding)
                except UnicodeDecodeError:
                    continue
            return raw.decode("utf-8", errors="replace")
        except (HTTPError, URLError, TimeoutError, OSError) as exc:
            last_error = exc
            if isinstance(exc, HTTPError) and exc.code == 429:
                time.sleep(5 * (attempt + 1))
            elif attempt + 1 < retries:
                time.sleep(2 * (attempt + 1))
    raise RuntimeError(f"Failed to fetch {url}: {last_error}") from last_error


def discover_playlist_codes() -> list[str]:
    """Scrape index pages and return unique playlist short codes."""
    codes: list[str] = []
    seen: set[str] = set()

    first_html = fetch_url(f"{BASE_URL}/")
    page_numbers = {int(n) for n in PAGE_LINK_RE.findall(first_html)}
    page_numbers.add(1)
    max_page = max(page_numbers) if page_numbers else 1

    for page in range(1, max_page + 1):
        url = f"{BASE_URL}/" if page == 1 else f"{BASE_URL}/page/{page}"
        html = first_html if page == 1 else fetch_url(url)
        for code in PLAYLIST_CODE_RE.findall(html):
            if code not in seen:
                seen.add(code)
                codes.append(code)
        logging.info("Page %d: found %d playlists so far", page, len(codes))

    return codes


def parse_extinf(line: str) -> tuple[dict[str, str], str]:
    attrs: dict[str, str] = {}
    for key, value in EXTINF_ATTR_RE.findall(line):
        attrs[key.lower()] = value

    comma_idx = line.rfind(",")
    name = line[comma_idx + 1 :].strip() if comma_idx >= 0 else ""
    return attrs, name


def parse_m3u(content: str, source: str) -> list[Channel]:
    channels: list[Channel] = []
    lines = content.splitlines()
    i = 0
    current_group = ""

    while i < len(lines):
        line = lines[i].strip()
        i += 1

        if not line:
            continue

        if line.startswith("#EXTINF"):
            attrs, name = parse_extinf(line)
            group = attrs.get("group-title", current_group)
            extra: list[str] = []

            while i < len(lines):
                nxt = lines[i].strip()
                if not nxt:
                    i += 1
                    continue
                if nxt.startswith("#"):
                    if nxt.startswith("#EXTINF"):
                        break
                    extra.append(nxt)
                    i += 1
                    continue
                url = nxt
                i += 1
                if url.startswith("http://") or url.startswith("https://"):
                    channels.append(
                        Channel(
                            name=name,
                            url=url,
                            group=group,
                            tvg_id=attrs.get("tvg-id", ""),
                            tvg_logo=attrs.get("tvg-logo", ""),
                            tvg_country=attrs.get("tvg-country", ""),
                            tvg_language=attrs.get("tvg-language", ""),
                            extinf_line=line,
                            source=source,
                            extra_lines=extra,
                        )
                    )
                break
            continue

        if line.startswith("#"):
            if line.startswith("#EXTGRP:"):
                current_group = line.split(":", 1)[1].strip()
            continue
    return channels


def contains_keyword(text: str, keywords: Iterable[str]) -> bool:
    lowered = text.lower()
    return any(kw in lowered for kw in keywords)


FOREIGN_NAME_KEYWORDS = (
    "world music - country", "world music - france", "world music - italia",
    "world music - japan", "world music - reggae", "world music - ukraine",
    "world music - germany", "world music - german", "bbc ", "cnn ", "fox news",
    "sky news", "euronews", "disney", "hbo ", "netflix", "espn", "nfl ", "nba ",
)

def is_russian_channel(ch: Channel) -> bool:
    name = ch.name
    group = ch.group
    combined = f"{name} {group} {ch.tvg_id} {ch.extinf_line}".lower()

    country = ch.tvg_country.strip().upper()
    if country in FOREIGN_COUNTRY_CODES and country not in {"RU", "BY"}:
        return False
    if country in {"RU", "RUS"}:
        return True

    lang = ch.tvg_language.strip().lower()
    if lang in RUSSIAN_LANG_CODES:
        return True
    if lang:
        foreign_langs = {"en", "eng", "english", "de", "ger", "fr", "fre", "uk", "ua", "tr", "ar", "it", "es", "ja", "ko"}
        if lang in foreign_langs:
            return False

    if contains_keyword(group, FOREIGN_GROUP_KEYWORDS):
        return False

    name_lower = name.lower()
    if contains_keyword(name_lower, FOREIGN_NAME_KEYWORDS):
        return False

    has_cyrillic = bool(CYRILLIC_RE.search(name))
    has_russian_group = contains_keyword(group, RUSSIAN_GROUP_KEYWORDS)
    has_russian_brand = contains_keyword(name.lower(), RUSSIAN_BRAND_KEYWORDS)

    if has_cyrillic:
        # Reject Ukrainian-only channels when clearly marked
        if contains_keyword(combined, ("украин", "ukraine", "🇺🇦")) and not contains_keyword(
            combined, ("рус", "росс", " cgtn", "rt рус")
        ):
            return False
        return True

    if has_russian_group or has_russian_brand:
        return True

    if re.search(r"\(RU\)|\[RU\]|🇷🇺", group, re.I):
        return True

    return False


def deduplicate_channels(channels: list[Channel]) -> list[Channel]:
    by_url: dict[str, Channel] = {}
    by_name: dict[str, Channel] = {}

    for ch in channels:
        url_key = ch.normalized_url.lower()
        name_key = ch.normalized_name

        existing = by_url.get(url_key)
        if existing is None or ch.quality_score() > existing.quality_score():
            by_url[url_key] = ch

        existing_name = by_name.get(name_key)
        if existing_name is None or ch.quality_score() > existing_name.quality_score():
            by_name[name_key] = ch

    chosen: dict[int, Channel] = {}
    for ch in by_url.values():
        chosen[id(ch)] = ch

    for ch in by_name.values():
        url_match = by_url.get(ch.normalized_url.lower())
        if url_match is not None:
            if ch.quality_score() > url_match.quality_score():
                by_url[ch.normalized_url.lower()] = ch
                chosen[id(ch)] = ch
            continue
        if id(ch) not in chosen:
            chosen[id(ch)] = ch

    result = list(by_url.values())
    seen_names: set[str] = set()
    final: list[Channel] = []
    for ch in sorted(result, key=lambda c: (c.group.lower(), c.normalized_name)):
        if ch.normalized_name in seen_names:
            continue
        seen_names.add(ch.normalized_name)
        final.append(ch)
    return final


def download_playlist(code: str) -> tuple[str, list[Channel] | None, str | None]:
    url = f"{BASE_URL}/{code}"
    time.sleep(0.35)  # gentle rate limit for m3u.su redirects
    try:
        content = fetch_url(url, timeout=45, retries=2)
        if "#EXTM3U" not in content and "#EXTINF" not in content:
            return code, None, "not a valid M3U"
        channels = parse_m3u(content, source=url)
        return code, channels, None
    except Exception as exc:
        return code, None, str(exc)


def build_extinf(ch: Channel) -> str:
    if ch.extinf_line:
        comma = ch.extinf_line.rfind(",")
        prefix = ch.extinf_line[:comma] if comma >= 0 else ch.extinf_line
        return f"{prefix},{ch.name}"
    parts = ['#EXTINF:-1']
    if ch.tvg_id:
        parts.append(f'tvg-id="{ch.tvg_id}"')
    if ch.tvg_logo:
        parts.append(f'tvg-logo="{ch.tvg_logo}"')
    if ch.group:
        parts.append(f'group-title="{ch.group}"')
    if ch.tvg_country:
        parts.append(f'tvg-country="{ch.tvg_country}"')
    if ch.tvg_language:
        parts.append(f'tvg-language="{ch.tvg_language}"')
    return " ".join(parts) + f",{ch.name}"


def write_m3u(channels: list[Channel], output_path: Path) -> None:
    output_path.parent.mkdir(parents=True, exist_ok=True)
    grouped: dict[str, list[Channel]] = {}
    for ch in channels:
        group = ch.group.strip() or "Без категории"
        grouped.setdefault(group, []).append(ch)

    lines = [
        "#EXTM3U",
        f"#PLAYLIST:Zip-TV RU merged from m3u.su ({datetime.now(timezone.utc).strftime('%Y-%m-%d %H:%M UTC')})",
        f"#TOTAL-CHANNELS:{len(channels)}",
        f"#TOTAL-GROUPS:{len(grouped)}",
    ]

    for group in sorted(grouped.keys(), key=str.lower):
        lines.append("")
        lines.append(f"#EXTGRP:{group}")
        for ch in sorted(grouped[group], key=lambda c: c.normalized_name):
            lines.append(build_extinf(ch))
            for extra in ch.extra_lines:
                lines.append(extra)
            lines.append(ch.url)

    output_path.write_text("\n".join(lines) + "\n", encoding="utf-8")


def main() -> int:
    parser = argparse.ArgumentParser(description="Build merged Russian M3U from m3u.su")
    parser.add_argument("--output", type=Path, default=DEFAULT_OUTPUT)
    parser.add_argument("--stats", type=Path, default=DEFAULT_STATS)
    parser.add_argument("--workers", type=int, default=4)
    parser.add_argument("--verbose", "-v", action="store_true")
    args = parser.parse_args()

    logging.basicConfig(
        level=logging.DEBUG if args.verbose else logging.INFO,
        format="%(asctime)s %(levelname)s %(message)s",
    )

    logging.info("Discovering playlists on %s ...", BASE_URL)
    codes = discover_playlist_codes()
    logging.info("Found %d playlist sources", len(codes))

    all_channels: list[Channel] = []
    failed_sources: dict[str, str] = {}
    source_stats: dict[str, int] = {}

    with ThreadPoolExecutor(max_workers=args.workers) as pool:
        futures = {pool.submit(download_playlist, code): code for code in codes}
        for future in as_completed(futures):
            code, channels, error = future.result()
            if error or channels is None:
                failed_sources[code] = error or "unknown error"
                logging.warning("Skip %s: %s", code, failed_sources[code])
                continue
            source_stats[code] = len(channels)
            all_channels.extend(channels)
            logging.info("Loaded %s: %d channels", code, len(channels))

    total_raw = len(all_channels)
    deduped = deduplicate_channels(all_channels)
    russian = [ch for ch in deduped if is_russian_channel(ch)]

    write_m3u(russian, args.output)

    categories = sorted({ch.group.strip() or "Без категории" for ch in russian}, key=str.lower)
    stats = {
        "generated_at": datetime.now(timezone.utc).isoformat(),
        "source_site": BASE_URL,
        "sources_total": len(codes),
        "sources_ok": len(source_stats),
        "sources_failed": len(failed_sources),
        "failed_sources": failed_sources,
        "channels_raw": total_raw,
        "channels_after_dedup": len(deduped),
        "channels_russian": len(russian),
        "categories_count": len(categories),
        "categories_sample": categories[:40],
        "categories_all": categories,
        "source_channel_counts": dict(sorted(source_stats.items(), key=lambda x: -x[1])[:30]),
        "output_file": str(args.output.resolve()),
    }
    args.stats.write_text(json.dumps(stats, ensure_ascii=False, indent=2), encoding="utf-8")

    print("\n=== Zip-TV m3u.su merge complete ===")
    print(f"Sources: {stats['sources_ok']}/{stats['sources_total']} OK, {stats['sources_failed']} failed")
    print(f"Channels: {total_raw} raw -> {len(deduped)} deduped -> {len(russian)} Russian")
    print(f"Categories: {len(categories)}")
    print(f"Output: {args.output.resolve()}")
    print(f"Stats:  {args.stats.resolve()}")
    print("\nSample categories:")
    for cat in categories[:20]:
        count = sum(1 for ch in russian if (ch.group.strip() or "Без категории") == cat)
        safe_cat = cat.encode("utf-8", errors="replace").decode("utf-8")
        print(f"  - {safe_cat} ({count})")

    return 0


if __name__ == "__main__":
    sys.exit(main())
