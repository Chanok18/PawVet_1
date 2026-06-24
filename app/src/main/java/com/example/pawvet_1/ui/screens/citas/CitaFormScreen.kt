package com.example.pawvet_1.ui.screens.citas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pawvet_1.ui.viewmodel.CitaViewModel
import com.example.pawvet_1.ui.viewmodel.MascotaViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.material3.DatePickerDefaults.dateFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CitaFormScreen(
    citaId: Int,
    citaViewModel: CitaViewModel,
    mascotaViewModel: MascotaViewModel,
    onBack: () -> Unit
) {
    val uiState by citaViewModel.uiState.collectAsState()
    val mascotaState by mascotaViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        mascotaViewModel.listarMascotas()
    }

    // Estados del formulario
    var selectedMascotaId by remember { mutableStateOf<Int?>(null) }
    var selectedMotivo by remember { mutableStateOf("") }
    var selectedFechaMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var selectedHora by remember { mutableStateOf("") }

    // Control de diálogos y menús
    var showDatePicker by remember { mutableStateOf(false) }
    var expandedMascotas by remember { mutableStateOf(false) }
    var expandedMotivos by remember { mutableStateOf(false) }

    val motivos = listOf(
        "Consulta General",
        "Vacunación",
        "Desparasitación",
        "Emergencia",
        "Control Médico",
        "Otro"
    )

    val horarios = listOf(
        "09:00 AM",
        "10:00 AM",
        "11:00 AM",
        "03:00 PM",
        "04:00 PM",
        "05:00 PM"
    )

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val fechaActualSeleccionada =
        dateFormatter.format(Date(selectedFechaMillis))

    val horariosOcupados = uiState.listaCitas
        .filter { it.fecha == fechaActualSeleccionada }
        .map { it.hora }

    // Cargar datos en caso de edición
    LaunchedEffect(citaId) {
        if (citaId != 0) {
            citaViewModel.seleccionarCita(citaId)
        }
    }

    LaunchedEffect(uiState.citaSeleccionada) {
        uiState.citaSeleccionada?.let {
            selectedMascotaId = it.mascotaId
            selectedMotivo = it.tipo
            selectedHora = it.hora
            try {
                val date = dateFormatter.parse(it.fecha)
                if (date != null) selectedFechaMillis = date.time
            } catch (e: Exception) {
                // Mantener fecha actual si falla el parseo
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (citaId == 0) "Agendar Cita Médica" else "Editar Cita Médica") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Selección de Mascota
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Selecciona el paciente", style = MaterialTheme.typography.titleSmall)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    ExposedDropdownMenuBox(
                        expanded = expandedMascotas,
                        onExpandedChange = { expandedMascotas = !expandedMascotas }
                    ) {
                        val mascotaSeleccionada = mascotaState.listaMascotas.find { it.id == selectedMascotaId }
                        OutlinedTextField(
                            value = mascotaSeleccionada?.nombre ?: "Elige una mascota",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Paciente") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMascotas) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        )
                        ExposedDropdownMenu(
                            expanded = expandedMascotas,
                            onDismissRequest = { expandedMascotas = false }
                        ) {
                            if (mascotaState.listaMascotas.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No hay mascotas registradas") },
                                    onClick = { expandedMascotas = false }
                                )
                            } else {
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
                }
            }

            // 2. Motivo de la Cita
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Motivo de consulta", style = MaterialTheme.typography.titleSmall)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = expandedMotivos,
                        onExpandedChange = { expandedMotivos = !expandedMotivos }
                    ) {
                        OutlinedTextField(
                            value = selectedMotivo,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Servicio") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMotivos) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
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
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 3. Fecha y Hora
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Programación", style = MaterialTheme.typography.titleSmall)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Fecha
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Día seleccionado:", style = MaterialTheme.typography.labelMedium)
                            Text(dateFormatter.format(Date(selectedFechaMillis)), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        }
                        FilledTonalButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Elegir")
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    // Hora
                    Text("Horarios disponibles:", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(bottom = 8.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        horarios.forEach { hora ->
                            val ocupado = horariosOcupados.contains(hora)
                            FilterChip(
                                selected = selectedHora == hora,
                                onClick = {
                                    if (!ocupado) {
                                        selectedHora = hora
                                    }
                                },
                                enabled = !ocupado,
                                label = {
                                    Text(
                                        if (ocupado)
                                            "$hora (Ocupado)"
                                        else
                                            hora
                                    )
                                },
                                leadingIcon = if (selectedHora == hora) {
                                    {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                } else null
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Acción
            val context = LocalContext.current
            Button(
                onClick = {
                    selectedMascotaId?.let { idMascota ->

                        val ahora = Calendar.getInstance()

                        val calendarioCita = Calendar.getInstance()
                        calendarioCita.timeInMillis = selectedFechaMillis

                        val hora24 = when (selectedHora) {
                            "09:00 AM" -> 9
                            "10:00 AM" -> 10
                            "11:00 AM" -> 11
                            "03:00 PM" -> 15
                            "04:00 PM" -> 16
                            "05:00 PM" -> 17
                            else -> 0
                        }

                        calendarioCita.set(Calendar.HOUR_OF_DAY, hora24)
                        calendarioCita.set(Calendar.MINUTE, 0)

                        if (calendarioCita.before(ahora)) {
                            Toast.makeText(
                                context,
                                "No puedes registrar citas en fechas u horas pasadas",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        citaViewModel.guardarCita(
                            id = citaId,
                            mascotaId = idMascota,
                            fecha = dateFormatter.format(Date(selectedFechaMillis)),
                            hora = selectedHora,
                            tipo = selectedMotivo
                        )

                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = selectedMascotaId != null && selectedMotivo.isNotBlank() && selectedHora.isNotBlank(),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("Confirmar Cita Médica", style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    // Diálogo del DatePicker
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedFechaMillis
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val zonaLocal = TimeZone.getDefault()
                        selectedFechaMillis = it + zonaLocal.getOffset(it)
                    }
                    showDatePicker = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
