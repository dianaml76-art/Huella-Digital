package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.HuellaViewModel

@Composable
fun PasswordGeneratorScreen(
    viewModel: HuellaViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    val length by viewModel.passwordLength.collectAsState()
    val includeUpper by viewModel.includeUpper.collectAsState()
    val includeNumbers by viewModel.includeNumbers.collectAsState()
    val includeSymbols by viewModel.includeSymbols.collectAsState()
    val generatedPassword by viewModel.generatedPassword.collectAsState()
    val isGeneratingLoading by viewModel.isGeneratingLoading.collectAsState()

    val strengthValue = viewModel.getPasswordStrength(generatedPassword)

    // Automatically generate a default password once on screen appearance so it's populated elegantly.
    LaunchedEffect(Unit) {
        if (generatedPassword.isEmpty()) {
            viewModel.generateSecurePassword()
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
                modifier = Modifier.testTag("back_from_password_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Generador de Claves",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Cerradura Digital",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )

            Text(
                text = "Crea una contraseña segura combinando números, letras y símbolos que sea indescifrable ante ataques.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Password output box (Monospaced display for codes/credentials as requested)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp),
                border = BoxBorder()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = generatedPassword.ifEmpty { "Generando..." },
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 21.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                letterSpacing = 2.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.testTag("txt_generated_password")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Strength visual bar indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 100.dp, height = 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(strengthValue.colorHex))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = strengthValue.label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(strengthValue.colorHex)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Clipboard and Share buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.copyToClipboard(context, generatedPassword) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_copy_password"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Copiar", fontSize = 14.sp)
                }

                OutlinedButton(
                    onClick = { viewModel.sharePassword(context, generatedPassword) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_share_password"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Compartir", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sliders and options
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Title Length
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Longitud de clave:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$length caracteres",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Slider(
                        value = length.toFloat(),
                        onValueChange = { viewModel.updateLength(it.toInt()) },
                        valueRange = 8f..20f,
                        steps = 11,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("slider_length")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Opciones avanzadas:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Checkbox Mayúsculas
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleUpper() }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = includeUpper,
                            onCheckedChange = { viewModel.toggleUpper() },
                            modifier = Modifier.testTag("chk_uppercase")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Incluir Mayúsculas (A-Z)", style = MaterialTheme.typography.bodyMedium)
                    }

                    // Checkbox Números
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleNumbers() }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = includeNumbers,
                            onCheckedChange = { viewModel.toggleNumbers() },
                            modifier = Modifier.testTag("chk_numbers")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Incluir Números (0-9)", style = MaterialTheme.typography.bodyMedium)
                    }

                    // Checkbox Símbolos
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleSymbols() }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = includeSymbols,
                            onCheckedChange = { viewModel.toggleSymbols() },
                            modifier = Modifier.testTag("chk_symbols")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Incluir Símbolos (@, $, !, %)", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action: Generate password button
            Button(
                onClick = { viewModel.generateSecurePassword() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_trigger_generation"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp),
                enabled = !isGeneratingLoading && (includeUpper || includeNumbers || includeSymbols)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Generar contraseña",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BoxBorder(): BorderStroke {
    return BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
}
