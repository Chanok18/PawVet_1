package com.example.pawvet_1.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawvet_1.data.model.Cita
import com.example.pawvet_1.data.model.Mascota
import com.example.pawvet_1.ui.components.PawVetBaseScreen
import com.example.pawvet_1.ui.viewmodel.AuthViewModel
import com.example.pawvet_1.ui.viewmodel.CitaViewModel
import com.example.pawvet_1.ui.viewmodel.MascotaViewModel

@Composable
fun PerfilScreen(
    authViewModel: AuthViewModel,
    mascotaViewModel: MascotaViewModel,
    citaViewModel: CitaViewModel,
    onLogout: () -> Unit,
    onBack: () -> Unit,
    onMascotaClick: (Int) -> Unit,
    onAddMascotaClick: () -> Unit,
    onEditMascotaClick: (Int) -> Unit,
    onEditCitaClick: (Int) -> Unit
) {
    val authState by authViewModel.uiState.collectAsState()
    val mascotaState by mascotaViewModel.uiState.collectAsState()
    val citaState by citaViewModel.uiState.collectAsState()

    // Sincronizar datos cuando el usuario esté disponible
    LaunchedEffect(authState.usuario) {
        if (authState.usuario != null) {
            mascotaViewModel.listarMascotas()
            citaViewModel.listarCitas()
        }
    }

    PawVetBaseScreen(
        title = "Mi Perfil PawVet",
        onBack = onBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header del Usuario Real
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            
            Text(
                text = authState.usuario?.email ?: "Usuario PawVet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )
            
            TextButton(
                onClick = onLogout,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 1. SECCIÓN: MIS MASCOTAS (CRUD)
            SectionHeader(title = "Mis Mascotas 🐾", onAddClick = onAddMascotaClick)
            
            if (mascotaState.listaMascotas.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Text("No tienes mascotas registradas aún.", modifier = Modifier.padding(16.dp), color = Color.Gray)
                }
            } else {
                mascotaState.listaMascotas.forEach { mascota ->
                    MascotaItem(
                        mascota = mascota,
                        onClick = { onMascotaClick(mascota.id) },
                        onEdit = { onEditMascotaClick(mascota.id) },
                        onDelete = { mascotaViewModel.eliminarMascota(mascota) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. SECCIÓN: CITAS (CRUD)
            SectionHeader(title = "Próximas Citas 📅")
            if (citaState.listaCitas.isEmpty()) {
                Text("Sin citas pendientes", color = Color.Gray)
            } else {
                citaState.listaCitas.forEach { cita ->
                    CitaItem(
                        cita = cita,
                        onEdit = { onEditCitaClick(cita.id) },
                        onDelete = { citaViewModel.eliminarCita(cita) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String, onAddClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        if (onAddClick != null) {
            IconButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Añadir", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun MascotaItem(mascota: Mascota, onClick: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = if (mascota.tipo == "Gato") "🐱" else "🐶", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = mascota.nombre, fontWeight = FontWeight.Bold)
                Text(text = mascota.raza, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp)) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.error) }
        }
    }
}

@Composable
fun CitaItem(cita: Cita, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = cita.tipo, fontWeight = FontWeight.Bold)
                Text(text = "${cita.fecha} - ${cita.hora}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp)) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.error) }
        }
    }
}
