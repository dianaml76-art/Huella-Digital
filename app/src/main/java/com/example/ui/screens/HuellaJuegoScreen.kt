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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.HuellaViewModel

@Composable
fun HuellaJuegoScreen(
    viewModel: HuellaViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val exposedServices by viewModel.exposedServices.collectAsState()
    val exposedDataList by viewModel.exposedDataList.collectAsState()
    val showSummary by viewModel.showHuellaSummary.collectAsState()
    
    // Track currently selected/last clicked service to show detailed bubble expansion
    var selectedServiceForBubble by remember { mutableStateOf<String?>(null) }

    // Map service names to distinct colors and icons
    val serviceMeta = mapOf(
        "Instagram" to Pair(Color(0xFFE1306C), Icons.Default.CameraAlt),
        "TikTok" to Pair(Color(0xFF000000), Icons.Default.MusicNote),
        "WhatsApp" to Pair(Color(0xFF25D366), Icons.Default.Phone),
        "Facebook" to Pair(Color(0xFF1877F2), Icons.Default.People),
        "Twitter/X" to Pair(Color(0xFF1DA1F2), Icons.Default.RateReview),
        "YouTube" to Pair(Color(0xFFFF0000), Icons.Default.PlayArrow),
        "Snapchat" to Pair(Color(0xFFFFFC00), Icons.Default.Face),
        "Spotify" to Pair(Color(0xFF1DB954), Icons.Default.Audiotrack),
        "Amazon" to Pair(Color(0xFFFF9900), Icons.Default.ShoppingCart),
        "Google" to Pair(Color(0xFF4285F4), Icons.Default.Search),
        "Roblox" to Pair(Color(0xFFE02B2B), Icons.Default.Gamepad),
        "Telegram" to Pair(Color(0xFF0088CC), Icons.Default.Send),
        "Servicio de Correo" to Pair(Color(0xFF6A1B9A), Icons.Default.Email),
        "Plataformas de Streaming" to Pair(Color(0xFFE53935), Icons.Default.Tv)
    )

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
                modifier = Modifier.testTag("back_from_huella_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Mapa de Conexiones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "¿Qué información dejas en internet?",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = "Muchos de los servicios que usas a diario recogen gran cantidad de datos personales. ¡Toca cada logo para develar qué capturan y ver cómo se acumula tu rastro!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Grid of service circles
            item {
                Text(
                    text = "Redes y servicios digitales (Toca cada una):",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Custom Flex Row Grid representation of the 10 services
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    maxItemsInEachRow = 5
                ) {
                    viewModel.listaServicios.forEach { servicio ->
                        val meta = serviceMeta[servicio.nombre] ?: Pair(Color.Gray, Icons.Default.Info)
                        val isExposed = servicio.nombre in exposedServices
                        
                        // Bubble scale animation
                        val scale by animateFloatAsState(
                            targetValue = if (selectedServiceForBubble == servicio.nombre) 1.2f else 1.0f,
                            animationSpec = tween(durationMillis = 200)
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(8.dp)
                                .scale(scale)
                                .clickable {
                                    viewModel.touchService(servicio.nombre)
                                    selectedServiceForBubble = servicio.nombre
                                }
                                .testTag("service_icon_${servicio.nombre}")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(if (isExposed) meta.first else meta.first.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = meta.second,
                                    contentDescription = servicio.nombre,
                                    tint = if (isExposed) Color.White else meta.first,
                                    modifier = Modifier.size(30.dp)
                                )
                                if (isExposed) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .align(Alignment.TopEnd)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.tertiary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Expuesto",
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = servicio.nombre,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isExposed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            // Interactive Information Bubble container
            item {
                selectedServiceForBubble?.let { activeName ->
                    val activeServ = viewModel.listaServicios.first { it.nombre == activeName }
                    val meta = serviceMeta[activeName] ?: Pair(Color.Gray, Icons.Default.Info)

                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + scaleIn(initialScale = 0.8f),
                        exit = fadeOut() + scaleOut()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = meta.first.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = meta.second,
                                        contentDescription = null,
                                        tint = meta.first,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = activeName,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = meta.first
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Datos recolectados:",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = activeServ.datosExpuestos,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Consejo de protección:",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                Text(
                                    text = activeServ.consejo,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Exposing Progress indicators & gravity thermometer
            val totalServicios = viewModel.listaServicios.size
            item {
                RiskThermometer(exposedCount = exposedServices.size, totalCount = totalServicios)
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Servicios analizados: ${exposedServices.size}/$totalServicios",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    if (exposedServices.size in 1..(totalServicios - 1) && !showSummary) {
                        Button(
                            onClick = { viewModel.showFootprintSummary() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            modifier = Modifier.testTag("show_summary_mid_game_button")
                        ) {
                            Text("Ver mi huella")
                        }
                    }
                }
                
                LinearProgressIndicator(
                    progress = { exposedServices.size.toFloat() / totalServicios.toFloat() },
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)).height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            }

            // Accumulation list footer ("Listado de exposición")
            if (exposedDataList.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Rastros de exposición acumulados:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                items(exposedDataList) { dataStr ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Dato expuesto",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = dataStr,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Summary Overlay and Actionable Clean Advice Box at completion
            if (showSummary) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                        ),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.tertiary),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Consejos para REDUCIR tu Huella Digital",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Has analizado cómo se entrelazan tus datos por redes públicas. Para borrar, mitigar o evitar el crecimiento de tu huella digital:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            val consejos = listOf(
                                "Revisa la configuración de privacidad de cada red social y ponla en modo privado para evitar miradas indiscretas.",
                                "No publiques tu ubicación en tiempo real — configura que solo tus amigos de entera confianza la vean.",
                                "Cierra cuentas antiguas que no uses en foros o juegos viejos; siguen conservando y exponiendo tus metadatos.",
                                "Evita ingresar con tu cuenta de un solo clic de Google o Facebook en sitios poco fiables que recogen hábitos.",
                                "Búscate en internet escribiendo tu nombre completo cada cierto tiempo para regular qué se sabe sobre ti online."
                            )

                            consejos.forEachIndexed { i, c ->
                                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text(
                                        text = "${i + 1}. ",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = c,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Controls
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            viewModel.resetHuellaJuego()
                            selectedServiceForBubble = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.testTag("reset_huella_game_button")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Reiniciar juego")
                    }
                }
            }
        }
    }
}

// Minimal implementation of a Flow Layout to represent Grid wrapping in older SDKs or standard Compose
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        maxItemsInEachRow = maxItemsInEachRow
    ) {
        content()
    }
}

