package com.aytachuseynli.locationtrackerapp.ui.auth.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aytachuseynli.locationtrackerapp.R
import com.aytachuseynli.locationtrackerapp.ui.components.GradientHeader
import com.aytachuseynli.locationtrackerapp.ui.theme.Blue80
import com.aytachuseynli.locationtrackerapp.ui.theme.Blue95

@Composable
fun AuthHeader(isLoginMode: Boolean) {
    GradientHeader(height = 300.dp) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Blue95
            )
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedLogo(isLoginMode = isLoginMode)
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedTitle(isLoginMode = isLoginMode)
        }
    }
}

@Composable
private fun AnimatedLogo(isLoginMode: Boolean) {
    val scale by animateFloatAsState(
        targetValue = if (isLoginMode) 1f else 0.9f,
        animationSpec = tween(300),
        label = "logo_scale"
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(Blue95.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = Blue95
        )
    }
}

@Composable
private fun AnimatedTitle(isLoginMode: Boolean) {
    AnimatedContent(
        targetState = isLoginMode,
        transitionSpec = {
            (slideInVertically { -it } + fadeIn()) togetherWith
                    (slideOutVertically { it } + fadeOut())
        },
        label = "title"
    ) { loginMode ->
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (loginMode) stringResource(R.string.welcome_back) else stringResource(R.string.create_account),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Blue95
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (loginMode) stringResource(R.string.sign_in_subtitle) else stringResource(R.string.sign_up_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Blue80.copy(alpha = 0.8f)
            )
        }
    }
}
