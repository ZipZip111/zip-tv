package com.ultratv.tv.nativeapp.ui.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.ultratv.tv.nativeapp.ProductConfig
import com.ultratv.tv.nativeapp.bootstrap.ProductBootstrap
import com.ultratv.tv.nativeapp.data.repo.ProviderRepository
import com.ultratv.tv.nativeapp.ui.common.Toaster
import com.ultratv.tv.nativeapp.ui.theme.UltraFonts
import com.ultratv.tv.nativeapp.ui.theme.UltraTokens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SetupUiState {
    data object Hidden : SetupUiState
    data class Running(val message: String) : SetupUiState
}

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val bootstrap: ProductBootstrap,
    private val providers: ProviderRepository,
) : ViewModel() {

    private val _message = MutableStateFlow("Подготовка…")
    private val _running = MutableStateFlow(false)
    private var started = false

    val state: StateFlow<SetupUiState> = combine(
        providers.observeProviders(),
        _running,
        _message,
    ) { list, running, msg ->
        when {
            !ProductConfig.AUTO_BOOTSTRAP_IPTV_ORG -> SetupUiState.Hidden
            list.isNotEmpty() -> SetupUiState.Hidden
            !running -> SetupUiState.Hidden
            else -> SetupUiState.Running(msg)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SetupUiState.Hidden)

    fun startIfNeeded() {
        if (started) return
        started = true
        viewModelScope.launch {
            if (providers.observeProviders().first().isNotEmpty()) return@launch
            _running.value = true
            when (val result = bootstrap.runIfNeeded { _message.value = it }) {
                is ProductBootstrap.Result.Success,
                is ProductBootstrap.Result.AlreadyConfigured,
                -> _running.value = false
                is ProductBootstrap.Result.Failed -> {
                    _running.value = false
                    Toaster.err(
                        "Не удалось настроить автоматически. Настройки → Каталог iptv-org → 🇷🇺 Россия",
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SetupOverlay(vm: SetupViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        vm.startIfNeeded()
    }

    when (val s = state) {
        SetupUiState.Hidden -> Unit
        is SetupUiState.Running -> FullScreenSetup(
            title = "Настраиваем ${ProductConfig.APP_DISPLAY_NAME}",
            subtitle = s.message,
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun FullScreenSetup(title: String, subtitle: String) {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        UltraTokens.AccentGhost,
                        MaterialTheme.colorScheme.background,
                    ),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(48.dp),
        ) {
            Box(
                Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(listOf(UltraTokens.Accent, UltraTokens.Accent2)),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text("Z", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = UltraTokens.CtaFgOnCta)
            }
            Spacer(Modifier.height(28.dp))
            Text(
                title,
                fontFamily = UltraFonts.Serif,
                fontSize = 42.sp,
                color = UltraTokens.Fg,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                subtitle,
                color = UltraTokens.Fg2,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "iptv-org · zip-dev.ru",
                color = UltraTokens.Accent,
                fontSize = 14.sp,
                letterSpacing = 1.sp,
            )
        }
    }
}