@Composable
fun RiskThermometer(exposedCount: Int, totalCount: Int) {
    val progress = if (totalCount > 0) exposedCount.toFloat() / totalCount.toFloat() else 0f
    
    // Determine color, rating level, emoji and detailed advice based on number of active services
    val (color, label, emoji, message) = when {
        exposedCount == 0 -> Quadruple(
            Color(0xFF4CAF50), 
            "SEGURO", 
            "🛡️", 
            "Sin rastros expuestos de momento. ¡Excelente, sigue así!"
        )
        exposedCount <= 3 -> Quadruple(
            Color(0xFF81C784), 
            "RIESGO BAJO", 
            "🌱", 
            "Rastro mínimo inicial en internet. Fácil de organizar y mantener a salvo."
        )
        exposedCount <= 7 -> Quadruple(
            Color(0xFFFFD54F), 
            "RIESGO MODERADO", 
            "⚠️", 
            "Tienes una presencia digital habitual. Revisa bien qué fotos compartes."
        )
        exposedCount <= 11 -> Quadruple(
            Color(0xFFFF9800), 
            "RIESGO ALTO", 
            "🔥", 
            "Exposición alta. Muchos datos entrelazados como geolocalización, chats o gustos."
        )
        else -> Quadruple(
            Color(0xFFE53935), 
            "RIESGO CRÍTICO", 
            "🚨", 
            "¡Alerta máxima! Tu huella digital es gigante y dejas demasiada información personal."
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = emoji,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Termómetro de Gravedad",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // The visual bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .clip(RoundedCornerShape(9.dp))
                        .background(color)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Severity ruler legend marks
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Seguro", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                Text("Leve", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                Text("Medio", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                Text("Grave", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                Text("Crítico", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
