package com.ultratv.tv.nativeapp.ui.common

import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

/** Three-bucket form-factor used by the adaptive nav / layout switches. */
enum class FormFactor { Compact, Medium, Expanded }

@Composable
fun rememberIsTelevision(): Boolean {
    val ctx = LocalContext.current
    val uiMode = LocalConfiguration.current.uiMode and Configuration.UI_MODE_TYPE_MASK
    return uiMode == Configuration.UI_MODE_TYPE_TELEVISION ||
        ctx.packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)
}

/**
 * Maps width (+ Android TV) to layout bucket.
 * TV boxes / leanback always use sidebar layout regardless of reported width.
 */
@Composable
fun rememberFormFactor(): FormFactor {
    if (rememberIsTelevision()) return FormFactor.Expanded
    val w = LocalConfiguration.current.screenWidthDp
    return when {
        w < 600 -> FormFactor.Compact
        w < 840 -> FormFactor.Medium
        else -> FormFactor.Expanded
    }
}
