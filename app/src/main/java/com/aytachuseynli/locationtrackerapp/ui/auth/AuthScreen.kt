package com.aytachuseynli.locationtrackerapp.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aytachuseynli.locationtrackerapp.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aytachuseynli.locationtrackerapp.ui.auth.components.AuthFormCard
import com.aytachuseynli.locationtrackerapp.ui.auth.components.AuthHeader

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSignedIn) {
        if (uiState.isSignedIn) {
            onAuthSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthHeader(isLoginMode = uiState.isLoginMode)

            AuthFormCard(
                email = uiState.email,
                password = uiState.password,
                displayName = uiState.displayName,
                isLoginMode = uiState.isLoginMode,
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage,
                passwordVisible = passwordVisible,
                focusManager = focusManager,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onDisplayNameChange = viewModel::onDisplayNameChange,
                onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
                onSubmit = { if (uiState.isLoginMode) viewModel.signIn() else viewModel.signUp() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            AuthModeToggle(
                isLoginMode = uiState.isLoginMode,
                onToggle = viewModel::toggleAuthMode
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AuthModeToggle(
    isLoginMode: Boolean,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isLoginMode) stringResource(R.string.dont_have_account) else stringResource(R.string.already_have_account),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextButton(onClick = onToggle) {
            Text(
                text = if (isLoginMode) stringResource(R.string.sign_up) else stringResource(R.string.sign_in),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
