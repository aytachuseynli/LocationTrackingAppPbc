package com.aytachuseynli.locationtrackerapp.ui.permission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aytachuseynli.locationtrackerapp.R
import com.aytachuseynli.locationtrackerapp.ui.permission.components.FeatureItem
import com.aytachuseynli.locationtrackerapp.ui.permission.components.PermissionHeader
import com.aytachuseynli.locationtrackerapp.ui.permission.components.WhyWeNeedCard
import com.aytachuseynli.locationtrackerapp.ui.theme.ButtonShape

@Composable
fun PermissionRequestScreen(onRequestPermissions: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            PermissionHeader()

            PermissionContent(onRequestPermissions = onRequestPermissions)
        }
    }
}

@Composable
private fun PermissionContent(onRequestPermissions: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WhyWeNeedCard(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.features),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        FeaturesList()

        Spacer(modifier = Modifier.height(32.dp))

        GrantPermissionButton(onClick = onRequestPermissions)

        Spacer(modifier = Modifier.height(16.dp))

        PrivacyNote()

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun FeaturesList() {
    Column {
        FeatureItem(
            icon = Icons.Default.MyLocation,
            title = stringResource(R.string.background_tracking),
            description = stringResource(R.string.background_tracking_desc)
        )
        Spacer(modifier = Modifier.height(12.dp))
        FeatureItem(
            icon = Icons.Default.Storage,
            title = stringResource(R.string.offline_storage),
            description = stringResource(R.string.offline_storage_desc)
        )
        Spacer(modifier = Modifier.height(12.dp))
        FeatureItem(
            icon = Icons.Default.Cloud,
            title = stringResource(R.string.auto_sync),
            description = stringResource(R.string.auto_sync_desc)
        )
        Spacer(modifier = Modifier.height(12.dp))
        FeatureItem(
            icon = Icons.Default.BatteryChargingFull,
            title = stringResource(R.string.battery_optimized),
            description = stringResource(R.string.battery_optimized_desc)
        )
    }
}

@Composable
private fun GrantPermissionButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.grant_permission),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PrivacyNote() {
    Text(
        text = stringResource(R.string.privacy_note),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}
