# HuellaSegura - Aplicación de Autoprotección Familiar

**HuellaSegura** es una aplicación nativa de Android escrita en **Kotlin** y desarrollada con **Jetpack Compose**. Su objetivo principal es educar y concientizar a familias (cuidadores, niños y adolescentes) sobre la seguridad en internet, la protección de datos personales y la mitigación de la huella digital.

---

## Características de la Aplicación

La aplicación implementa de manera rigurosa las siguientes funcionalidades:

1. **Selector de Perfiles Inclusivo**: Pantalla de bienvenida interactiva donde se selecciona el perfil del usuario utilizando lenguaje no sexista e inclusivo:
   - **Persona adulta / Cuidador/a**
   - **Aventurera / Aventurero**
   - Saludos dinámicos ("Hola, Cuidador/a" u "Hola, Aventurera/o") y cambios de colores de acento/fondo adaptados a la psicología y tono de cada perfil.
2. **Juego de la Huella Digital (Mapa de Conexiones)**: "¿Qué información dejas en internet?" - Analiza 10 redes o servicios famosos (Instagram, TikTok, WhatsApp, Google, etc.). Revela de manera animada qué datos capturan, genera una pila acumulada de exposición y ofrece consejos para borrar o disminuir tu rastro.
3. **Desafío de Ciberseguridad (Emparejar Conceptos)**: Un juego con un banco de **50 preguntas reales** en español. Cada partida selecciona 10 preguntas aleatorias en un tablero de emparejar dos columnas (conceptos frente a definiciones básicas) con respuesta háptica (vibración) y marcas de éxito animadas.
4. **Tarjetas de Aprendizaje Volteables ("Aprende")**: Centro educativo con 3 pestañas ("Peligros en internet", "Cómo protegerte", "Consejos familiares") y un total de **15 tarjetas educativas reales** que efectúan una rotación 3D en el eje Y al hacer clic para revelar soluciones.
5. **Convenio Familiar**: Un editor interactivo cargado con un acuerdo realista de 9 cláusulas saludables sobre tecnología.
   - Solo editable y accesible desde el perfil de cuidador/a.
   - Genera un documento PDF formateado de forma nativa que se graba en la carpeta de Descargas del dispositivo para usarse de manera **100% offline** (sin internet).
6. **Generador de Contraseñas Fuertes**: Herramienta interactiva con deslizador de longitud (8 a 20 letras) y opciones para combinar mayúsculas, números y símbolos. Muestra un indicador visual de seguridad tricolor y ejecuta una animación de "máquina tragamonedas" al mezclar caracteres aleatorios. Ofrece copiado al portapapeles y compartido rápido.
7. **Limpieza de Rastro e Historial**: Diálogo de confirmación para vaciar la base de datos de récords locales (usando **Room**) y restablecer la selección de perfil en la memoria de preferencias (**DataStore**).

---

## Requisitos de Entorno

*   **Android Studio** Jellyfish o posterior.
*   **JDK 17** o posterior.
*   **Android SDK**:
    *   Minimum SDK: 24 (Android 7.0)
    *   Target SDK: 34+ / 36

---

## Instrucciones de Compilación (Gradle)

Puedes compilar la aplicación utilizando la terminal integrada de Android Studio o tu consola de comandos desde la carpeta raíz.

### 1. Compilar el proyecto en modo de depuración (Debug)
Para construir la versión de pruebas de la aplicación, ejecuta de forma estándar:
```bash
gradle assembleDebug
```
Esto creará el archivo APK instalable en:
`app/build/outputs/apk/debug/app-debug.apk`

### 2. Ejecutar Pruebas Unitarias y de Captura de Pantalla
La aplicación cuenta con soporte integrado para pruebas locales automatizadas con Robolectric y Roborazzi:
```bash
gradle :app:testDebugUnitTest
```

---

## Cómo Generar un APK Firmado de Producción (Release)

Para distribuir tu aplicación de manera segura e instalarla en cualquier dispositivo físico, debes crear un keystore y firmar el APK.

### Paso 1: Generar un Keystore de firma
Si no posees un archivo de fábrica, puedes generarlo desde la terminal usando `keytool` provisto con el JDK:
```bash
keytool -genkey -v -keystore mi-llave-segura.jks -keyalg RSA -keysize 2048 -validity 10000 -alias mi-alias
```
*Te solicitará definir una contraseña maestra para el almacenamiento y para el alias.*

### Paso 2: Configurar las Variables en Android Studio
Para no escribir tus claves privadas de forma visible en el código, la aplicación utiliza el **Secrets Gradle Plugin**. 

1. Abre el panel de **Secrets** de tu entorno o crea un archivo `.env` en el directorio principal (basándote en el archivo de plantilla `.env.example`).
2. Agrega las credenciales de tu almacén de claves generadas:
   ```env
   # Ruta completa de ubicación de tu archivo mi-llave-segura.jks
   KEYSTORE_PATH="/ruta/a/mi-llave-segura.jks"
   STORE_PASSWORD="tu_password_del_keystore"
   KEY_PASSWORD="tu_password_del_alias"
   ```

### Paso 3: Lanzar la compilación Release Firmada
Una vez ingresadas las variables en el entorno, genera el paquete firmado ejecutando:
```bash
gradle assembleRelease
```
El instalador de producción optimizado se encontrará almacenado en:
`app/build/outputs/apk/release/app-release.apk`

¡Listo para ser subido a Google Play Store o compartido en dispositivos Android de tu familia!
