package com.aytachuseynli.locationtrackerapp.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aytachuseynli.locationtrackerapp.R
import com.aytachuseynli.locationtrackerapp.domain.model.LocationData
import com.aytachuseynli.locationtrackerapp.ui.theme.CardShape
import com.aytachuseynli.locationtrackerapp.ui.theme.ChipShape
import com.aytachuseynli.locationtrackerapp.ui.theme.Warning60
import com.aytachuseynli.locationtrackerapp.ui.theme.Warning90
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LocationItem(
    location: LocationData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LocationIcon(isSynced = location.isSynced)
            Spacer(modifier = Modifier.width(12.dp))
            LocationDetails(
                location = location,
                modifier = Modifier.weight(1f)
            )
            LocationStatus(location = location)
        }
    }
}

@Composable
private fun LocationIcon(isSynced: Boolean) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(
                if (isSynced)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    Warning90
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = if (isSynced)
                MaterialTheme.colorScheme.primary
            else
                Warning60
        )
    }
}

@Composable
private fun LocationDetails(
    location: LocationData,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "%.6f, %.6f".format(location.latitude, location.longitude),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = formatTimestamp(location.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LocationStatus(location: LocationData) {
    Column(horizontalAlignment = Alignment.End) {
        SyncStatusChip(isSynced = location.isSynced)
        Spacer(modifier = Modifier.height(6.dp))
        BatteryIndicator(batteryLevel = location.batteryLevel)
    }
}

@Composable
private fun SyncStatusChip(isSynced: Boolean) {
    Surface(
        shape = ChipShape,
        color = if (isSynced)
            MaterialTheme.colorScheme.primaryContainer
        else
            Warning90
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isSynced) Icons.Default.Check else Icons.Default.CloudOff,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = if (isSynced)
                    MaterialTheme.colorScheme.primary
                else
                    Warning60
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isSynced) stringResource(R.string.synced) else stringResource(R.string.pending),
                style = MaterialTheme.typography.labelSmall,
                color = if (isSynced)
                    MaterialTheme.colorScheme.primary
                else
                    Warning60
            )
        }
    }
}

@Composable
private fun BatteryIndicator(batteryLevel: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.BatteryFull,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$batteryLevel%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
