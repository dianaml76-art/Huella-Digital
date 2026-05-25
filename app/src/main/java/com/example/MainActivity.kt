package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.HuellaViewModel
import com.example.ui.screens.*
import com.example.ui.theme.HuellaSeguraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Let's enable modern Edge-to-Edge draws natively
        enableEdgeToEdge()
        
        setContent {
            val viewModel: HuellaViewModel = viewModel()
            val profile by viewModel.selectedProfile.collectAsState()
            val themeChoice by viewModel.selectedTheme.collectAsState()

            val darkTheme = when (themeChoice) {
                "light" -> false
                "dark" -> true
                else -> isSystemInDarkTheme()
            }

            HuellaSeguraTheme(
                darkTheme = darkTheme,
                profile = profile
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (profile == null) {
                        WelcomeScreen(viewModel = viewModel)
                    } else {
                        MainAppContainer(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun MainAppContainer(
    viewModel: HuellaViewModel
) {
    var activeTab by remember { mutableStateOf("inicio") }
    // Backstack / state routing indicator: "main" (showing tab screens) or sub screen names
    var activeSubScreen by remember { mutableStateOf("main") }

    if (activeSubScreen == "main") {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("app_navigation_bar"),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    // TAB 1: Inicio
                    NavigationBarItem(
                        selected = activeTab == "inicio",
                        onClick = { activeTab = "inicio" },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Inicio"
                            )
                        },
                        label = { Text("Inicio") },
                        modifier = Modifier.testTag("nav_tab_inicio")
                    )

                    // TAB 2: Aprende
                    NavigationBarItem(
                        selected = activeTab == "aprende",
                        onClick = { activeTab = "aprende" },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Help,
                                contentDescription = "Aprende"
                            )
                        },
                        label = { Text("Aprende") },
                        modifier = Modifier.testTag("nav_tab_aprende")
                    )

                    // TAB 3: Juegos
                    NavigationBarItem(
                        selected = activeTab == "juegos",
                        onClick = { activeTab = "juegos" },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Gamepad,
                                contentDescription = "Juegos"
                            )
                        },
                        label = { Text("Juegos") },
                        modifier = Modifier.testTag("nav_tab_juegos")
                    )

                    // TAB 4: Ajustes
                    NavigationBarItem(
                        selected = activeTab == "ajustes",
                        onClick = { activeTab = "ajustes" },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Ajustes"
                            )
                        },
                        label = { Text("Ajustes") },
                        modifier = Modifier.testTag("nav_tab_ajustes")
                    )
                }
            }
        ) { innerPadding ->
            val screenModifier = Modifier.padding(innerPadding)
            when (activeTab) {
                "inicio" -> {
                    InicioScreen(
                        viewModel = viewModel,
                        onNavigateToPassword = { activeSubScreen = "password" },
                        onNavigateToAgreement = { activeSubScreen = "agreement" },
                        modifier = screenModifier
                    )
                }
                "aprende" -> {
                    AprendeScreen(
                        viewModel = viewModel,
                        modifier = screenModifier
                    )
                }
                "juegos" -> {
                    JuegosScreen(
                        viewModel = viewModel,
                        onNavigateToHuella = { activeSubScreen = "huella" },
                        onNavigateToMatching = { 
                            // Re-initialize question bank on entering matching game
                            viewModel.initNewMatchingGame()
                            activeSubScreen = "matching" 
                        },
                        modifier = screenModifier
                    )
                }
                "ajustes" -> {
                    AjustesScreen(
                        viewModel = viewModel,
                        modifier = screenModifier
                    )
                }
            }
        }
    } else {
        // FULLSCREEN SUB-SCREENS FLOWS
        when (activeSubScreen) {
            "huella" -> {
                HuellaJuegoScreen(
                    viewModel = viewModel,
                    onNavigateBack = { activeSubScreen = "main" }
                )
            }
            "matching" -> {
                ConceptMatchingScreen(
                    viewModel = viewModel,
                    onNavigateBack = { activeSubScreen = "main" }
                )
            }
            "password" -> {
                PasswordGeneratorScreen(
                    viewModel = viewModel,
                    onNavigateBack = { activeSubScreen = "main" }
                )
            }
            "agreement" -> {
                ConvenioFamiliarScreen(
                    viewModel = viewModel,
                    onNavigateBack = { activeSubScreen = "main" }
                )
            }
        }
    }
}
