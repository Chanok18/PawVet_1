package com.example.pawvet_1.ui.screens.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawvet_1.data.model.Cita
import com.example.pawvet_1.data.model.Mascota
import com.example.pawvet_1.data.model.Servicio
import com.example.pawvet_1.ui.components.PawVetBaseScreen
import com.example.pawvet_1.ui.theme.PawVetAccent
import com.example.pawvet_1.ui.theme.PawVetBorder
import com.example.pawvet_1.ui.theme.PawVetBodyFont
import com.example.pawvet_1.ui.theme.PawVetPrimary
import com.example.pawvet_1.ui.theme.PawVetSurface
import com.example.pawvet_1.ui.theme.PawVetTextPrimary
import com.example.pawvet_1.ui.theme.PawVetTextSecondary
import com.example.pawvet_1.ui.viewmodel.CitaViewModel
import com.example.pawvet_1.ui.viewmodel.MascotaViewModel
import com.example.pawvet_1.ui.viewmodel.ServicioViewModel

@Composable
fun PerfilScreen(
    userName: String,
    mascotaViewModel: MascotaViewModel,
    citaViewModel: CitaViewModel,
    servicioViewModel: ServicioViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onMascotaClick: (Int) -> Unit,
    onAddMascotaClick: () -> Unit,
    onEditMascotaClick: (Int) -> Unit,
    onEditCitaClick: (Int) -> Unit
) {
    val mascotaState by mascotaViewModel.uiState.collectAsState()
    val citaState by citaViewModel.uiState.collectAsState()
    val servicioState by servicioViewModel.uiState.collectAsState()

    PawVetBaseScreen(
        title = "Mi Perfil",
        onBack = onBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 110.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(118.dp),
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.8f)),
                shadowElevation = 10.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = PawVetPrimary, modifier = Modifier.size(52.dp))
                }
            }

            Text(
                text = userName.ifBlank { "Cuenta PawVet" },
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = PawVetBodyFont,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                ),
                color = PawVetTextPrimary,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Todo el historial y cuidado de tus mascotas, en un solo lugar.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = PawVetBodyFont,
                    fontWeight = FontWeight.Medium
                ),
                color = PawVetTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
            )

            Button(
                onClick = onLogout,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PawVetAccent.copy(alpha = 0.9f))
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Text("Cerrar sesión", modifier = Modifier.padding(start = 8.dp))
            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionHeader(
                title = "Mis Mascotas",
                onAddClick = onAddMascotaClick
            )
            if (mascotaState.listaMascotas.isEmpty()) {
                EmptyStateCard("No tienes mascotas registradas.")
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

            Spacer(modifier = Modifier.height(18.dp))

            SectionHeader(title = "Próximas Citas")
            if (citaState.listaCitas.isEmpty()) {
                EmptyStateCard("No hay citas médicas programadas.")
            } else {
                citaState.listaCitas.forEach { cita ->
                    CitaItem(
                        cita = cita,
                        onEdit = { onEditCitaClick(cita.id) },
                        onDelete = { citaViewModel.eliminarCita(cita) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            SectionHeader(title = "Reservas Estéticas")
            if (servicioState.listaServicios.isEmpty()) {
                EmptyStateCard("No hay reservas estéticas programadas.")
            } else {
                servicioState.listaServicios.forEach { servicio ->
                    ServicioItem(
                        servicio = servicio,
                        onDelete = { servicioViewModel.eliminarServicio(servicio) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, onAddClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = PawVetBodyFont,
                fontWeight = FontWeight.ExtraBold
            ),
            color = PawVetTextPrimary
        )
        if (onAddClick != null) {
            Button(
                onClick = onAddClick,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PawVetPrimary)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Agregar", modifier = Modifier.padding(start = 6.dp))
            }
        }
    }
}

@Composable
private fun EmptyStateCard(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = PawVetSurface,
        border = BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.8f)),
        shadowElevation = 8.dp
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = PawVetTextSecondary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MascotaItem(mascota: Mascota, onClick: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(26.dp),
        color = PawVetSurface,
        border = BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.8f)),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center) {
                Text(text = if (mascota.tipo.lowercase().contains("gato")) "🐱" else "🐶", fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.size(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = mascota.nombre, style = MaterialTheme.typography.titleMedium, color = PawVetTextPrimary)
                Text(text = mascota.raza, style = MaterialTheme.typography.bodyMedium, color = PawVetTextSecondary)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = PawVetPrimary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = PawVetAccent)
            }
        }
    }
}

@Composable
private fun CitaItem(cita: Cita, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(26.dp),
        color = PawVetSurface,
        border = BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.8f)),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = PawVetPrimary)
            }
            Spacer(modifier = Modifier.size(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = cita.tipo, style = MaterialTheme.typography.titleMedium, color = PawVetTextPrimary)
                Text(text = "${cita.fecha} · ${cita.hora}", style = MaterialTheme.typography.bodyMedium, color = PawVetTextSecondary)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = PawVetPrimary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = PawVetAccent)
            }
        }
    }
}

@Composable
private fun ServicioItem(servicio: Servicio, onDelete: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(26.dp),
        color = PawVetSurface,
        border = BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.8f)),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Star, contentDescription = null, tint = PawVetPrimary)
            }
            Spacer(modifier = Modifier.size(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = servicio.tipoServicio, style = MaterialTheme.typography.titleMedium, color = PawVetTextPrimary)
                Text(text = "${servicio.fecha} · ${servicio.hora}", style = MaterialTheme.typography.bodyMedium, color = PawVetTextSecondary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = PawVetAccent)
            }
        }
    }
}
