package com.example.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.security.SecureRandom

class HuellaViewModel(val app: Application) : AndroidViewModel(app) {

    private val prefsManager = PreferencesManager(app)
    private val db = GameDatabase.getDatabase(app)
    private val repository = GameRepository(db.resultadoDao())

    // 1. STREAMS OF PREFERENCES
    val selectedProfile: StateFlow<String?> = prefsManager.profileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedTheme: StateFlow<String> = prefsManager.themeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "system")

    val resultadosJuego: StateFlow<List<ResultadoJuego>> = repository.allResultados
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 2. STREAMS FOR GAME 1: DIGITAL FOOTPRINT
    private val _exposedServices = MutableStateFlow<Set<String>>(emptySet())
    val exposedServices: StateFlow<Set<String>> = _exposedServices.asStateFlow()

    private val _exposedDataList = MutableStateFlow<List<String>>(emptyList())
    val exposedDataList: StateFlow<List<String>> = _exposedDataList.asStateFlow()

    private val _showHuellaSummary = MutableStateFlow(false)
    val showHuellaSummary: StateFlow<Boolean> = _showHuellaSummary.asStateFlow()

    // 10 Services and what they track
    val listaServicios = listOf(
        ServicioDigital("Instagram", "tu ubicación, fotos privadas, contactos, gustos y hábitos de navegación.", "Evita publicar fotos con uniformes escolares o rutinas fijas y pon tu cuenta en modo 'Privado'."),
        ServicioDigital("TikTok", "vídeos grabados por ti, historial de búsqueda, contactos y tiempo que pasas en la app.", "Limita el tiempo de uso diario y desactiva la descarga de tus videos por parte de desconocidos."),
        ServicioDigital("WhatsApp", "número de foto de perfil, números de tus contactos, estado de conexión y tu ubicación si la compartes.", "Configura la privacidad para que solo tus contactos guardados puedan ver tu foto de perfil, info y estados."),
        ServicioDigital("Facebook", "conexiones familiares, correos, opiniones, me gusta y fotos históricas etiquetadas.", "Revisa el registro de actividad y elimina las etiquetas de fotos inadecuadas tomadas hace años."),
        ServicioDigital("Twitter/X", "tus pensamientos en tiempo real, ubicación al tuitear y tu red de contactos directos.", "Desactiva la geolocalización de tus tweets antes de publicar y protege tus posts si prefieres privacidad."),
        ServicioDigital("YouTube", "tus gustos musicales, videos vistos en exceso, historial de búsquedas y comentarios.", "Usa el modo restringido o de pausa de historial de reproducciones si no quieres que el algoritmo rastree todo."),
        ServicioDigital("Snapchat", "fotos efímeras guardadas en servidores, mapa de localización en directo y contactos.", "Activa el 'Modo Fantasma' para que nadie en el mapa del servicio pueda ver dónde estás parado físicamente."),
        ServicioDigital("Spotify", "música que disfrutas, listas creadas, tu Facebook sincronizado y actividad al momento.", "Desactiva la opción de hacer públicas tus listas de reproducción por defecto en la configuración principal."),
        ServicioDigital("Amazon", "artículos comprados, dirección de tu domicilio, métodos de pago y tus hábitos de búsqueda.", "Borra las búsquedas de tu historial y nunca guardes datos de tarjetas bancarias para compras automáticas de un clic."),
        ServicioDigital("Google", "búsquedas de voz, ubicaciones por donde caminas, historial web, fotos y correos electrónicos.", "Visita la página de 'Mi Actividad' de Google periódicamente para borrar el historial de ubicaciones grabadas.")
    )

    // Data class for local structure
    data class ServicioDigital(
        val nombre: String,
        val datosExpuestos: String,
        val consejo: String
    )

    fun touchService(serviceName: String) {
        val servicio = listaServicios.firstOrNull { it.nombre == serviceName } ?: return
        if (serviceName !in _exposedServices.value) {
            _exposedServices.value = _exposedServices.value + serviceName
            _exposedDataList.value = _exposedDataList.value + "${servicio.nombre}: ${servicio.datosExpuestos}"
        }
        // Auto show summary when all 10 services are clicked
        if (_exposedServices.value.size >= 10) {
            _showHuellaSummary.value = true
        }
    }

    fun showFootprintSummary() {
        _showHuellaSummary.value = true
    }

    fun resetHuellaJuego() {
        _exposedServices.value = emptySet()
        _exposedDataList.value = emptyList()
        _showHuellaSummary.value = false
    }


    // 3. STREAMS FOR GAME 2: CONCEPT MATCHING
    private val _matchingQuestions = MutableStateFlow<List<ConceptoPregunta>>(emptyList())
    val matchingQuestions: StateFlow<List<ConceptoPregunta>> = _matchingQuestions.asStateFlow()

    // Shuffle and provide concepts for column A, definitions for column B (shuffled differently)
    private val _shuffledConcepts = MutableStateFlow<List<ConceptoPregunta>>(emptyList())
    val shuffledConcepts: StateFlow<List<ConceptoPregunta>> = _shuffledConcepts.asStateFlow()

    private val _shuffledDefinitions = MutableStateFlow<List<ConceptoPregunta>>(emptyList())
    val shuffledDefinitions: StateFlow<List<ConceptoPregunta>> = _shuffledDefinitions.asStateFlow()

    private val _selectedConcept = MutableStateFlow<ConceptoPregunta?>(null)
    val selectedConcept: StateFlow<ConceptoPregunta?> = _selectedConcept.asStateFlow()

    private val _selectedDefinition = MutableStateFlow<ConceptoPregunta?>(null)
    val selectedDefinition: StateFlow<ConceptoPregunta?> = _selectedDefinition.asStateFlow()

    private val _matchedIds = MutableStateFlow<Set<Int>>(emptySet())
    val matchedIds: StateFlow<Set<Int>> = _matchedIds.asStateFlow()

    private val _tempCorrectMatchId = MutableStateFlow<Int?>(null)
    val tempCorrectMatchId: StateFlow<Int?> = _tempCorrectMatchId.asStateFlow()

    private val _gameFinished = MutableStateFlow(false)
    val gameFinished: StateFlow<Boolean> = _gameFinished.asStateFlow()

    private val _matchingScore = MutableStateFlow(0)
    val matchingScore: StateFlow<Int> = _matchingScore.asStateFlow()

    private var currentMatchAttempts = 0

    init {
        initNewMatchingGame()
    }

    fun initNewMatchingGame() {
        viewModelScope.launch {
            val random10 = ContentDataProvider.bancoPreguntas.shuffled().take(10)
            _matchingQuestions.value = random10
            _shuffledConcepts.value = random10.shuffled()
            _shuffledDefinitions.value = random10.shuffled()
            _selectedConcept.value = null
            _selectedDefinition.value = null
            _matchedIds.value = emptySet()
            _tempCorrectMatchId.value = null
            _gameFinished.value = false
            _matchingScore.value = 0
            currentMatchAttempts = 0
        }
    }

    fun selectConcept(preg: ConceptoPregunta) {
        if (preg.id in _matchedIds.value) return
        _selectedConcept.value = if (_selectedConcept.value == preg) null else preg
        checkAndMatch()
    }

    fun selectDefinition(preg: ConceptoPregunta) {
        if (preg.id in _matchedIds.value) return
        _selectedDefinition.value = if (_selectedDefinition.value == preg) null else preg
        checkAndMatch()
    }

    private fun checkAndMatch() {
        val concept = _selectedConcept.value
        val definition = _selectedDefinition.value

        if (concept != null && definition != null) {
            currentMatchAttempts++
            if (concept.id == definition.id) {
                // Correct match!
                viewModelScope.launch {
                    _matchedIds.value = _matchedIds.value + concept.id
                    _tempCorrectMatchId.value = concept.id
                    _matchingScore.value = _matchingScore.value + 1

                    // Reset choices
                    _selectedConcept.value = null
                    _selectedDefinition.value = null

                    delay(600) // visual animation delay duration
                    _tempCorrectMatchId.value = null

                    // Check game end
                    if (_matchedIds.value.size >= 10) {
                        finishMatchingGame()
                    }
                }
            } else {
                // Incorrect match, auto clear selections after a brief delay so user can see their mistake
                viewModelScope.launch {
                    delay(500)
                    _selectedConcept.value = null
                    _selectedDefinition.value = null
                }
            }
        }
    }

    private suspend fun finishMatchingGame() {
        _gameFinished.value = true
        // Store score in database!
        repository.insert(ResultadoJuego(puntaje = _matchingScore.value))
    }

    fun resetMatchingGame() {
        initNewMatchingGame()
    }


    // 4. STREAMS FOR SECTION "LEARN" (Flip card states are stored in UI directly, no backend needed,
    // but we can expose card list)
    val learnCardsList = ContentDataProvider.tarjetasAprende


    // 5. STREAMS FOR FAMILY AGREEMENT (CONVENIO FAMILIAR)
    val defaultText = """CONVENIO FAMILIAR PARA EL USO SEGURO Y RESPONSABLE DE DISPOSITIVOS

Nosotros, en familia, nos comprometemos a cumplir y respetar las siguientes pautas sobre tecnología para convivir felices y protegidos:

1. HORARIOS SALUDABLES: No usaremos teléfonos ni tabletas durante las comidas principales ni una hora antes de ir a dormir.
2. ZONAS LIBRES DE SECTORES: Las habitaciones por la noche y los baños serán áreas completamente libres de pantallas. Los móviles cargarán en la sala de estar comunes.
3. CONTRAPRESTACIÓN AL APRENDIZAJE: Antes de descargar una aplicación o juego nuevo, pediremos autorización a papá/mamá/cuidador/a.
4. CONVERSACIÓN ABIERTA: Si veo algo raro o alguien me molesta o pide fotos incómodas en internet, se lo comentaré de inmediato a mi cuidador/a, sabiendo que no recibiré un castigo.
5. RESPETO MUTUO: No publicaremos fotos ni videos de otras personas sin su previo consentimiento y cuidaremos nuestra netiqueta digital.
6. CONTRASENAS PRIVADAS: Compartiremos nuestras claves únicamente con nuestros padres o cuidadores principales para emergencias. ¡Nunca con amigos!
7. LÍMITES DE TIEMPO: Acordamos un límite diario de uso recreativo de pantallas. Cuando suene la alarma del temporizador, apagaremos el juego con alegría.
8. DERECHO A LA DESCONEXION: Realizaremos actividades conjuntas al aire libre, lectura o conversación de forma frecuente apagando los datos.
9. SEGURIDAD ANTE DESCONOCIDOS: No agregaremos a personas que no conocemos cara a cara en la vida real en chats o redes.

Firmado con compromiso familiar."""

    private val _agreementText = MutableStateFlow(defaultText)
    val agreementText: StateFlow<String> = _agreementText.asStateFlow()

    fun updateAgreementText(newTxt: String) {
        _agreementText.value = newTxt
    }


    // 6. STREAMS FOR PASSWORD GENERATOR
    private val _passwordLength = MutableStateFlow(12)
    val passwordLength: StateFlow<Int> = _passwordLength.asStateFlow()

    private val _includeUpper = MutableStateFlow(true)
    val includeUpper: StateFlow<Boolean> = _includeUpper.asStateFlow()

    private val _includeNumbers = MutableStateFlow(true)
    val includeNumbers: StateFlow<Boolean> = _includeNumbers.asStateFlow()

    private val _includeSymbols = MutableStateFlow(true)
    val includeSymbols: StateFlow<Boolean> = _includeSymbols.asStateFlow()

    private val _generatedPassword = MutableStateFlow("")
    val generatedPassword: StateFlow<String> = _generatedPassword.asStateFlow()

    private val _isGeneratingLoading = MutableStateFlow(false)
    val isGeneratingLoading: StateFlow<Boolean> = _isGeneratingLoading.asStateFlow()

    fun updateLength(len: Int) {
        _passwordLength.value = len
    }

    fun toggleUpper() {
        _includeUpper.value = !_includeUpper.value
    }

    fun toggleNumbers() {
        _includeNumbers.value = !_includeNumbers.value
    }

    fun toggleSymbols() {
        _includeSymbols.value = !_includeSymbols.value
    }

    fun generateSecurePassword() {
        viewModelScope.launch {
            _isGeneratingLoading.value = true
            
            val charsLower = "abcdefghijklmnopqrstuvwxyz"
            val charsUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            val charsNums = "0123456789"
            val charsSyms = "!@#$%^&*()_+-=[]{}|;:,.<>?"

            var pool = charsLower
            if (_includeUpper.value) pool += charsUpper
            if (_includeNumbers.value) pool += charsNums
            if (_includeSymbols.value) pool += charsSyms

            val length = _passwordLength.value
            val random = SecureRandom()

            // To simulate the "slot machine" fast characters shuffling effect requested
            // we update the password string 8 times with a tiny delay before setting the final strong password.
            for (i in 0..7) {
                val tempPassword = (1..length)
                    .map { pool[random.nextInt(pool.length)] }
                    .joinToString("")
                _generatedPassword.value = tempPassword
                delay(40)
            }

            // Generate guaranteed strong password complying with checkboxes
            val finalPasswordBuilder = StringBuilder()
            val requiredChars = mutableListOf<Char>()
            
            if (_includeUpper.value) {
                requiredChars.add(charsUpper[random.nextInt(charsUpper.length)])
            }
            if (_includeNumbers.value) {
                requiredChars.add(charsNums[random.nextInt(charsNums.length)])
            }
            if (_includeSymbols.value) {
                requiredChars.add(charsSyms[random.nextInt(charsSyms.length)])
            }
            
            // Fill rest
            val restLength = length - requiredChars.size
            for (i in 0 until restLength) {
                requiredChars.add(pool[random.nextInt(pool.length)])
            }
            
            // Shuffle list to randomize positions
            requiredChars.shuffle()
            requiredChars.forEach { finalPasswordBuilder.append(it) }

            _generatedPassword.value = finalPasswordBuilder.toString()
            _isGeneratingLoading.value = false
        }
    }

    // Dynamic strength color and label indicator
    fun getPasswordStrength(password: String): PasswordStrength {
        if (password.isEmpty()) return PasswordStrength.DEBIL
        val length = password.length
        
        val hasUpper = password.any { it.isUpperCase() }
        val hasNumber = password.any { it.isDigit() }
        val hasSymbol = password.any { !it.isLetterOrDigit() }

        var score = 0
        if (length >= 10) score++
        if (length >= 14) score++
        if (hasUpper) score++
        if (hasNumber) score++
        if (hasSymbol) score++

        return when {
            length < 8 || score <= 2 -> PasswordStrength.DEBIL
            length < 13 || score <= 4 -> PasswordStrength.MEDIA
            else -> PasswordStrength.FUERTE
        }
    }

    enum class PasswordStrength(val label: String, val colorHex: Long) {
        DEBIL("Débil - ¡Riesgo alto!", 0xFFE57373), // Rojo suave
        MEDIA("Saturación Media - Aceptable", 0xFFFFD54F), // Amarillo suave
        FUERTE("Fuerte - ¡Excelente seguridad!", 0xFF81C784) // Verde suave
    }

    // Clipboard Copy
    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Contraseña Segura", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Contraseña copiada al portapapeles", Toast.LENGTH_SHORT).show()
    }

    // Share Content
    fun sharePassword(context: Context, text: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Mi nueva clave segura es: $text")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Compartir Contraseña")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }


    // 7. PREFS SETTERS
    fun changeProfile(profile: String?) {
        viewModelScope.launch {
            prefsManager.saveProfile(profile)
        }
    }

    fun changeTheme(theme: String) {
        viewModelScope.launch {
            prefsManager.saveTheme(theme)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            prefsManager.clearPreferences()
            repository.clearAll()
            resetHuellaJuego()
            initNewMatchingGame()
        }
    }
}
