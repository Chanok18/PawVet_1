package com.example.pawvet_1.ui.screens.servicios

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pawvet_1.ui.components.PawVetBaseScreen
import com.example.pawvet_1.ui.theme.PawVetBorder
import com.example.pawvet_1.ui.theme.PawVetPrimary
import com.example.pawvet_1.ui.theme.PawVetSurface
import com.example.pawvet_1.ui.theme.PawVetTextPrimary
import com.example.pawvet_1.ui.theme.PawVetTextSecondary
import com.example.pawvet_1.ui.viewmodel.BreedsViewModel
import com.example.pawvet_1.ui.viewmodel.ImagesUiState

@Composable
fun ServiciosScreen(
    viewModel: BreedsViewModel,
    onNavigateToForm: () -> Unit,
    onBack: () -> Unit
) {
    val imagesState by viewModel.imagesUiState.collectAsState()

    PawVetBaseScreen(
        title = "Servicios Estéticos",
        onBack = onBack
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Explora estilos y acabados para elegir el look perfecto antes de reservar.",
                style = MaterialTheme.typography.bodyMedium,
                color = PawVetTextSecondary,
                modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
            )

            Box(modifier = Modifier.weight(1f)) {
                when (val state = imagesState) {
                    is ImagesUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = PawVetPrimary
                        )
                    }

                    is ImagesUiState.Success -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.images) { imageUrl ->
                                StyleCard(imageUrl)
                            }
                        }
                    }

                    is ImagesUiState.Error -> {
                        Surface(
                            modifier = Modifier.align(Alignment.Center),
                            shape = RoundedCornerShape(28.dp),
                            color = PawVetSurface,
                            border = BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.75f)),
                            shadowElevation = 10.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = state.message,
                                    color = PawVetTextPrimary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Button(
                                    onClick = { viewModel.fetchServiceImages() },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PawVetPrimary),
                                    modifier = Modifier.padding(top = 14.dp)
                                ) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = onNavigateToForm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PawVetPrimary)
            ) {
                Icon(Icons.Default.Star, contentDescription = null)
                Text(
                    text = "Reservar estética",
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun StyleCard(imageUrl: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp),
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 8.dp,
        color = PawVetSurface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Estilo de corte",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.58f)),
                            startY = 250f
                        )
                    )
            )

            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart),
                color = Color.White.copy(alpha = 0.85f),
                shape = RoundedCornerShape(999.dp)
            ) {
                Text(
                    text = "Top",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PawVetTextPrimary
                )
            }

            Text(
                text = "Estilo sugerido",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
