package com.example.pawvet_1.ui.screens.servicios

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawvet_1.ui.components.PawVetBaseScreen
import com.example.pawvet_1.navigation.Screen
import com.example.pawvet_1.ui.notifications.NotificationHelper
import com.example.pawvet_1.ui.screens.citas.PremiumFormSection
import com.example.pawvet_1.ui.theme.BlobBlue
import com.example.pawvet_1.ui.theme.BlobCoral
import com.example.pawvet_1.ui.theme.BlobGreen
import com.example.pawvet_1.ui.theme.BlobYellow
import com.example.pawvet_1.ui.theme.PawVetPrimary
import com.example.pawvet_1.ui.theme.PawVetTextPrimary
import com.example.pawvet_1.ui.viewmodel.MascotaViewModel
import com.example.pawvet_1.ui.viewmodel.ServicioViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ServicioFormScreen(
    servicioId: Int,
    viewModel: ServicioViewModel,
    mascotaViewModel: MascotaViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val mascotaState by mascotaViewModel.uiState.collectAsState()
    val servicioState by viewModel.uiState.collectAsState()

    var selectedMascotaId by remember { mutableStateOf<Int?>(null) }
    var selectedTipo by remember { mutableStateOf("Baño y Corte") }
    var selectedFechaMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var selectedHora by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var expandedMascotas by remember { mutableStateOf(false) }
    var expandedTipos by remember { mutableStateOf(false) }

    val tiposServicio = listOf("Baño y Corte", "Limpieza Dental", "Corte de Uñas", "Baño Medicado", "Spa Completo")
    val horarios = listOf("09:00 AM", "10:30 AM", "12:00 PM", "02:30 PM", "04:00 PM", "05:30 PM")
    val dateFormatter = SimpleDateFormat("EEEE, dd MMMM", Locale("es", "ES"))

    LaunchedEffect(servicioId) {
        if (servicioId > 0) {
            viewModel.seleccionarServicio(servicioId)
        } else {
            viewModel.resetSeleccion()
        }
    }

    LaunchedEffect(servicioState.servicioSeleccionado) {
        servicioState.servicioSeleccionado?.let { servicio ->
            selectedMascotaId = servicio.mascotaId
            selectedTipo = servicio.tipoServicio
            selectedHora = servicio.hora
        }
    }

    PawVetBaseScreen(
        title = if (servicioId > 0) "Editar Reserva Estética" else "Reserva Estética",
        onBack = onBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            PremiumFormSection(title = "Paciente", icon = Icons.Default.Favorite, iconBg = BlobBlue) {
                ExposedDropdownMenuBox(
                    expanded = expandedMascotas,
                    onExpandedChange = { expandedMascotas = !expandedMascotas }
                ) {
                    val mascota = mascotaState.listaMascotas.find { it.id == selectedMascotaId }
                    OutlinedTextField(
                        value = mascota?.nombre ?: "Selecciona tu mascota",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedMascotas) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PawVetPrimary,
                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                        )
                    )
                    ExposedDropdownMenu(expanded = expandedMascotas, onDismissRequest = { expandedMascotas = false }) {
                        mascotaState.listaMascotas.forEach { mascota ->
                            DropdownMenuItem(
                                text = { Text("${mascota.nombre} (${mascota.raza})") },
                                onClick = {
                                    selectedMascotaId = mascota.id
                                    expandedMascotas = false
                                }
                            )
                        }
                    }
                }
            }

            PremiumFormSection(title = "Servicio", icon = Icons.Default.Star, iconBg = BlobGreen) {
                ExposedDropdownMenuBox(
                    expanded = expandedTipos,
                    onExpandedChange = { expandedTipos = !expandedTipos }
                ) {
                    OutlinedTextField(
                        value = selectedTipo,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedTipos) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PawVetPrimary,
                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                        )
                    )
                    ExposedDropdownMenu(expanded = expandedTipos, onDismissRequest = { expandedTipos = false }) {
                        tiposServicio.forEach { tipo ->
                            DropdownMenuItem(
                                text = { Text(tipo) },
                                onClick = {
                                    selectedTipo = tipo
                                    expandedTipos = false
                                }
                            )
                        }
                    }
                }
            }

            PremiumFormSection(title = "Fecha", icon = Icons.Default.DateRange, iconBg = BlobYellow) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dateFormatter.format(Date(selectedFechaMillis)).replaceFirstChar { it.uppercase() },
                            color = PawVetTextPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Cambiar",
                            color = PawVetPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            PremiumFormSection(title = "Horario disponible", icon = Icons.Default.Check, iconBg = BlobCoral) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    horarios.forEach { hora ->
                        FilterChip(
                            selected = selectedHora == hora,
                            onClick = { selectedHora = hora },
                            label = { Text(hora, modifier = Modifier.padding(vertical = 4.dp)) },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PawVetPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = if (selectedHora == hora) PawVetPrimary else Color.LightGray.copy(alpha = 0.5f),
                                enabled = true,
                                selected = selectedHora == hora
                            )
                        )
                    }
                }
            }

            Button(
                onClick = {
                    selectedMascotaId?.let { idMascota ->
                        val finalDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedFechaMillis))
                        val mascotaNombre = mascotaState.listaMascotas.firstOrNull { it.id == idMascota }?.nombre ?: "tu mascota"
                        viewModel.guardarServicio(
                            id = servicioId,
                            mascotaId = idMascota,
                            tipo = selectedTipo,
                            fecha = finalDate,
                            hora = selectedHora
                        )
                        NotificationHelper.scheduleAppointmentReminder(
                            context = context,
                            uniqueName = NotificationHelper.buildReminderUniqueName("servicio", idMascota, finalDate, selectedHora),
                            title = "Recordatorio de reserva estetica",
                            body = "$mascotaNombre tiene $selectedTipo a las $selectedHora en PawVet.",
                            date = finalDate,
                            time = selectedHora,
                            targetRoute = Screen.Perfil.route
                        )
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                enabled = selectedMascotaId != null && selectedTipo.isNotBlank() && selectedHora.isNotBlank(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PawVetPrimary)
            ) {
                Text(
                    text = if (servicioId > 0) "Guardar reserva estética" else "Confirmar cita estética",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedFechaMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedFechaMillis = datePickerState.selectedDateMillis ?: selectedFechaMillis
                    showDatePicker = false
                }) {
                    Text("Aceptar", color = PawVetPrimary, fontWeight = FontWeight.Bold)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
