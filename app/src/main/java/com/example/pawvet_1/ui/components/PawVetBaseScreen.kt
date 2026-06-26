package com.example.pawvet_1.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pawvet_1.ui.theme.PawVetBorder
import com.example.pawvet_1.ui.theme.PawVetPrimary
import com.example.pawvet_1.ui.theme.PawVetSurface
import com.example.pawvet_1.ui.theme.PawVetTextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PawVetBaseScreen(
    title: String,
    onBack: (() -> Unit)? = null,
    onProfileClick: (() -> Unit)? = null,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = PawVetTextPrimary
                    )
                },
                navigationIcon = {
                    if (onBack != null) {
                        Surface(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(42.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = PawVetSurface,
                            border = BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.8f)),
                            shadowElevation = 6.dp
                        ) {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Atrás",
                                    tint = PawVetPrimary
                                )
                            }
                        }
                    }
                },
                actions = {
                    if (onProfileClick != null) {
                        Surface(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(42.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = PawVetSurface,
                            border = BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.8f)),
                            shadowElevation = 6.dp
                        ) {
                            IconButton(onClick = onProfileClick) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Perfil",
                                    tint = PawVetPrimary
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.size(58.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = floatingActionButton,
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}
