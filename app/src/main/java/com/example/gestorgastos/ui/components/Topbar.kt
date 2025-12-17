package com.example.gestorgastos.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gestorgastos.data.AppThemeMode
import com.example.gestorgastos.data.ThemeManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val themeMode by ThemeManager.themeMode.collectAsState()

    val isDark = when (themeMode) {
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val containerColor = if (isDark) Color(0xFF121212) else Color(0xFFECE4F4)

    val contentColor = if (isDark) Color.White else Color.Black

    val borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Transparent

    val shadowElevation = if (isDark) 0.dp else 14.dp

    Column(modifier = modifier) {

        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = containerColor,
                titleContentColor = contentColor,
                navigationIconContentColor = contentColor,
                actionIconContentColor = contentColor
            ),
            windowInsets = WindowInsets.statusBars,

            modifier = Modifier.shadow(elevation = shadowElevation, shape = RectangleShape),

            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = contentColor
                        )
                    }
                }
            },

            actions = {
                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes",
                        tint = contentColor
                    )
                }
            }
        )

        if (isDark) {
            HorizontalDivider(
                thickness = 1.dp,
                color = borderColor
            )
        }
    }
}