package com.ultratv.tv.nativeapp.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * App-wide toast layer. Must NOT use a full-screen hit-target —
 * that blocks every click/focus event on TV and phones.
 */
object Toaster {
    enum class Kind { INFO, OK, ERROR }
    data class Msg(val text: String, val kind: Kind, val id: Long = System.nanoTime())

    val state = MutableStateFlow<Msg?>(null)

    fun show(text: String, kind: Kind = Kind.INFO) {
        state.value = Msg(text, kind)
    }
    fun ok(text: String) = show(text, Kind.OK)
    fun err(text: String) = show(text, Kind.ERROR)

    fun clear() { state.value = null }
}

@OptIn(androidx.tv.material3.ExperimentalTvMaterial3Api::class)
@Composable
fun BoxScope.ToasterHost() {
    val msg by Toaster.state.collectAsState()

    LaunchedEffect(msg?.id) {
        if (msg != null) {
            delay(3500)
            if (Toaster.state.value?.id == msg?.id) Toaster.clear()
        }
    }

    if (msg == null) return

    Box(
        Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = safeBottomPadding(24.dp)),
    ) {
        AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
            val m = msg ?: return@AnimatedVisibility
            val (bg, fg) = when (m.kind) {
                Toaster.Kind.OK -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
                Toaster.Kind.ERROR -> androidx.compose.ui.graphics.Color(0xFFC23A3A) to androidx.compose.ui.graphics.Color.White
                Toaster.Kind.INFO -> MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.onSurface
            }
            Text(
                text = m.text,
                color = fg,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(bg)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
            )
        }
    }
}
