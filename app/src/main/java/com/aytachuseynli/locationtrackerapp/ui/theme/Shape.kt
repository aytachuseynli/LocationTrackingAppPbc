package com.aytachuseynli.locationtrackerapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ============================================
// Professional Shape System
// Consistent rounded corners across the app
// ============================================

val Shapes = Shapes(
    // Extra small - chips, badges
    extraSmall = RoundedCornerShape(4.dp),

    // Small - text fields, small buttons
    small = RoundedCornerShape(8.dp),

    // Medium - cards, dialogs
    medium = RoundedCornerShape(16.dp),

    // Large - bottom sheets, large cards
    large = RoundedCornerShape(24.dp),

    // Extra large - full screen dialogs
    extraLarge = RoundedCornerShape(32.dp)
)

// Custom shape extensions for specific use cases
val CardShape = RoundedCornerShape(16.dp)
val ButtonShape = RoundedCornerShape(12.dp)
val InputFieldShape = RoundedCornerShape(12.dp)
val ChipShape = RoundedCornerShape(8.dp)
val BottomSheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
val FloatingActionButtonShape = RoundedCornerShape(16.dp)
