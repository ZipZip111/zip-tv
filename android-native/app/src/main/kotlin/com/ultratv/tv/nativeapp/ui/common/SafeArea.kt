package com.ultratv.tv.nativeapp.ui.common

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Bottom inset for gesture / 3-button navigation (edge-to-edge). */
@Composable
fun navigationBarInset(): Dp =
    WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

/** Top inset for status bar when drawing edge-to-edge. */
@Composable
fun statusBarInset(): Dp =
    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

/** Minimum bottom padding combining system nav and app chrome. */
@Composable
fun safeBottomPadding(extra: Dp = 0.dp): Dp = navigationBarInset() + extra
