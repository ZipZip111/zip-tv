package tv.own.owntv.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import tv.own.owntv.ui.theme.AccentCyan
import tv.own.owntv.ui.theme.OwnTVTheme

/**
 * Zip-TV wordmark — green play-mark (#9DFF4F) on theme-adaptive background.
 */
@Composable
fun BrandLockup(
    modifier: Modifier = Modifier,
    markSize: Int = 36,
    textSize: Int = 26,
) {
    val colors = OwnTVTheme.colors
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Rounded-square play mark
        val markShape = RoundedCornerShape(percent = 28)
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(markSize.dp)
                .clip(markShape)
                .background(colors.card)
                .border(2.dp, AccentCyan, markShape),
            contentAlignment = Alignment.Center,
        ) {
            OwnTVIcon(
                icon = OwnTVIcon.PLAY,
                tint = AccentCyan,
                filled = true,
                modifier = Modifier
                    .padding(start = (markSize * 0.06f).dp)
                    .size((markSize * 0.5f).dp),
            )
        }
        Text(
            text = buildAnnotatedString {
                withStyle(androidx.compose.ui.text.SpanStyle(color = colors.textPrimary, fontWeight = FontWeight.Bold)) {
                    append("Zip")
                }
                withStyle(androidx.compose.ui.text.SpanStyle(color = AccentCyan, fontWeight = FontWeight.Bold)) {
                    append("-TV")
                }
            },
            fontSize = textSize.sp,
        )
    }
}
