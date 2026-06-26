package com.example.pawvet_1.ui.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawvet_1.navigation.Screen
import com.example.pawvet_1.ui.notifications.NotificationHelper
import com.example.pawvet_1.ui.theme.BlobBlue
import com.example.pawvet_1.ui.theme.BlobCoral
import com.example.pawvet_1.ui.theme.BlobGreen
import com.example.pawvet_1.ui.theme.BlobYellow
import com.example.pawvet_1.ui.theme.PawVetBackground
import com.example.pawvet_1.ui.theme.PawVetPrimary
import com.example.pawvet_1.ui.theme.PawVetTextPrimary
import com.example.pawvet_1.ui.viewmodel.CitaViewModel
import com.example.pawvet_1.ui.viewmodel.MascotaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CitaFormScreen(
    citaId: Int,
    citaViewModel: CitaViewModel,
    mascotaViewModel: MascotaViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by citaViewModel.uiState.collectAsState()
    val mascotaState by mascotaViewModel.uiState.collectAsState()

    var selectedMascotaId by remember { mutableStateOf<Int?>(null) }
    var selectedMotivo by remember { mutableStateOf("") }
    var selectedFechaMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var selectedHora by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var expandedMascotas by remember { mutableStateOf(false) }
    var expandedMotivos by remember { mutableStateOf(false) }

    val motivos = listOf("Consulta General", "Vacunacion", "Desparasitacion", "Control Medico", "Urgencia")
    val horarios = listOf("09:00 AM", "10:30 AM", "12:00 PM", "02:30 PM", "04:00 PM", "05:30 PM")
    val dateFormatter = remember {
        SimpleDateFormat("EEEE, dd MMMM", Locale("es", "ES"))
    }
    val selectedDateText = remember(selectedFechaMillis) { formatDateForStorage(selectedFechaMillis) }

    LaunchedEffect(citaId) {
        if (citaId != 0) {
            citaViewModel.seleccionarCita(citaId)
        } else {
            citaViewModel.limpiarMensajeError()
            citaViewModel.resetSeleccion()
        }
    }

    LaunchedEffect(uiState.citaSeleccionada) {
        uiState.citaSeleccionada?.let { cita ->
            selectedMascotaId = cita.mascotaId
            selectedMotivo = cita.tipo
            selectedHora = cita.hora
            selectedFechaMillis = parseStoredDateToMillis(cita.fecha) ?: selectedFechaMillis
        }
    }

    Scaffold(
        containerColor = PawVetBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (citaId == 0) "Agendar Cita" else "Editar Cita",
                        fontWeight = FontWeight.ExtraBold,
                        color = PawVetTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PawVetPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
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
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMascotas) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PawVetPrimary,
                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandedMascotas,
                        onDismissRequest = { expandedMascotas = false }
                    ) {
                        mascotaState.listaMascotas.forEach { mascota ->
                            DropdownMenuItem(
                                text = { Text(mascota.nombre) },
                                onClick = {
                                    selectedMascotaId = mascota.id
                                    expandedMascotas = false
                                }
                            )
                        }
                    }
                }
            }

            PremiumFormSection(title = "Motivo de Consulta", icon = Icons.Default.Edit, iconBg = BlobGreen) {
                ExposedDropdownMenuBox(
                    expanded = expandedMotivos,
                    onExpandedChange = { expandedMotivos = !expandedMotivos }
                ) {
                    OutlinedTextField(
                        value = selectedMotivo.ifEmpty { "Selecciona el motivo" },
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMotivos) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PawVetPrimary,
                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandedMotivos,
                        onDismissRequest = { expandedMotivos = false }
                    ) {
                        motivos.forEach { motivo ->
                            DropdownMenuItem(
                                text = { Text(motivo) },
                                onClick = {
                                    selectedMotivo = motivo
                                    expandedMotivos = false
                                    citaViewModel.limpiarMensajeError()
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
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color.LightGray.copy(alpha = 0.5f)
                    )
                ) {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dateFormatter.format(Date(selectedFechaMillis)).replaceFirstChar { it.uppercase() },
                            fontWeight = FontWeight.Bold,
                            color = PawVetTextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = PawVetPrimary)
                    }
                }
            }

            PremiumFormSection(title = "Horario Disponible", icon = Icons.Default.Check, iconBg = BlobCoral) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    horarios.forEach { hora ->
                        val horarioPasado = isPastSchedule(selectedFechaMillis, hora)
                        FilterChip(
                            selected = selectedHora == hora,
                            onClick = {
                                selectedHora = hora
                                citaViewModel.limpiarMensajeError()
                            },
                            label = { Text(hora, modifier = Modifier.padding(vertical = 4.dp)) },
                            shape = RoundedCornerShape(12.dp),
                            enabled = !horarioPasado,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PawVetPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                disabledContainerColor = Color(0xFFF3F4F6),
                                disabledLabelColor = Color.Gray
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = if (selectedHora == hora) PawVetPrimary else Color.LightGray.copy(alpha = 0.5f),
                                enabled = !horarioPasado,
                                selected = selectedHora == hora
                            )
                        )
                    }
                }
            }

            uiState.mensajeError?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    selectedMascotaId?.let { mascotaId ->
                        val mascotaNombre = mascotaState.listaMascotas
                            .firstOrNull { it.id == mascotaId }
                            ?.nombre
                            ?: "tu mascota"
                        val citaGuardada = citaViewModel.guardarCita(
                            id = citaId,
                            mascotaId = mascotaId,
                            fecha = selectedDateText,
                            hora = selectedHora,
                            tipo = selectedMotivo
                        )
                        if (citaGuardada) {
                            NotificationHelper.scheduleAppointmentReminder(
                                context = context,
                                uniqueName = NotificationHelper.buildReminderUniqueName(
                                    "cita",
                                    mascotaId,
                                    selectedDateText,
                                    selectedHora
                                ),
                                title = "Recordatorio de cita medica",
                                body = "La cita de $mascotaNombre es a las $selectedHora. Te esperamos en PawVet.",
                                date = selectedDateText,
                                time = selectedHora,
                                targetRoute = Screen.Citas.route
                            )
                            onBack()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .shadow(12.dp, RoundedCornerShape(20.dp), ambientColor = PawVetPrimary),
                enabled = selectedMascotaId != null && selectedMotivo.isNotBlank() && selectedHora.isNotBlank(),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PawVetPrimary)
            ) {
                Text("Confirmar Cita Medica", fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedFechaMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedFechaMillis = datePickerState.selectedDateMillis ?: selectedFechaMillis
                        citaViewModel.limpiarMensajeError()
                        showDatePicker = false
                    }
                ) {
                    Text("ACEPTAR", color = PawVetPrimary, fontWeight = FontWeight.Bold)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

private fun formatDateForStorage(dateMillis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return formatter.format(Date(dateMillis))
}

private fun parseStoredDateToMillis(date: String): Long? {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        isLenient = false
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return runCatching { formatter.parse(date)?.time }.getOrNull()
}

private fun isPastSchedule(selectedDateMillis: Long, hora: String): Boolean {
    val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US).apply {
        isLenient = false
    }
    val appointment = runCatching {
        formatter.parse("${formatDateForStorage(selectedDateMillis)} $hora")
    }.getOrNull() ?: return false

    return appointment.before(Date())
}

@Composable
fun PremiumFormSection(
    title: String,
    icon: ImageVector,
    iconBg: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = iconBg
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = PawVetTextPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold,
                    color = PawVetTextPrimary,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}
