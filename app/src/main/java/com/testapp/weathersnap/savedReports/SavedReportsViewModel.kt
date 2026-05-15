package com.testapp.weathersnap.savedReports


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testapp.weathersnap.data.local.WeatherReport
import com.testapp.weathersnap.data.local.WeatherReportDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SavedReportsViewModel @Inject constructor(
    private val weatherReportDao: WeatherReportDao
) : ViewModel() {

    // Streams raw database models dynamically into a state architecture
    val uiState: StateFlow<SavedReportsUiState> = weatherReportDao.getAllReports()
        .map { reportList ->
            SavedReportsUiState(reports = reportList, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SavedReportsUiState()
        )
}