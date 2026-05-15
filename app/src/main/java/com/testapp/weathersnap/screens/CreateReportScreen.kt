
package com.testapp.weathersnap.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.testapp.weathersnap.createReport.CreateReportViewModel
import com.testapp.weathersnap.ui.theme.*
import java.util.Locale

@Composable
fun CreateReportScreen(
    contentPadding: PaddingValues,
    navController: NavController,
    viewModel: CreateReportViewModel = hiltViewModel(),
    onNavigateToCamera: () -> Unit,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val currentBackStackEntry = navController.currentBackStackEntry
    LaunchedEffect(key1 = currentBackStackEntry) {
        val handle = currentBackStackEntry?.savedStateHandle
        val path = handle?.get<String>("photoPath")
        val size = handle?.get<Long>("originalSize")

        if (path != null && size != null) {
            viewModel.onPhotoCaptured(path = path, originalSize = size, compressedSize = (size * 0.35).toLong())
            handle.remove<String>("photoPath")
            handle.remove<Long>("originalSize")
        }
    }

    val state by viewModel.uiState.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(contentPadding)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.horizontalGradient(listOf(HeaderGradientStart, HeaderGradientEnd)))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column {
                Text("Create Report", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF1B2214))
                Text("Capture, compress, annotate", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1B2214).copy(alpha = 0.7f))
            }
            Button(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterEnd).height(36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333D25)),
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
            ) {
                Text("Back", color = AccentGreen, fontSize = 12.sp)
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(state.cityName, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(state.condition, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                }
                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color.Transparent).padding(horizontal = 10.dp, vertical = 4.dp)) {
                    Text("${state.temperature.toInt()}°C", style = MaterialTheme.typography.titleMedium, color = AccentGreen, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                WeatherStatItem(
                    label = "Humidity",
                    value = "${state.humidity}%",
                    bgColor = Color(0xFF1D2622),
                    textColor = Color(0xFF388E3C),
                    modifier = Modifier.weight(1f)
                )
                WeatherStatItem(
                    label = "Wind",
                    value = String.format(Locale.US, "%.1f" + " m/s", state.windSpeed),
                    bgColor = Color(0xFF20272F),
                    textColor = Color(0xFF3F7BB3),
                    modifier = Modifier.weight(1f)
                )
                WeatherStatItem(
                    label = "Pressure",
                    value = "${state.pressure.toInt()}",
                    bgColor = Color(0xFF2B221B),
                    textColor = Color(0xFF9E6334),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 3. PHOTO SECTION (FLEXIBLE WEIGHT)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.2f) // Takes more space
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF323625))
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (state.photoPath == null) {
                    Text("Photo Preview", color = Color.White.copy(alpha = 0.4f), style = MaterialTheme.typography.bodySmall)
                } else {
                    val bitmap = remember(state.photoPath) { BitmapFactory.decodeFile(state.photoPath) }
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            if (state.photoPath != null) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Original: ${state.originalSizeText}", color = Color.Gray, fontSize = 10.sp)
                    Text("Compressed: ${state.compressedSizeText}", color = AccentGreen, fontSize = 10.sp)
                }
            }

            Button(
                onClick = onNavigateToCamera,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                shape = CircleShape
            ) {
                Text("Capture Photo", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Field Notes", style = MaterialTheme.typography.labelLarge, color = Color.White)
            OutlinedTextField(
                value = state.notes,
                onValueChange = viewModel::onNotesChange,
                modifier = Modifier.fillMaxSize(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentGreen,
                    unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                    focusedLabelColor = AccentGreen,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = AccentGreen
                ),
                shape = RoundedCornerShape(10.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
            )
        }


        if (state.isSaving) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).size(30.dp), color = AccentGreen)
        } else {
            Button(
                onClick = { viewModel.saveReport(onSaveSuccess) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                shape = CircleShape
            ) {
                Text("Save Report", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}