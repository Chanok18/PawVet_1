package com.example.pawvet_1.ui.screens.mascotas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawvet_1.ui.components.PawVetBaseScreen
import com.example.pawvet_1.ui.viewmodel.MascotaViewModel

/**
 * [VISTA - FORMULARIO]
 * - COMPOSABLES: Card, Column, OutlinedTextField.
 * - ORGANIZACIÓN: VerticalScroll para que no se corte en pantallas chicas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotaFormScreen(
    mascotaId: Int,
    viewModel: MascotaViewModel,
    onBack: () -> Unit
) {
    // - OBSERVACIÓN: Escuchamos al ViewModel. Si el estado cambia, la UI se RECOMPONE.
    val uiState by viewModel.uiState.collectAsState()
    
    // - ESTADO LOCAL: Variables temporales para lo que el usuario escribe.
    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Perro") }
    var raza by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }

    val tiposMascota = listOf("Perro", "Gato", "Hamster", "Conejo", "Ave", "Otro")
    val razasComunes = listOf("Labrador", "Pastor Alemán", "Persa", "Siamés", "Pug", "Chihuahua", "Bulldog", "Mestizo", "Otro")

    var expandedTipo by remember { mutableStateOf(false) }
    var expandedRaza by remember { mutableStateOf(false) }

    LaunchedEffect(mascotaId) {
        if (mascotaId > 0) {
            viewModel.seleccionarMascota(mascotaId)
        } else {
            viewModel.resetSeleccion()
        }
    }

    LaunchedEffect(uiState.mascotaSeleccionada) {
        uiState.mascotaSeleccionada?.let {
            nombre = it.nombre
            tipo = it.tipo
            raza = it.raza
            edad = it.edad.toString()
            peso = it.peso.toString()
        }
    }

    PawVetBaseScreen(
        title = if (mascotaId > 0) "Editar Mascota" else "Nueva Mascota",
        onBack = onBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre de la mascota") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = { Icon(Icons.Default.Face, contentDescription = null) }
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedTipo,
                        onExpandedChange = { expandedTipo = !expandedTipo }
                    ) {
                        OutlinedTextField(
                            value = tipo,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("¿Qué es?") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo) },
                            leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTipo,
                            onDismissRequest = { expandedTipo = false }
                        ) {
                            tiposMascota.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        tipo = item
                                        expandedTipo = false
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedRaza,
                        onExpandedChange = { expandedRaza = !expandedRaza }
                    ) {
                        OutlinedTextField(
                            value = raza,
                            onValueChange = { raza = it },
                            label = { Text("Raza") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRaza) },
                            leadingIcon = { Icon(Icons.Default.Star, contentDescription = null) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedRaza,
                            onDismissRequest = { expandedRaza = false }
                        ) {
                            razasComunes.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        raza = item
                                        expandedRaza = false
                                    }
                                )
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = edad,
                            onValueChange = { if (it.all { char -> char.isDigit() }) edad = it },
                            label = { Text("Edad") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            suffix = { Text("años") }
                        )
                        OutlinedTextField(
                            value = peso,
                            onValueChange = { peso = it },
                            label = { Text("Peso") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            suffix = { Text("kg") }
                        )
                    }
                }
            }
            
            // - ACCIÓN: Al pulsar, enviamos los datos al ViewModel.
            Button(
                onClick = {
                    viewModel.guardarMascota(
                        id = mascotaId,
                        nombre = nombre,
                        tipo = tipo,
                        raza = raza,
                        edad = edad.toIntOrNull() ?: 0,
                        peso = peso.toDoubleOrNull() ?: 0.0
                    )
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = nombre.isNotBlank() && raza.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (mascotaId > 0) "Guardar Cambios" else "Registrar Mascota",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
