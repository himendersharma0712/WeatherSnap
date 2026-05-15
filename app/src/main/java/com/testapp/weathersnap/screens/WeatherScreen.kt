
package com.testapp.weathersnap.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.testapp.weathersnap.data.remote.City
import com.testapp.weathersnap.data.remote.CurrentWeather
import com.testapp.weathersnap.ui.theme.AccentGreen
import com.testapp.weathersnap.ui.theme.CardBackground
import com.testapp.weathersnap.ui.theme.HeaderGradientEnd
import com.testapp.weathersnap.ui.theme.HeaderGradientStart
import com.testapp.weathersnap.weather.WeatherViewModel
import com.testapp.weathersnap.weather.getWeatherDescription

@Composable
fun WeatherScreen(
    contentPadding: PaddingValues,
    viewModel: WeatherViewModel = hiltViewModel(),
    onNavigateToCreateReport: (City, CurrentWeather) -> Unit,
    onNavigateToReports: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF12140E))
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        HeaderCard(onReportsClick = onNavigateToReports)


        SearchSection(
            query = state.searchQuery,
            onQueryChange = viewModel::onQueryChange,
            suggestions = state.suggestions,
            onCityClick = viewModel::onCitySelected,
            onSearchButtonClick = {
                if (state.suggestions.isNotEmpty()) {
                    viewModel.onCitySelected(state.suggestions.first())
                }
            }
        )


        if (state.isSearchingSuggestions && state.suggestions.isEmpty()) {
            FindingCitiesWidget()
        }


        Box(modifier = Modifier.fillMaxWidth()) {
            if (state.isLoading) {
                LoadingWeatherWidget()
            }


            androidx.compose.animation.AnimatedVisibility(
                visible = state.weatherData != null && !state.isLoading,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                state.weatherData?.let { weather ->
                    WeatherDetailCard(
                        city = (state.selectedCity?.name + ", " + state.selectedCity?.country) ?: "Unknown",
                        weather = weather,
                        onCreateReport = {
                            onNavigateToCreateReport(state.selectedCity!!, weather)
                        }
                    )
                }
            }


            androidx.compose.animation.AnimatedVisibility(
                visible = state.weatherData == null && !state.isLoading,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                IdleStateWidget()
            }
        }


        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun HeaderCard(onReportsClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.horizontalGradient(listOf(HeaderGradientStart, HeaderGradientEnd)))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "WeatherSnap",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B2214)
            )
            Text(
                text = "Live weather reports with camera evidence",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1B2214).copy(alpha = 0.7f),
                fontSize = 10.sp
            )
        }
        Button(
            onClick = onReportsClick,
            modifier = Modifier.align(Alignment.CenterEnd).widthIn(max = 90.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333D25)),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text("Reports", color = AccentGreen, fontSize = 11.sp)
        }
    }
}



@Composable
fun FindingCitiesWidget() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = AccentGreen,
            strokeWidth = 2.dp
        )
        Text(
            text = "Finding cities...",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun LoadingWeatherWidget() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = AccentGreen,
                strokeWidth = 2.dp
            )
            Column {
                Text(
                    text = "Loading weather...",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Fetching coordinates and current conditions",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                )
            }
        }
    }
}

@Composable
fun SearchSection(
    query: String,
    onQueryChange: (String) -> Unit,
    suggestions: List<City>,
    onCityClick: (City) -> Unit,
    onSearchButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text("City") },
                modifier = Modifier.weight(1f).height(60.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentGreen,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = AccentGreen
                ),
                shape = RoundedCornerShape(5.dp),
                singleLine = true
            )
            Button(
                onClick = onSearchButtonClick,
                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                shape = CircleShape
            ) {
                Text("Search", color = Color.Black, fontWeight = FontWeight.SemiBold, fontSize = 9.sp)
            }
        }

        Text(
            text = "Enter more than 2 letters to start city suggestions.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 10.sp
        )


        AnimatedVisibility(visible = suggestions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 240.dp)
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(suggestions) { city ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White.copy(alpha = 0.03f))
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .clickable { onCityClick(city) }
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${city.name}, ${city.country ?: ""}",
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IdleStateWidget() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF243B2D), Color(0xFF16261E)))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Search. Capture. Save.",
                color = Color(0xFFE2EAD8),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        }


        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "No weather loaded",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 12.sp
            )
            Text(
                text = "Enter more than 2 letters, choose a city, then search.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun WeatherDetailCard(
    city: String,
    weather: CurrentWeather,
    onCreateReport: () -> Unit
) {
    val description = getWeatherDescription(weather.weatherCode ?: 0)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(city, style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = description, color = Color.Gray)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF3F4921))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    "${weather.temperature.toInt()}°C",
                    style = MaterialTheme.typography.headlineLarge,
                    color = AccentGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeatherStatItem(
                label = "Humidity",
                value = "${weather.humidity}%",
                bgColor = Color(0xFF1D2622),
                textColor = Color(0xFF388E3C),
                modifier = Modifier.weight(1f)
            )
            WeatherStatItem(
                label = "Wind",
                value = "${weather.windSpeed} m/s",
                bgColor = Color(0xFF20272F),
                textColor = Color(0xFF3F7BB3),
                modifier = Modifier.weight(1f)
            )
            WeatherStatItem(
                label = "Pressure",
                value = "${weather.pressure.toInt()}",
                bgColor = Color(0xFF2B221B),
                textColor = Color(0xFF9E6334),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Report readiness", color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Text("Camera and Room DB enabled", color = Color.White, fontSize = 12.sp)
        }

        Button(
            onClick = onCreateReport,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Create Report", color = Color.Black, modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun WeatherStatItem(
    label: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(12.dp)
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = textColor, fontWeight = FontWeight.SemiBold)
    }
}