package com.example.pawvet_1.ui.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawvet_1.R
import com.example.pawvet_1.ui.theme.PawVetAccent
import com.example.pawvet_1.ui.theme.PawVetBackground
import com.example.pawvet_1.ui.theme.PawVetBodyFont
import com.example.pawvet_1.ui.theme.PawVetBorder
import com.example.pawvet_1.ui.theme.PawVetCoralGlow
import com.example.pawvet_1.ui.theme.PawVetPrimary
import com.example.pawvet_1.ui.theme.PawVetSuccess
import com.example.pawvet_1.ui.theme.PawVetSuccessSoft
import com.example.pawvet_1.ui.theme.PawVetSurface
import com.example.pawvet_1.ui.theme.PawVetTextPrimary
import com.example.pawvet_1.ui.theme.PawVetTextSecondary
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

private val HomeDisplayFont = PawVetBodyFont

private data class ServiceItem(
    val title: String,
    val description: String,
    val cta: String,
    val tag: String,
    @DrawableRes val imageRes: Int,
    val tint: List<Color>,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(
    userName: String,
    onCitasClick: () -> Unit,
    onServiciosClick: () -> Unit,
    onConsultasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    val services = listOf(
        ServiceItem(
            title = "Citas Médicas",
            description = "Consultas, vacunas y chequeos con veterinarios certificados.",
            cta = "Agendar cita",
            tag = "Salud",
            imageRes = R.drawable.medical,
            tint = listOf(Color(0xFF244D53), Color(0x8A244D53), Color.Transparent),
            onClick = onCitasClick
        ),
        ServiceItem(
            title = "Servicios Estéticos",
            description = "Baño, peluquería y spa para que luzca radiante y feliz.",
            cta = "Reservar spa",
            tag = "Bienestar",
            imageRes = R.drawable.grooming,
            tint = listOf(Color(0xFF71503D), Color(0x8F71503D), Color.Transparent),
            onClick = onServiciosClick
        ),
        ServiceItem(
            title = "Consulta IA",
            description = "Respuestas inmediatas sobre síntomas y cuidados, 24/7.",
            cta = "Preguntar ahora",
            tag = "Inteligencia",
            imageRes = R.drawable.ai_consult,
            tint = listOf(Color(0xFF143E48), Color(0x99143E48), Color.Transparent),
            onClick = onConsultasClick
        )
    )

    Scaffold(
        containerColor = PawVetBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp)
                .padding(bottom = 108.dp)
        ) {
            AnimatedEntrance(delayMillis = 0) {
                WelcomeHeader(
                    userName = userName,
                    onPerfilClick = onPerfilClick
                )
            }

            Spacer(Modifier.height(28.dp))

            AnimatedEntrance(delayMillis = 80) {
                HeroHeadline()
            }

            Spacer(Modifier.height(24.dp))

            AnimatedEntrance(delayMillis = 130) {
                ServicesSection(services = services)
            }

            Spacer(Modifier.height(22.dp))

            AnimatedEntrance(delayMillis = 160) {
                EmergencySection()
            }
        }
    }
}

@Composable
private fun HeroHeadline() {
    Text(
        text = "El cuidado que tu mascota merece, en un solo lugar.",
        color = PawVetTextPrimary,
        fontFamily = HomeDisplayFont,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 37.sp,
        lineHeight = 40.sp,
        letterSpacing = (-1.0).sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun WelcomeHeader(
    userName: String,
    onPerfilClick: () -> Unit
) {
    val displayName = userName.substringBefore(" ").ifBlank { "junior" }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White.copy(alpha = 0.9f),
        border = BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.8f)),
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFEAF9F5), Color(0xFFD6F1EB))
                            )
                        )
                        .border(1.dp, PawVetBorder.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
                        .clickable { onPerfilClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil de usuario",
                        tint = PawVetPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp, y = 2.dp)
                        .size(13.dp)
                        .clip(CircleShape)
                        .background(PawVetSuccess)
                        .border(2.dp, Color.White, CircleShape)
                )
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = PawVetPrimary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "PawVet",
                            color = PawVetPrimary,
                            fontFamily = PawVetBodyFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 0.2.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = displayName,
                        color = PawVetTextPrimary,
                        fontFamily = HomeDisplayFont,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 31.sp,
                        lineHeight = 32.sp,
                        letterSpacing = (-0.8).sp
                    )
                }
            }

            Spacer(Modifier.width(56.dp))
        }
    }
}

