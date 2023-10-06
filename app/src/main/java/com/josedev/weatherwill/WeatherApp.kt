package com.josedev.weatherwill

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp: Application() {
    /**
     * To inject dependencies, it is also set in the manifest through:
     * android:name tag inside application
     */
}