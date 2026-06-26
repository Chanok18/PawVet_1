package com.example.pawvet_1.ui.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawvet_1.ui.theme.*
import com.example.pawvet_1.ui.viewmodel.CitaViewModel
import com.example.pawvet_1.ui.viewmodel.MascotaViewModel
import java.text.SimpleDateFormat
import java.util.*

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

    var selectedMascotaId by remember { mutableStateOf<Int?>(null) }
    var selectedMotivo by remember { mutableStateOf("") }
    var selectedFechaMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var selectedHora by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var expandedMascotas by remember { mutableStateOf(false) }
    var expandedMotivos by remember { mutableStateOf(false) }

    val motivos = listOf("Consulta General", "Vacunación", "Desparasitación", "Control Médico", "Urgencia")
    val horarios = listOf("09:00 AM", "10:30 AM", "12:00 PM", "02:30 PM", "04:00 PM", "05:30 PM")
    val dateFormatter = SimpleDateFormat("EEEE, dd MMMM", Locale("es", "ES"))

    LaunchedEffect(citaId) {
        if (citaId != 0) citaViewModel.seleccionarCita(citaId)
    }

    LaunchedEffect(uiState.citaSeleccionada) {
        uiState.citaSeleccionada?.let {
            selectedMascotaId = it.mascotaId
            selectedMotivo = it.tipo
            selectedHora = it.hora
        }
    }

    Scaffold(
        containerColor = PawVetBackground,
        topBar = {
            TopAppBar(
                title = { Text(if (citaId == 0) "Agendar Cita" else "Editar Cita", fontWeight = FontWeight.ExtraBold, color = PawVetTextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = PawVetPrimary)
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
            // SECCIÓN 1: PACIENTE
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
                    ExposedDropdownMenu(expandedMascotas, { expandedMascotas = false }) {
                        mascotaState.listaMascotas.forEach {
                            DropdownMenuItem(
                                text = { Text(it.nombre) },
                                onClick = { selectedMascotaId = it.id; expandedMascotas = false }
                            )
                        }
                    }
                }
            }

            // SECCIÓN 2: MOTIVO
            PremiumFormSection(title = "Motivo de Consulta", icon = Icons.Default.Edit, iconBg = BlobGreen) {
                ExposedDropdownMenuBox(
                    expanded = expandedMotivos,
                    onExpandedChange = { expandedMotivos = !expandedMotivos }
                ) {
                    OutlinedTextField(
                        value = selectedMotivo.ifEmpty { "Selecciona el motivo" },
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedMotivos) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PawVetPrimary,
                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                        )
                    )
                    ExposedDropdownMenu(expandedMotivos, { expandedMotivos = false }) {
                        motivos.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = { selectedMotivo = it; expandedMotivos = false }
                            )
                        }
                    }
                }
            }

            // SECCIÓN 3: FECHA
            PremiumFormSection(title = "Fecha", icon = Icons.Default.DateRange, iconBg = BlobYellow) {
                Surface(
                    modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = dateFormatter.format(Date(selectedFechaMillis)).replaceFirstChar { it.uppercase() },
                            fontWeight = FontWeight.Bold,
                            color = PawVetTextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.Default.DateRange, null, tint = PawVetPrimary)
                    }
                }
            }

            // SECCIÓN 4: HORARIO (CHIPS)
            PremiumFormSection(title = "Horario Disponible", icon = Icons.Default.Check, iconBg = BlobCoral) {
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

            Spacer(Modifier.height(24.dp))

            // BOTÓN CONFIRMAR
            Button(
                onClick = {
                    selectedMascotaId?.let { id ->
                        val finalDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedFechaMillis))
                        citaViewModel.guardarCita(citaId, id, finalDate, selectedHora, selectedMotivo)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(62.dp).shadow(12.dp, RoundedCornerShape(20.dp), ambientColor = PawVetPrimary),
                enabled = selectedMascotaId != null && selectedMotivo.isNotBlank() && selectedHora.isNotBlank(),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PawVetPrimary)
            ) {
                Text("Confirmar Cita Médica", fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
            }
            Spacer(Modifier.height(20.dp))
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
                }) { Text("ACEPTAR", color = PawVetPrimary, fontWeight = FontWeight.Bold) }
            }
        ) { DatePicker(state = datePickerState) }
    }
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(36.dp), shape = RoundedCornerShape(10.dp), color = iconBg) {
                    Box(contentAlignment = Alignment.Center) { Icon(icon, null, modifier = Modifier.size(20.dp), tint = PawVetTextPrimary) }
                }
                Spacer(Modifier.width(12.dp))
                Text(text = title, fontWeight = FontWeight.ExtraBold, color = PawVetTextPrimary, fontSize = 16.sp)
            }
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}
