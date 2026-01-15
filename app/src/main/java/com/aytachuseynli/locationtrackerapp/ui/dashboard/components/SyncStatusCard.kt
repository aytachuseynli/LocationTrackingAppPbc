package com.aytachuseynli.locationtrackerapp.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aytachuseynli.locationtrackerapp.R
import com.aytachuseynli.locationtrackerapp.domain.model.SyncStatus
import com.aytachuseynli.locationtrackerapp.ui.theme.Blue60
import com.aytachuseynli.locationtrackerapp.ui.theme.Blue90
import com.aytachuseynli.locationtrackerapp.ui.theme.ButtonShape
import com.aytachuseynli.locationtrackerapp.ui.theme.CardShape
import com.aytachuseynli.locationtrackerapp.ui.theme.Success60
import com.aytachuseynli.locationtrackerapp.ui.theme.Success90

@Composable
fun SyncStatusCard(
    unsyncedCount: Int,
    syncStatus: SyncStatus,
    onSyncClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusInfo = getSyncStatusInfo(syncStatus)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                SyncStatusIcon(
                    syncStatus = syncStatus,
                    statusColor = statusInfo.color,
                    statusBgColor = statusInfo.bgColor,
                    statusIcon = statusInfo.icon
                )
                Spacer(modifier = Modifier.width(16.dp))
                SyncStatusInfo(
                    syncStatus = syncStatus,
                    unsyncedCount = unsyncedCount
                )
            }

            SyncButton(
                syncStatus = syncStatus,
                unsyncedCount = unsyncedCount,
                onClick = onSyncClick
            )
        }
    }
}

private data class StatusInfo(
    val icon: ImageVector,
    val color: Color,
    val bgColor: Color
)

@Composable
private fun getSyncStatusInfo(syncStatus: SyncStatus): StatusInfo {
    return when (syncStatus) {
        is SyncStatus.Idle -> StatusInfo(
            Icons.Default.CloudOff,
            MaterialTheme.colorScheme.onSurfaceVariant,
            MaterialTheme.colorScheme.surfaceVariant
        )
        is SyncStatus.Syncing -> StatusInfo(
            Icons.Default.CloudSync,
            Blue60,
            Blue90
        )
        is SyncStatus.Success -> StatusInfo(
            Icons.Default.Cloud,
            Success60,
            Success90
        )
        is SyncStatus.Error -> StatusInfo(
            Icons.Default.Error,
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.errorContainer
        )
    }
}

@Composable
private fun SyncStatusIcon(
    syncStatus: SyncStatus,
    statusColor: Color,
    statusBgColor: Color,
    statusIcon: ImageVector
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color = statusBgColor),
        contentAlignment = Alignment.Center
    ) {
        if (syncStatus is SyncStatus.Syncing) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = statusColor,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                imageVector = statusIcon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = statusColor
            )
        }
    }
}

@Composable
private fun SyncStatusInfo(
    syncStatus: SyncStatus,
    unsyncedCount: Int
) {
    Column {
        Text(
            text = stringResource(R.string.sync_status),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = when (syncStatus) {
                is SyncStatus.Idle -> if (unsyncedCount > 0) stringResource(R.string.locations_pending, unsyncedCount) else stringResource(R.string.all_synced)
                is SyncStatus.Syncing -> stringResource(R.string.syncing_data)
                is SyncStatus.Success -> stringResource(R.string.synced_locations, syncStatus.syncedCount)
                is SyncStatus.Error -> syncStatus.message
            },
            style = MaterialTheme.typography.bodySmall,
            color = when (syncStatus) {
                is SyncStatus.Error -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SyncButton(
    syncStatus: SyncStatus,
    unsyncedCount: Int,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        enabled = syncStatus !is SyncStatus.Syncing && unsyncedCount > 0,
        shape = ButtonShape,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = stringResource(R.string.sync),
            style = MaterialTheme.typography.labelLarge
        )
    }
}
