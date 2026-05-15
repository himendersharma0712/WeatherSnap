package com.testapp.weathersnap.createReport

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testapp.weathersnap.data.local.WeatherReport
import com.testapp.weathersnap.data.local.WeatherReportDao
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val weatherReportDao: WeatherReportDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReportUiState())
    val uiState: StateFlow<CreateReportUiState> = _uiState.asStateFlow()

    private var rawPhotoFile: File? = null

    init {
        val cityName = savedStateHandle.get<String>("cityName") ?: "Unknown"
        val condition = savedStateHandle.get<String>("condition") ?: "Unknown"
        val temp = savedStateHandle.get<Float>("temp")?.toDouble() ?: 0.0
        val humidity = savedStateHandle.get<Int>("humidity") ?: 0
        val wind = savedStateHandle.get<Float>("wind")?.toDouble() ?: 0.0
        val pressure = savedStateHandle.get<Float>("pressure")?.toDouble() ?: 0.0

        _uiState.update {
            it.copy(
                cityName = cityName,
                condition = condition,
                temperature = temp,
                humidity = humidity,
                windSpeed = wind,
                pressure = pressure
            )
        }
    }

    fun onNotesChange(newNotes: String) {
        _uiState.update { it.copy(notes = newNotes) }
    }

    fun onPhotoCaptured(path: String, originalSize: Long, compressedSize: Long) {
        rawPhotoFile = File(path)
        _uiState.update {
            it.copy(
                photoPath = path,
                originalSizeText = "${originalSize / 1024} KB",
                compressedSizeText = "${compressedSize / 1024} KB"
            )
        }
    }

    fun saveReport(onSuccess: () -> Unit) {
        val currentState = _uiState.value

        if (currentState.photoPath == null) {
            _uiState.update { it.copy(error = "Please capture a photo first.") }
            return
        }

        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            try {

                val originalSize = rawPhotoFile?.length() ?: 0L
                var finalPhotoPath = currentState.photoPath
                var finalCompressedSize = originalSize

                val compressedFile = withContext(Dispatchers.IO) {
                    rawPhotoFile?.let { file ->
                        try {
                            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            val destinationFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
                            val outputStream = FileOutputStream(destinationFile)


                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                            outputStream.flush()
                            outputStream.close()
                            destinationFile
                        } catch (e: Exception) {
                            null
                        }
                    }
                }

                if (compressedFile != null && compressedFile.exists()) {
                    finalPhotoPath = compressedFile.absolutePath
                    finalCompressedSize = compressedFile.length()
                }


                val reportEntity = WeatherReport(
                    cityName = currentState.cityName,
                    temperature = currentState.temperature,
                    condition = currentState.condition,
                    humidity = currentState.humidity,
                    windSpeed = currentState.windSpeed,
                    pressure = currentState.pressure,
                    notes = currentState.notes,
                    photoPath = finalPhotoPath ?: "",
                    originalImageSize = originalSize,
                    compressedImageSize = finalCompressedSize
                )

                withContext(Dispatchers.IO) {
                    weatherReportDao.insertReport(reportEntity)


                    rawPhotoFile?.let { if (it.exists()) it.delete() }
                }

                _uiState.update { it.copy(isSaving = false) }


                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = "Database transaction failed: ${e.message}") }
            }
        }
    }
}