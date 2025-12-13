package com.example.gestorgastos.ui.screens.profile

import android.Manifest
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var showExplanationDialog by remember { mutableStateOf(false) }

    // 1. LANZADOR DE CÁMARA (Obtiene la foto pequeña/thumbnail)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            viewModel.onImageCaptured(bitmap)
        }
    }

    // 2. LANZADOR DE PERMISO (Pide permiso al sistema)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Si aceptó, abrimos la cámara
            cameraLauncher.launch(null)
        } else {
            // MANEJO DE NEGACIÓN: Mostramos diálogo o aviso
            showExplanationDialog = true
        }
    }

    // --- UI ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // FOTO DE PERFIL (Círculo grande)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5))
                    .border(4.dp, Color(0xFF7E57C2), CircleShape)
            ) {
                if (viewModel.profileImage != null) {
                    Image(
                        bitmap = viewModel.profileImage!!.asImageBitmap(),
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Usuario Invitado",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Gestiona tu identidad y permisos",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(40.dp))

            // BOTÓN DE CÁMARA (Funcionalidad Central)
            Button(
                onClick = {
                    // Lógica para pedir permiso
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tomar Foto de Perfil")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Volver", color = Color.Gray)
            }
        }
    }

    // DIÁLOGO DE NEGACIÓN (Requisito Obligatorio)
    if (showExplanationDialog) {
        AlertDialog(
            onDismissRequest = { showExplanationDialog = false },
            title = { Text("Permiso Necesario") },
            text = { Text("Para poder establecer tu foto de perfil, necesitamos acceso a la cámara. Sin este permiso, esta función no está disponible.") },
            confirmButton = {
                TextButton(onClick = {
                    showExplanationDialog = false
                    // Opcional: Podrías intentar pedirlo de nuevo aquí
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Reintentar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showExplanationDialog = false
                    Toast.makeText(context, "Función deshabilitada", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}