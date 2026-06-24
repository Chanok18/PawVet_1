package com.example.pawvet_1.ui.screens.servicios

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
import com.example.pawvet_1.ui.viewmodel.ServicioViewModel
import com.example.pawvet_1.ui.viewmodel.MascotaViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ServicioFormScreen(
    viewModel: ServicioViewModel,
    mascotaViewModel: MascotaViewModel,
    onBack: () -> Unit
) {
    val mascotaState by mascotaViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        mascotaViewModel.listarMascotas()
    }

    // Estados del formulario
    var selectedMascotaId by remember { mutableStateOf<Int?>(null) }
    var selectedTipo by remember { mutableStateOf("Baño y Corte") }
    var selectedFechaMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var selectedHora by remember { mutableStateOf("09:00 AM") }

    // Control de diálogos y menús
    var showDatePicker by remember { mutableStateOf(false) }
    var expandedMascotas by remember { mutableStateOf(false) }
    var expandedTipos by remember { mutableStateOf(false) }

    val tiposServicio = listOf("Baño y Corte", "Limpieza Dental", "Corte de Uñas", "Baño Medicado", "Spa Completo")
    val horarios = listOf(
        "09:00 AM",
        "10:00 AM",
        "11:00 AM",
        "03:00 PM",
        "04:00 PM",
        "05:00 PM"
    )
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar Estética") },
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
            // 1. Card de Paciente
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Paciente", style = MaterialTheme.typography.titleSmall)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    ExposedDropdownMenuBox(
                        expanded = expandedMascotas,
                        onExpandedChange = { expandedMascotas = !expandedMascotas }
                    ) {
                        val mascotaSeleccionada = mascotaState.listaMascotas.find { it.id == selectedMascotaId }
                        OutlinedTextField(
                            value = mascotaSeleccionada?.nombre ?: "Seleccionar Mascota",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Mascota") },
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

            // 2. Card de Tipo de Servicio
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Servicio solicitado", style = MaterialTheme.typography.titleSmall)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = expandedTipos,
                        onExpandedChange = { expandedTipos = !expandedTipos }
                    ) {
                        OutlinedTextField(
                            value = selectedTipo,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de Servicio") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipos) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTipos,
                            onDismissRequest = { expandedTipos = false }
                        ) {
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
            }

            // 3. Card de Fecha
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Fecha sugerida", style = MaterialTheme.typography.titleSmall)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dateFormatter.format(Date(selectedFechaMillis)),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Button(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Cambiar")
                        }
                    }
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        "Horario",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        horarios.forEach { hora ->

                            FilterChip(
                                selected = selectedHora == hora,
                                onClick = {
                                    selectedHora = hora
                                },
                                label = {
                                    Text(hora)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Confirmación
            Button(
                onClick = {
                    selectedMascotaId?.let { idMascota ->
                        viewModel.guardarServicio(
                            mascotaId = idMascota,
                            tipo = selectedTipo,
                            fecha = dateFormatter.format(Date(selectedFechaMillis)),
                            hora = selectedHora
                        )
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled =
                    selectedMascotaId != null &&
                            selectedTipo.isNotBlank() &&
                            selectedHora.isNotBlank(),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("Confirmar Cita Estética", style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    // Diálogo del DatePicker
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedFechaMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectedFechaMillis =
                            it + TimeZone.getDefault().getOffset(it)
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
