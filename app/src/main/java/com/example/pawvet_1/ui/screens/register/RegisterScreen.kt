package com.example.pawvet_1.ui.screens.register

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawvet_1.ui.components.PawVetBaseScreen
import com.example.pawvet_1.ui.theme.*

@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PawVetBackground)
    ) {
        // Fondo decorativo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PawVetSecondary.copy(alpha = 0.1f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🐾", fontSize = 40.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Crea tu cuenta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = PawVetTextPrimary
            )

            Text(
                text = "Únete a la mejor comunidad veterinaria",
                style = MaterialTheme.typography.bodyMedium,
                color = PawVetTextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            // Formulario
            PremiumTextField(
                value = name,
                onValueChange = { name = it },
                label = "Nombre Completo",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(16.dp))

            PremiumTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo Electrónico",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            PremiumTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                icon = Icons.Default.Lock,
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PremiumTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirmar Contraseña",
                icon = Icons.Default.Lock,
                isPassword = true,
                passwordVisible = passwordVisible
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onBackToLogin, // Asumiendo que el registro exitoso vuelve al login o navega
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(8.dp, RoundedCornerShape(18.dp), ambientColor = PawVetSecondary),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PawVetSecondary),
                enabled = name.isNotBlank() && email.isNotBlank() && password.isNotBlank() && password == confirmPassword
            ) {
                Text(
                    text = "Registrarse",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "¿Ya tienes una cuenta?", color = PawVetTextSecondary)
                TextButton(onClick = onBackToLogin) {
                    Text(
                        text = "Inicia Sesión",
                        fontWeight = FontWeight.Bold,
                        color = PawVetPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = PawVetPrimary) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PawVetPrimary,
            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
            focusedLabelColor = PawVetPrimary
        ),
        trailingIcon = if (isPassword && onPasswordToggle != null) {
            {
                IconButton(onClick = onPasswordToggle) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Info else Icons.Default.Lock,
                        contentDescription = null,
                        tint = PawVetTextSecondary
                    )
                }
            }
        } else null
    )
}
