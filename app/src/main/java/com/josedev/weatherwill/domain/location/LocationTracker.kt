package com.josedev.weatherwill.domain.location

import android.location.Location

interface LocationTracker {
    suspend fun getLocation(): Location?
}