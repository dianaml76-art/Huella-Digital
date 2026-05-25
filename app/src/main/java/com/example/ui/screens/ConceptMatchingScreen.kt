package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.HuellaViewModel
import kotlinx.coroutines.delay

@Composable
fun ConceptMatchingScreen(
    viewModel: HuellaViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    val shuffledConcepts by viewModel.shuffledConcepts.collectAsState()
    val shuffledDefinitions by viewModel.shuffledDefinitions.collectAsState()
    val selectedConcept by viewModel.selectedConcept.collectAsState()
    val selectedDefinition by viewModel.selectedDefinition.collectAsState()
    val matchedIds by viewModel.matchedIds.collectAsState()
    val tempCorrectMatchId by viewModel.tempCorrectMatchId.collectAsState()
    val gameFinished by viewModel.gameFinished.collectAsState()
    val score by viewModel.matchingScore.collectAsState()

    // Trigger haptic feedback and show toast when a match is correctly made
    LaunchedEffect(matchedIds) {
        if (matchedIds.isNotEmpty()) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .windowInsetsPadding(WindowInsets.safeDrawing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.testTag("back_from_match_button")
            ) {
                Icon(
                    imageVector = Icons.Default.NavigateBefore,
                    contentDescription = "Regresar",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = "Desafío de Ciberseguridad",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (gameFinished) {
            // Success view
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "¡Desafío Completado!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Aciertos logrados:",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )

                        Text(
                            text = "$score / 10",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = if (score >= 8) {
                                "¡Impresionante! Tienes un conocimiento excepcional sobre autoprotección digital y ciberhigiene."
                            } else {
                                "¡Buen intento! Continúa repasando los candados y definiciones para proteger tu huella digital."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.resetMatchingGame() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_play_match_again"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Jugar otra vez (Nuevos conceptos)")
                        }
                    }
                }
            }
        } else {
            // Columns of matching items
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Empareja Conceptos",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Selecciona un concepto de la izquierda, luego asocialo con su definición de la derecha. ¡Empareja los 10 con cuidado!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    
                    Text(
                        text = "Aciertos: $score / 10",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // COLUMN A: CONCEPTS
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "CONCEPTOS",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                            )

                            shuffledConcepts.forEach { concept ->
                                val isMatched = concept.id in matchedIds
                                val isSelected = selectedConcept == concept
                                val isJustMatched = tempCorrectMatchId == concept.id

                                val cardBgColor = when {
                                    isJustMatched -> Color(0xFFE8F5E9) // Verde fuerte transitorio
                                    isMatched -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    else -> MaterialTheme.colorScheme.surface
                                }
                                val cardBorderColor = when {
                                    isJustMatched -> Color(0xFF4CAF50)
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    else -> Color.Transparent
                                }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("match_concept_${concept.id}")
                                        .clickable(enabled = !isMatched) { viewModel.selectConcept(concept) },
                                    colors = CardDefaults.cardColors(containerColor = cardBgColor),
                                    border = if (isSelected || isJustMatched) BorderStroke(2.dp, cardBorderColor) else null,
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = if (isMatched) 0.dp else 2.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Column {
                                            Text(
                                                text = concept.concepto,
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isMatched) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) else MaterialTheme.colorScheme.primary
                                            )
                                        }

                                        // Success green check animation overlay
                                        if (isMatched) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Correcto",
                                                tint = MaterialTheme.colorScheme.tertiary,
                                                modifier = Modifier.align(Alignment.CenterEnd).size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // COLUMN B: DEFINITIONS
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "DEFINICIONES",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                            )

                            shuffledDefinitions.forEach { def ->
                                val isMatched = def.id in matchedIds
                                val isSelected = selectedDefinition == def
                                val isJustMatched = tempCorrectMatchId == def.id

                                val cardBgColor = when {
                                    isJustMatched -> Color(0xFFE8F5E9)
                                    isMatched -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    else -> MaterialTheme.colorScheme.surface
                                }
                                val cardBorderColor = when {
                                    isJustMatched -> Color(0xFF4CAF50)
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    else -> Color.Transparent
                                }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("match_def_${def.id}")
                                        .clickable(enabled = !isMatched) { viewModel.selectDefinition(def) },
                                    colors = CardDefaults.cardColors(containerColor = cardBgColor),
                                    border = if (isSelected || isJustMatched) BorderStroke(2.dp, cardBorderColor) else null,
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = if (isMatched) 0.dp else 2.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Column {
                                            Text(
                                                text = def.definicion,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = if (isMatched) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        if (isMatched) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Correcto",
                                                tint = MaterialTheme.colorScheme.tertiary,
                                                modifier = Modifier.align(Alignment.CenterEnd).size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.resetMatchingGame() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                            .testTag("reset_matching_game_button")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reiniciar partida")
                    }
                }
            }
        }
    }
}
