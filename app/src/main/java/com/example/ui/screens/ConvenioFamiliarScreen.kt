package com.example.ui.screens

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EscalatorWarning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.HuellaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvenioFamiliarScreen(
    viewModel: HuellaViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val profile by viewModel.selectedProfile.collectAsState()
    val agreementText by viewModel.agreementText.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .windowInsetsPadding(WindowInsets.safeDrawing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.testTag("back_from_agreement_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Convenio Familiar",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (profile == "child") {
            // Screen state for child: Display friendly informative card to request parents
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.EscalatorWarning,
                            contentDescription = "Sólida vigilancia",
                            modifier = Modifier.size(72.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Convenio de Uso de Dispositivos",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Pide a una persona adulta que revise el convenio contigo.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Este convenio ayuda a establecer horarios saludables para jugar, lugares donde descansar del móvil y compromisos para navegar juntos y súper seguros.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        } else {
            // Screen state for caregiver: Editable document interface
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Convenio familiar de uso de dispositivos",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = "A continuación, te presentamos una propuesta realista de acuerdo familiar. Edita las cláusulas para adaptarlas al ritmo cotidiano de tu familia y presiona descargar para obtener el PDF imprimible.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Editable text box
                OutlinedTextField(
                    value = agreementText,
                    onValueChange = { viewModel.updateAgreementText(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .testTag("agreement_editor_field"),
                    label = { Text("Texto del convenio") },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 22.sp,
                        fontSize = 15.sp
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { generatePdfFile(context, agreementText) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("generate_pdf_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Generar PDF",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// Complete local, native PDF generator method that writes files safely into MediaStore / Downloads and works offline
private fun generatePdfFile(context: Context, textContent: String) {
    val doc = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 portrait size
    var page = doc.startPage(pageInfo)
    var canvas = page.canvas

    val textPaint = Paint().apply {
        textSize = 11f
        isAntiAlias = true
    }

    val titlePaint = Paint().apply {
        textSize = 15f
        isFakeBoldText = true
        isAntiAlias = true
    }

    var y = 60f
    
    // Header brand draw
    canvas.drawText("CONVENIO FAMILIAR DE DISPOSITIVOS - HUELLASEGURA", 40f, y, titlePaint)
    y += 15f
    canvas.drawText("Guía autoprotectora de responsabilidades familiares en internet.", 40f, y, textPaint.apply { isFakeBoldText = true })
    textPaint.isFakeBoldText = false
    y += 35f

    val lines = textContent.split("\n")
    for (line in lines) {
        val words = line.split(" ")
        var currentLine = ""
        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val width = textPaint.measureText(testLine)
            if (width > 515f) { // Page bounds matching margin
                canvas.drawText(currentLine, 40f, y, textPaint)
                y += 18f
                currentLine = word
                
                // Page limits detection
                if (y > 780f) {
                    doc.finishPage(page)
                    val newPageInfo = PdfDocument.PageInfo.Builder(595, 842, 2).create()
                    page = doc.startPage(newPageInfo)
                    canvas = page.canvas
                    y = 50f
                }
            } else {
                currentLine = testLine
            }
        }
        if (currentLine.isNotEmpty()) {
            canvas.drawText(currentLine, 40f, y, textPaint)
            y += 18f
        }
        y += 4f // spacing between paragraphs
        
        if (y > 780f) {
            doc.finishPage(page)
            val newPageInfo = PdfDocument.PageInfo.Builder(595, 842, 2).create()
            page = doc.startPage(newPageInfo)
            canvas = page.canvas
            y = 50f
        }
    }

    doc.finishPage(page)

    // Save using MediaStore for sandboxed writes to DIRECTORY_DOWNLOADS without needing storage permission
    try {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "Convenio_Familiar_HuellaSegura.pdf")
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
        }
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            resolver.openOutputStream(uri)?.use { out ->
                doc.writeTo(out)
            }
            Toast.makeText(context, "Convenio guardado en 'Descargas' con éxito (Sin Internet)", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Error al generar enlace pre-descarga", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Error al guardar PDF: ${e.message}", Toast.LENGTH_LONG).show()
    } finally {
        doc.close()
    }
}
