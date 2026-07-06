package tv.own.owntv

import android.content.Context
import java.io.File

/**
 * Copies bundled bootstrap playlists from assets into internal storage so [SyncManager]
 * can read them via an absolute path (same as user-picked local M3U files).
 */
class BootstrapAssets(private val context: Context) {

    fun resolvePlaylistPath(assetRelativePath: String): String {
        val fileName = assetRelativePath.substringAfterLast('/')
        val dest = File(context.filesDir, "$DIR/$fileName")
        if (!dest.exists() || dest.length() == 0L) {
            dest.parentFile?.mkdirs()
            context.assets.open(assetRelativePath).use { input ->
                dest.outputStream().use { output -> input.copyTo(output) }
            }
        }
        return dest.absolutePath
    }

    private companion object {
        const val DIR = "bootstrap-playlists"
    }
}
