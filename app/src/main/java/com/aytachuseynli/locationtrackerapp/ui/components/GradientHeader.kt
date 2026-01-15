package com.aytachuseynli.locationtrackerapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aytachuseynli.locationtrackerapp.ui.theme.GradientEnd
import com.aytachuseynli.locationtrackerapp.ui.theme.GradientMiddle
import com.aytachuseynli.locationtrackerapp.ui.theme.GradientStart

@Composable
fun GradientHeader(
    modifier: Modifier = Modifier,
    height: Dp = 280.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientMiddle, GradientEnd)
                )
            )
            .padding(top = statusBarPadding)
            .height(height),
        contentAlignment = Alignment.Center,
        content = content
    )
}
