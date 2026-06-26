package com.example.pawvet_1.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawvet_1.ui.theme.*

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PawVetBackground)
    ) {
        // Fondo decorativo sutil
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PawVetPrimary.copy(alpha = 0.1f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Identidad
            Surface(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(30.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🐾", fontSize = 50.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Bienvenido a PawVet",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = PawVetTextPrimary,
                letterSpacing = (-0.5).sp
            )
            
            Text(
                text = "Cuidamos a los que más quieres",
                style = MaterialTheme.typography.bodyMedium,
                color = PawVetTextSecondary,
                modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
            )

            // Campos de entrada Estilizados
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = PawVetPrimary) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PawVetPrimary,
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedLabelColor = PawVetPrimary
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = PawVetPrimary) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PawVetPrimary,
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedLabelColor = PawVetPrimary
                ),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        // Usamos íconos básicos para evitar errores de material-icons-extended
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Info else Icons.Default.Lock,
                            contentDescription = null,
                            tint = PawVetTextSecondary
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(12.dp, RoundedCornerShape(18.dp), ambientColor = PawVetPrimary),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PawVetPrimary),
                enabled = email.isNotBlank() && password.isNotBlank()
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "¿Nuevo en la clínica?", color = PawVetTextSecondary)
                TextButton(onClick = onRegisterClick) {
                    Text(
                        text = "Crea una cuenta",
                        fontWeight = FontWeight.Bold,
                        color = PawVetSecondary
                    )
                }
            }
        }
    }
}
