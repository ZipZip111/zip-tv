package com.ultratv.tv.nativeapp.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.ultratv.tv.nativeapp.data.iptvorg.IptvOrgCatalog
import com.ultratv.tv.nativeapp.data.iptvorg.IptvOrgPreset
import com.ultratv.tv.nativeapp.i18n.LocalStrings
import com.ultratv.tv.nativeapp.ui.theme.UltraTokens

@OptIn(ExperimentalLayoutApi::class, androidx.tv.material3.ExperimentalTvMaterial3Api::class)
@Composable
fun IptvOrgCatalogSection(
    syncing: Boolean,
    onAddPreset: (IptvOrgPreset) -> Unit,
) {
    val S = LocalStrings.current
    val ru = S.navHome == "Главная"

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            if (ru) "📡 Каталог iptv-org" else "📡 iptv-org catalog",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            if (ru) {
                "Бесплатные публичные плейлисты с GitHub. Нажмите — плейлист добавится и синхронизируется автоматически (EPG — где доступен)."
            } else {
                "Free public playlists from GitHub. Tap to add and sync automatically (EPG when available)."
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IptvOrgCatalog.presets.forEach { preset ->
                Button(
                    onClick = { onAddPreset(preset) },
                    enabled = !syncing,
                ) {
                    Column {
                        Text(
                            "${preset.emoji} ${preset.displayName(ru)}",
                            fontSize = 14.sp,
                        )
                        val hint = preset.channelHint(ru)
                        if (hint.isNotBlank()) {
                            Text(hint, fontSize = 11.sp, color = UltraTokens.Fg3)
                        }
                    }
                }
            }
        }
        Text(
            if (ru) IptvOrgCatalog.LICENSE_NOTE_RU else IptvOrgCatalog.LICENSE_NOTE_EN,
            color = UltraTokens.Fg4,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
