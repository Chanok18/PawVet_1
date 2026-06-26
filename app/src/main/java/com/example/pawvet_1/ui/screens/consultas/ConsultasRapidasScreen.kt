package com.example.pawvet_1.ui.screens.consultas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawvet_1.ui.components.PawVetBaseScreen
import com.example.pawvet_1.ui.theme.PawVetAccent
import com.example.pawvet_1.ui.theme.PawVetBorder
import com.example.pawvet_1.ui.theme.PawVetPrimary
import com.example.pawvet_1.ui.theme.PawVetSurface
import com.example.pawvet_1.ui.theme.PawVetTextPrimary
import com.example.pawvet_1.ui.theme.PawVetTextSecondary

@Composable
fun ConsultasRapidasScreen(
    onBack: () -> Unit
) {
    PawVetBaseScreen(
        title = "Consulta IA",
        onBack = onBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(30.dp),
                color = PawVetPrimary,
                shadowElevation = 14.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF107E78), PawVetPrimary)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = "PawBot activo",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Haz preguntas sobre síntomas, cuidados, vacunas o recomendaciones rápidas.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 18.dp),
                contentPadding = PaddingValues(vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    ChatBubble(
                        message = "Hola, soy tu asistente de PawVet. Puedo ayudarte con dudas rápidas sobre cuidados, síntomas comunes o preparación antes de una cita.",
                        isUser = false
                    )
                }
                item {
                    ChatBubble(
                        message = "¿Qué debo revisar antes de llevar a mi mascota a control?",
                        isUser = true
                    )
                }
                item {
                    ChatBubble(
                        message = "Lleva su carnet, anota síntomas recientes, cambios de apetito, peso o energía, y si toma medicación. Si quieres, luego te preparo un checklist completo.",
                        isUser = false
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                shape = RoundedCornerShape(24.dp),
                color = PawVetSurface,
                border = BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.8f)),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Escribe tu consulta...",
                        color = PawVetTextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        shape = CircleShape,
                        color = PawVetPrimary
                    ) {
                        Box(
                            modifier = Modifier.padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: String,
    isUser: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = if (isUser) PawVetPrimary else PawVetSurface,
            border = if (isUser) null else BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.8f)),
            shape = RoundedCornerShape(
                topStart = if (isUser) 24.dp else 8.dp,
                topEnd = if (isUser) 8.dp else 24.dp,
                bottomStart = 24.dp,
                bottomEnd = 24.dp
            ),
            shadowElevation = if (isUser) 0.dp else 6.dp
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                color = if (isUser) Color.White else PawVetTextPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
