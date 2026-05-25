package com.example.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

// Data models for the content

data class ConceptoPregunta(
    val id: Int,
    val concepto: String,
    val definicion: String
)

data class TarjetaAprende(
    val id: Int,
    val categoria: String, // "peligros", "proteccion", "familia"
    val titulo: String,
    val icono: ImageVector,
    val explicacion: String,
    val consejo: String,
    val queHacer: String
)

object ContentDataProvider {

    // 50 REAL CYBERSECURITY QUESTIONS IN SPANISH
    val bancoPreguntas: List<ConceptoPregunta> = listOf(
        ConceptoPregunta(1, "Huella digital", "El rastro de información que dejas en internet al navegar o publicar."),
        ConceptoPregunta(2, "Phishing", "Mensajes y correos falsos que intentan engañarte para robar tus claves."),
        ConceptoPregunta(3, "Contraseña segura", "Clave larga que mezcla letras, números y símbolos, difícil de adivinar."),
        ConceptoPregunta(4, "Privacidad digital", "El control y derecho que tienes sobre tus propios datos personales en internet."),
        ConceptoPregunta(5, "Ciberacoso", "Acoso o burla hacia una persona por medio de redes sociales, chats o videojuegos."),
        ConceptoPregunta(6, "Ingeniería social", "Manipulación psicológica para que reveles información confidencial alegremente."),
        ConceptoPregunta(7, "Virus informático", "Software malicioso que daña o altera el funcionamiento de tu dispositivo."),
        ConceptoPregunta(8, "Doble factor (2FA)", "Método de seguridad donde introduces un segundo código para verificar tu identidad."),
        ConceptoPregunta(9, "Red Wi-Fi pública", "Conexión gratuita que cualquiera puede usar, pero que facilita el robo de datos."),
        ConceptoPregunta(10, "Geolocalización", "Función que comparte la ubicación geográfica exacta en tiempo real de tu móvil."),
        ConceptoPregunta(11, "Cortafuegos (Firewall)", "Sistema de defensa que analiza el tráfico de red de tu dispositivo para bloquear peligros."),
        ConceptoPregunta(12, "Malware", "Término general que define a cualquier tipo de programa dañino en internet."),
        ConceptoPregunta(13, "Antivirus", "Programa diseñado para detectar, bloquear y eliminar virus del sistema."),
        ConceptoPregunta(14, "VPN", "Red privada que oculta tu dirección IP y cifra tus datos para navegar seguro."),
        ConceptoPregunta(15, "Spyware", "Programa espía diseñado para vigilarte y recolectar tus contraseñas en secreto."),
        ConceptoPregunta(16, "Adware", "Mensajes y ventanas emergentes de publicidad invasiva e indeseada en tu navegador."),
        ConceptoPregunta(17, "Sextorsión", "Chantaje con difundir Fotos o videos íntimos tuyos para obligarte a hacer algo."),
        ConceptoPregunta(18, "Grooming", "Engaño de un adulto que se hace pasar por menor en internet para ganarse tu confianza."),
        ConceptoPregunta(19, "Netiqueta", "Conjunto de normas de comportamiento educado y respetuoso al escribir online."),
        ConceptoPregunta(20, "Cifrado de datos", "Codificar la información para que solo las personas autorizadas puedan leerla."),
        ConceptoPregunta(21, "Backup (Copia de seguridad)", "Resguardar tus archivos importantes en un disco o la nube para no perderlos."),
        ConceptoPregunta(22, "Cookie de internet", "Pequeño archivo que los sitios web guardan para recordar detalles de tu visita."),
        ConceptoPregunta(23, "Actualización de software", "Proceso de instalar mejoras de seguridad para tapar fallos de los programas."),
        ConceptoPregunta(24, "Enlace acortado", "Link de internet modificado para ser pequeño, usado con frecuencia para ocultar phishing."),
        ConceptoPregunta(25, "Suplantación de identidad", "Hacerse pasar por otra persona real creando perfiles falsos en redes sociales."),
        ConceptoPregunta(26, "Oversharing", "Compartir demasiados detalles de tu vida privada en tus fotos o publicaciones."),
        ConceptoPregunta(27, "Fake news", "Noticias o rumores falsos inventados para desinformar o crear pánico en la red."),
        ConceptoPregunta(28, "Troll de internet", "Persona que publica mensajes agresivos o burlas solo para molestar a otros."),
        ConceptoPregunta(29, "Metadatos en fotos", "Información oculta en una foto que revela con qué cámara y dónde fue tomada."),
        ConceptoPregunta(30, "Ransomware", "Programa que secuestra tus archivos y te exige un pago en dinero para liberarlos."),
        ConceptoPregunta(31, "Spam", "Correos electrónicos publicitarios o sospechosos enviados masivamente sin tu permiso."),
        ConceptoPregunta(32, "HTTPS", "Protocolo de red seguro que asegura que los datos viajan protegidos hacia una web."),
        ConceptoPregunta(33, "Ajustes de privacidad", "Panel donde limitas qué personas pueden ver lo que subes en tus redes sociales."),
        ConceptoPregunta(34, "Modo incógnito", "Ventana de navegación que no guarda tu historial localmente, pero no te hace invisible."),
        ConceptoPregunta(35, "Typosquatting", "Webs falsas que imitan nombres reales con errores tipográficos insignificantes."),
        ConceptoPregunta(36, "Clonación de tarjeta", "Copia no autorizada de los datos de pago para realizar compras fraudulentas."),
        ConceptoPregunta(37, "Términos y condiciones", "Contrato que aceptas al registrarte donde autorizas qué harán con tus datos."),
        ConceptoPregunta(38, "Bot de internet", "Programa automático que realiza tareas repetitivas, como mandar enlaces abusivos."),
        ConceptoPregunta(39, "Bloqueador de anuncios", "Extensión para el navegador que bloquea la publicidad molesta y rastreadores."),
        ConceptoPregunta(40, "Contraseña maestra", "Clave única e hipersegura que abre tu bóveda del gestor de contraseñas."),
        ConceptoPregunta(41, "Gestor de contraseñas", "App que guarda e inventa claves fuertes por ti de forma ultra protegida."),
        ConceptoPregunta(42, "Huella digital pasiva", "Los datos que dejas sin darte cuenta, como tu dirección IP o navegador."),
        ConceptoPregunta(43, "Huella digital activa", "Los datos que compartes intencionalmente, como subir una foto o un comentario."),
        ConceptoPregunta(44, "Ingeniería social inversa", "Engañarte para que tú seas quien busque al estafador pidiéndole ayuda ficticia."),
        ConceptoPregunta(45, "Envenenamiento de DNS", "Truco que desvía tu navegador hacia una web impostora idéntica sin que lo notes."),
        ConceptoPregunta(46, "Suscripción trampa", "Casillas marcadas ocultas que te cobran dinero mensualmente sin aviso claro."),
        ConceptoPregunta(47, "Desconexión digital", "Decisión consciente de apagar pantallas para descansar tu mente y vista."),
        ConceptoPregunta(48, "Rastreo de aplicaciones", "Permiso que piden las apps para vigilar qué haces en otras webs y vender anuncios."),
        ConceptoPregunta(49, "Cuentas inactivas", "Perfiles viejos que ya no usas pero siguen guardando tus datos antiguos expuestos."),
        ConceptoPregunta(50, "Ciberhigiene", "Hábitos diarios de seguridad como actualizar, borrar cuentas viejas y revisar claves.")
    )

