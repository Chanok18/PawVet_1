package com.example.pawvet_1.ui.screens.citas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawvet_1.data.model.Cita
import com.example.pawvet_1.ui.components.PawVetBaseScreen
import com.example.pawvet_1.ui.theme.PawVetAccent
import com.example.pawvet_1.ui.theme.PawVetBodyFont
import com.example.pawvet_1.ui.theme.PawVetBorder
import com.example.pawvet_1.ui.theme.PawVetPrimary
import com.example.pawvet_1.ui.theme.PawVetSurface
import com.example.pawvet_1.ui.theme.PawVetTextPrimary
import com.example.pawvet_1.ui.theme.PawVetTextSecondary
import com.example.pawvet_1.ui.viewmodel.CitaViewModel

@Composable
fun CitasScreen(
    viewModel: CitaViewModel,
    onNavigateToForm: (Int) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    PawVetBaseScreen(
        title = "Citas Médicas",
        onBack = onBack,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToForm(0) },
                containerColor = PawVetPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva cita")
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Gestiona consultas, vacunas y controles con el mismo estilo premium del home.",
                style = MaterialTheme.typography.bodyMedium,
                color = PawVetTextSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 18.dp)
            )

            if (uiState.listaCitas.isEmpty()) {
                EmptyCitasState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 110.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(uiState.listaCitas) { cita ->
                        PremiumCitaCard(
                            cita = cita,
                            onClick = { onNavigateToForm(cita.id) },
                            onDelete = { viewModel.eliminarCita(cita) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyCitasState() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = PawVetSurface,
        border = BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.75f)),
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(PawVetPrimary.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = PawVetPrimary)
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Aún no tienes citas registradas",
                style = MaterialTheme.typography.titleMedium,
                color = PawVetTextPrimary
            )
            Text(
                text = "Agenda la primera y aquí aparecerá con el mismo formato elegante del dashboard.",
                style = MaterialTheme.typography.bodyMedium,
                color = PawVetTextSecondary,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
private fun PremiumCitaCard(
    cita: Cita,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val accent = when (cita.tipo.lowercase()) {
        "vacunación", "vacuna" -> Color(0xFF2A9D8F)
        "emergencia", "urgencia" -> PawVetAccent
        "control", "control médico" -> Color(0xFF7AB0A7)
        else -> PawVetPrimary
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(30.dp),
        color = PawVetSurface,
        border = BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.75f)),
        shadowElevation = 10.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(accent.copy(alpha = 0.10f), Color.Transparent)
                    )
                )
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(accent.copy(alpha = 0.14f), RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = accent, modifier = Modifier.size(24.dp))
                }

                Spacer(modifier = Modifier.size(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cita.tipo,
                        style = MaterialTheme.typography.titleMedium,
                        color = PawVetTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = PawVetTextSecondary, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = cita.fecha,
                            fontFamily = PawVetBodyFont,
                            fontSize = 13.sp,
                            color = PawVetTextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = PawVetTextSecondary, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = cita.hora,
                            fontFamily = PawVetBodyFont,
                            fontSize = 13.sp,
                            color = PawVetTextSecondary
                        )
                    }
                }

                Surface(
                    modifier = Modifier.clickable { onDelete() },
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFF4F1)
                ) {
                    Box(
                        modifier = Modifier.padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = PawVetAccent, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}
