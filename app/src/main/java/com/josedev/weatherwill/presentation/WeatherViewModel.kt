package com.josedev.weatherwill.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josedev.weatherwill.domain.location.LocationTracker
import com.josedev.weatherwill.domain.repository.WeatherRepository
import com.josedev.weatherwill.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
): ViewModel(){
    var state by mutableStateOf(WeatherState())
        private set // Only the ViewModel can change this state

    fun loadWeatherInfo() {
        /**
         * This will call the API
         */

        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            locationTracker.getCurrentLocation()?.let { location ->
                when(val result =repository.getWeatherData(lat = location.latitude, lon = location.longitude)){
                    is Resource.Success -> {
                        state = state.copy(
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } ?: kotlin.run {
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location, please check to grant locations permissions and enable GPS"
                )
            }
        }
    }
}