    // 15 REAL EDUCATION CARDS (5 PER TAB)
    val tarjetasAprende: List<TarjetaAprende> = listOf(
        // Categoria 1: peligros
        TarjetaAprende(
            1, "peligros", "Phishing en Chat", Icons.Default.Email,
            "Cuentas falsas o hackeadas de amigos te envían un link diciendo que ganaste un premio o necesitas iniciar sesión urgente.",
            "Desconfía de enlaces raros y ofertas demasiado buenas para ser verdad, aunque vengan de conocidos.",
            "Pregunta a tu amigo por fuera de la red si él te mandó eso. ¡Nunca ingreses tus datos!"
        ),
        TarjetaAprende(
            2, "peligros", "Cyberbullying / Acoso", Icons.Default.Warning,
            "Mensajes repetitivos de insultos, exclusión de grupos o burlas en salas de juego y chats de redes sociales.",
            "Recuerda que nada justifica las agresiones. Configura la privacidad para bloquear a extraños.",
            "No respondas a las provocaciones, toma capturas de pantalla de prueba y avisa a un adulto de confianza inmediatamente."
        ),
        TarjetaAprende(
            3, "peligros", "Grooming o Suplantación", Icons.Default.Person,
            "Adultos con malas intenciones se crean perfiles infantiles para chatear contigo, ser tus amigos y pedirte fotos privadas.",
            "No agregues a desconocidos y nunca aceptes videollamadas con personas que no conozcas cara a cara en la vida real.",
            "Si alguien te habla de cosas incómodas o te pide fotos, avisa de inmediato a un adulto. No borres los mensajes, servirán de prueba."
        ),
        TarjetaAprende(
            4, "peligros", "Malware y virus", Icons.Default.BugReport,
            "Descargar juegos modificados, trucos (hacks) o películas gratis suele venir acompañado de virus ocultos que dañan el móvil.",
            "Descarga únicamente aplicaciones desde tiendas oficiales como Google Play y mantén el antivirus activo.",
            "Si tu teléfono empieza a andar muy lento o salen anuncios solos, pídele a un cuidador que te ayude a formatearlo y limpiarlo."
        ),
        TarjetaAprende(
            5, "peligros", "Oversharing (Sobreexposición)", Icons.Default.Visibility,
            "Publicar todo lo que haces: la escuela a la que vas, el club, tu dormitorio o fotos de tus rutinas diarias expone tu privacidad.",
            "Piensa dos veces antes de subir fotos o videos: una vez en internet, es imposible volver a borrarlo del todo.",
            "Usa los filtros de privacidad de las redes sociales para que solo tus amigos de verdad vean lo que subes."
        ),

        // Categoria 2: proteccion
        TarjetaAprende(
            6, "proteccion", "Contraseñas acorazadas", Icons.Default.Lock,
            "El escudo de entrada a tus cuentas. Una clave débil permite que extraños lean tus conversaciones y te suplanten.",
            "No utilices tu nombre ni tu fecha de nacimiento. Mezcla mayúsculas, números y caracteres especiales (como @, $, !).",
            "Usa un gestor de contraseñas seguro para no olvidarlas y configúralo con una clave única de acceso."
        ),
        TarjetaAprende(
            7, "proteccion", "Candado Doble (2FA)", Icons.Default.Security,
            "El doble factor de verificación le exige un segundo código enviado a tu móvil a cualquiera que intente abrir tu cuenta.",
            "Activa el segundo factor de autenticación en todas tus aplicaciones importantes (WhatsApp, Instagram, Google).",
            "Si te llega un código de seguridad sin que lo hayas pedido, no se lo pases a nadie: significa que alguien tiene tu clave inicial."
        ),
        TarjetaAprende(
            8, "proteccion", "Desactivar ubicación", Icons.Default.LocationOn,
            "Las fotos y aplicaciones registran de forma automática la parte exacta de la ciudad donde estás cuando subes algo.",
            "Desactiva el permiso de ubicación de la cámara y configúralo en las redes sociales para que solo se use al buscar mapas.",
            "Revisa cada mes qué aplicaciones tienen el permiso de ubicación activo en el menú de privacidad de tu dispositivo."
        ),
        TarjetaAprende(
            9, "proteccion", "Cuidado con Wi-Fi público", Icons.Default.Wifi,
            "Las conexiones gratuitas en plazas o cafeterías no protegen tus datos y un atacante en la misma red podría espiar lo que envías.",
            "Evita transferir dinero, abrir cuentas confidenciales o ingresar claves secretas cuando estés conectado a una red pública.",
            "Si debes usarla sí o sí, utiliza una conexión VPN segura para proteger y cifrar tus deambulaciones por la red."
        ),
        TarjetaAprende(
            10, "proteccion", "Borrar cuentas viejas", Icons.Default.Delete,
            "Mantener cuentas de juegos o redes que no usas desde hace años deja tus datos antiguos a merced de hackers e intrusos.",
            "Dedica un día al año a hacer 'ciberhigiene' eliminando cuentas viejas de plataformas de videojuegos y redes inactivas.",
            "Escribe en el buscador de internet tu nombre completo para ver qué perfiles e imágenes viejas aparecen y solicita darles de baja."
        ),

        // Categoria 3: familia
        TarjetaAprende(
            11, "familia", "Zonas Libres de Pantallas", Icons.Default.Home,
            "Espacios físicos del hogar donde se prohíbe el uso de teléfonos para fomentar la conversación y la desconexión emocional.",
            "Definan juntos en familia que la mesa del almuerzo y las habitaciones por la noche son zonas completamente libres de dispositivos.",
            "Coloca una canasta en el comedor para dejar los celulares silenciados durante las horas familiares de reunión."
        ),
        TarjetaAprende(
            12, "familia", "Tiempos de Desconexión", Icons.Default.Timer,
            "El uso excesivo de pantallas puede alterar el sueño, generar ansiedad y restarle horas a los lazos reales de afecto.",
            "Configuren temporizadores de uso en las apps y establezcan un horario límite para apagar el Wi-Fi en las noches.",
            "Conversen sobre qué actividades les gustaría hacer juntos fuera de internet (juegos de mesa, paseos, cocinar)."
        ),
        TarjetaAprende(
            13, "familia", "Diálogo SIN Castigos", Icons.Default.Forum,
            "Si un menor comete un error en internet por inocencia, el castigo severo provoca que oculte los problemas de ciberacoso.",
            "Establezcan la regla de oro: pase lo que pase en internet, si me lo cuentas, no habrá un castigo, buscaremos la solución juntos.",
            "Pregúntale a tus hijos con alegría sobre sus juegos y creadores de contenido favoritos para tender puentes digitales de confianza."
        ),
        TarjetaAprende(
            14, "familia", "Configuración Parental", Icons.Default.Settings,
            "Herramientas tecnológicas que te ayudan a bloquear contenidos inapropiados según la madurez de cada aventurero.",
            "Configura Google Family Link o herramientas parentales similares para definir límites saludables y autorizar descargas.",
            "Utiliza navegadores y buscadores infantiles como YouTube Kids si tus hijos se encuentran en edades de primaria temprana."
        ),
        TarjetaAprende(
            15, "familia", "Acompañamiento Activo", Icons.Default.EscalatorWarning,
            "La mejor protección no consiste en espiar el móvil en silencio, sino en guiar y enseñar con el ejemplo sobre autoprotección.",
            "Expliquen juntos en familia qué cosas no se deben publicar en internet (nombres, escuelas, fotos de uniformes).",
            "Jueguen partidas de videojuegos juntos para entender sus dinámicas y poder asesorarlos ante trampas o jugadores tóxicos."
        )
    )
}
