package com.ultratv.tv.nativeapp.bootstrap

import com.ultratv.tv.nativeapp.ProductConfig
import com.ultratv.tv.nativeapp.data.iptvorg.IptvOrgCatalog
import com.ultratv.tv.nativeapp.data.prefs.AppTheme
import com.ultratv.tv.nativeapp.data.prefs.UserPreferencesStore
import com.ultratv.tv.nativeapp.data.repo.ProviderRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductBootstrap @Inject constructor(
    private val prefs: UserPreferencesStore,
    private val providers: ProviderRepository,
) {
    sealed class Result {
        data object AlreadyConfigured : Result()
        data object Success : Result()
        data class Failed(val message: String) : Result()
    }

    suspend fun runIfNeeded(onProgress: (String) -> Unit): Result {
        if (providers.observeProviders().first().isNotEmpty()) {
            return Result.AlreadyConfigured
        }
        if (!ProductConfig.AUTO_BOOTSTRAP_IPTV_ORG) {
            return Result.Failed("Auto bootstrap disabled")
        }

        return runCatching {
            applyDefaults(onProgress)
            val preset = IptvOrgCatalog.presets.first { it.id == ProductConfig.DEFAULT_IPTV_ORG_PRESET_ID }
            onProgress("Добавление ${preset.nameRu}…")
            val id = providers.addM3u(
                name = "${preset.emoji} iptv-org · ${preset.nameRu}",
                url = preset.playlistUrl,
                epgUrl = preset.epgUrl,
            )
            providers.setDefault(id)
            onProgress("Загрузка каналов…")
            providers.syncAll(id) { onProgress(it) }
            if (preset.epgUrl.isNotBlank()) {
                onProgress("Загрузка телепрограммы…")
                providers.syncXmltv(id) { onProgress(it) }
            }
            prefs.markOnboardingSeen()
            prefs.setLastSyncAt(System.currentTimeMillis())
            Result.Success
        }.getOrElse { Result.Failed(it.message ?: "Unknown error") }
    }

    private suspend fun applyDefaults(onProgress: (String) -> Unit) {
        onProgress("Настройка Zip-TV…")
        prefs.setLanguage(ProductConfig.DEFAULT_LANGUAGE)
        prefs.setTheme(AppTheme.AMOLED)
        prefs.setAutoSync(true)
        prefs.setTelemetry(ProductConfig.DEFAULT_TELEMETRY)
    }
}
