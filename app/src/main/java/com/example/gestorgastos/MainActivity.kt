package com.example.gestorgastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestorgastos.ui.navigation.AppNavigation
import com.example.gestorgastos.ui.screens.home.MainScreen
import com.example.gestorgastos.ui.theme.GestorGastosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestorGastosTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun Greeting(name: String, materia: String, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
    ) {
        Text(
            text = "Hello $name!"
        )
        Text(
            text = "La materia es $materia",
            modifier = Modifier.padding(top = 4.dp)
            )
        }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    GestorGastosTheme {
//        Greeting("Android")
//    }
//}