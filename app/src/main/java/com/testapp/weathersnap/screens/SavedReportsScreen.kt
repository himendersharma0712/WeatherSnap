package com.testapp.weathersnap.screens


import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.testapp.weathersnap.data.local.WeatherReport
import com.testapp.weathersnap.savedReports.SavedReportsViewModel
import com.testapp.weathersnap.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SavedReportsScreen(
    contentPadding: PaddingValues,
    viewModel: SavedReportsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.horizontalGradient(listOf(HeaderGradientStart, HeaderGradientEnd))) //
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Saved Reports",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1B2214) //
                )


                val countText = when (state.reports.size) {
                    1 -> "1 report stored locally"
                    else -> "${state.reports.size} reports stored locally"
                }
                Text(
                    text = countText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1B2214).copy(alpha = 0.7f),
                    fontSize = 15.sp
                )
            }


            Button(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterEnd),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A3831)),
                shape = CircleShape
            ) {
                Text("Back", color = AccentGreen, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
        }


        if (!state.isLoading && state.reports.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No stored data snapshots.", color = Color.Gray)
            }
        }


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.reports, key = { it.id }) { report ->
                HistoryReportItemCard(report = report)
            }
        }
    }
}

@Composable
fun HistoryReportItemCard(report: WeatherReport) {

    val formattedDate = remember(report.timestamp) {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US)
            .format(Date(report.timestamp))
            .replace("AM", "am")
            .replace("PM", "pm")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground) //
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            val bitmap = remember(report.photoPath) {
                BitmapFactory.decodeFile(report.photoPath)
            }
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Saved telemetry file metadata picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black))
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = report.cityName,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = report.condition,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray.copy(alpha = 0.8f)
                )
            }


            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF3F4921)) //
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "${report.temperature.toInt()}°C",
                    style = MaterialTheme.typography.titleMedium,
                    color = AccentGreen, //
                    fontWeight = FontWeight.Bold
                )
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.02f))
                    .padding(12.dp)
            ) {
                Text("Original", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(2.dp))
                Text("${report.originalImageSize / 1024} KB", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFD4A373), fontWeight = FontWeight.Bold)
            }


            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.02f))
                    .padding(12.dp)
            ) {
                Text("Compressed", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(2.dp))
                Text("${report.compressedImageSize / 1024} KB", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF56AB91), fontWeight = FontWeight.Bold)
            }
        }

        if (report.notes.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = report.notes,
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}