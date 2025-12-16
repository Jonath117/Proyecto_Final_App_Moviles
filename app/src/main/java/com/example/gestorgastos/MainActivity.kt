package com.example.gestorgastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.gestorgastos.data.AppThemeMode
import com.example.gestorgastos.data.ThemeManager
import com.example.gestorgastos.navigation.AppNavigation
import com.example.gestorgastos.ui.theme.GestorGastosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.init(this)

        setContent {
            val themeMode by ThemeManager.themeMode.collectAsState()

            val isDarkTheme = when (themeMode) {
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            GestorGastosTheme (
                darkTheme = isDarkTheme
            ) {
                AppNavigation()
            }
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    GestorGastosTheme {
//        Greeting("Android")
//    }
//}