@Composable
private fun ServicesSection(services: List<ServiceItem>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Nuestros servicios",
                fontFamily = HomeDisplayFont,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                lineHeight = 30.sp,
                letterSpacing = (-0.5).sp,
                color = PawVetTextPrimary
            )
        }

        Spacer(Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            services.forEach { service ->
                ServiceCard(service = service)
            }
        }
    }
}

@Composable
private fun ServiceCard(service: ServiceItem) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clickable { service.onClick() },
        shape = RoundedCornerShape(42.dp),
        shadowElevation = 12.dp,
        color = PawVetSurface
    ) {
        Box {
            Image(
                painter = painterResource(service.imageRes),
                contentDescription = service.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                service.tint[1].copy(alpha = 0.20f),
                                service.tint[0].copy(alpha = 0.78f)
                            ),
                            startY = 50f,
                            endY = 760f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color(0x4D5E655D),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.10f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Spacer(Modifier.width(7.dp))
                        Text(
                            text = service.tag,
                            color = Color.White,
                            fontFamily = PawVetBodyFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                Column(modifier = Modifier.fillMaxWidth(0.82f)) {
                    Text(
                        text = service.title,
                        color = Color.White,
                        fontFamily = HomeDisplayFont,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = if (service.title.length > 18) 23.sp else 29.sp,
                        lineHeight = if (service.title.length > 18) 27.sp else 31.sp,
                        letterSpacing = (-0.8).sp
                    )
                    Text(
                        text = service.description,
                        color = Color.White.copy(alpha = 0.82f),
                        fontFamily = PawVetBodyFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        lineHeight = 24.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(Modifier.height(20.dp))
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = PawVetSurface
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = service.cta,
                                color = PawVetTextPrimary,
                                fontFamily = HomeDisplayFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                letterSpacing = (-0.2).sp
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = PawVetTextPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.weight(0.55f))
            }
        }
    }
}

@Composable
private fun EmergencySection() {
    val pulseTransition = rememberInfiniteTransition(label = "emergency")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emergencyScale"
    )
    val pulseAlpha by pulseTransition.animateFloat(
        initialValue = 0.38f,
        targetValue = 0.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emergencyAlpha"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        color = PawVetSurface,
        border = BorderStroke(1.dp, PawVetAccent.copy(alpha = 0.22f)),
        shadowElevation = 16.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(PawVetCoralGlow.copy(alpha = 0.30f), Color.Transparent),
                        center = Offset(80f, 50f),
                        radius = 220f
                    )
                )
                .padding(18.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .scale(pulseScale)
                                .clip(RoundedCornerShape(18.dp))
                                .background(PawVetAccent.copy(alpha = pulseAlpha))
                        )
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(PawVetAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Emergencias 24h",
                                color = PawVetTextPrimary,
                                fontFamily = HomeDisplayFont,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                letterSpacing = (-0.5).sp
                            )
                            Spacer(Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = PawVetSuccessSoft
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(PawVetSuccess)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Nuestro equipo veterinario está disponible a toda hora. Si es urgente, te conectamos de inmediato.",
                            color = PawVetTextSecondary,
                            fontFamily = PawVetBodyFont,
                            fontSize = 15.sp,
                            lineHeight = 23.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedEntrance(
    delayMillis: Int,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delayMillis.toLong())
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "alpha"
    )
    val translateY by animateFloatAsState(
        targetValue = if (visible) 0f else 26f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "translateY"
    )

    Box(
        modifier = Modifier.graphicsLayer {
            this.alpha = alpha
            translationY = translateY
        }
    ) {
        content()
    }
}
