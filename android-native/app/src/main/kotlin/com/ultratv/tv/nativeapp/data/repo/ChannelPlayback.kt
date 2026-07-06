package com.ultratv.tv.nativeapp.data.repo

import com.ultratv.tv.nativeapp.data.db.ChannelEntity
import javax.inject.Inject
import javax.inject.Singleton

/** Resolves play URLs and seeds [PlaybackContext] for any screen that starts live TV. */
@Singleton
class ChannelPlayback @Inject constructor(
    private val providers: ProviderRepository,
    private val playback: PlaybackContext,
) {
    suspend fun resolveUrl(channelId: Long, storedUrl: String): String {
        if (!storedUrl.startsWith("stalker://")) return storedUrl
        return providers.resolvePlayUrl(channelId, storedUrl)
    }

    fun register(channel: ChannelEntity, resolvedUrl: String) {
        playback.set(
            PlaybackContext.Item(
                providerId = channel.providerId,
                kind = "LIVE",
                remoteId = channel.remoteId,
                title = channel.name,
                poster = channel.logo,
                streamUrl = resolvedUrl,
            ),
        )
    }
}
