package com.josedev.weatherwill.widget


import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartService
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.josedev.weatherwill.data.repository.WeatherRepositoryImpl
import com.josedev.weatherwill.di.AppModule
import com.josedev.weatherwill.domain.location.LocationTracker
import com.josedev.weatherwill.presentation.MainActivity
import com.josedev.weatherwill.presentation.WeatherViewModel
import com.josedev.weatherwill.presentation.ui.theme.DarkBlue
import javax.inject.Inject


object WeatherWidget : GlanceAppWidget() {

    val weatherKey = intPreferencesKey("weather_key_int")
    val weatherKeyDouble = doublePreferencesKey("weather_key_double")
    val weatherKeyString = stringPreferencesKey("weather_key_string")

    @Composable
    override fun Content() {
        val state = currentState(key = weatherKey) ?: 0
        Box (
            modifier = GlanceModifier
                .cornerRadius(8.dp)
                .fillMaxSize()
                .background(DarkBlue)

        ){
            Column(
                modifier = GlanceModifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Weather !",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(Color.White),
                        fontSize = 26.sp
                    )
                )

                Text(
                    text = "${state} °C",
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 18.sp
                    ))

                Button(
                    text = "Count",
                    onClick = actionRunCallback(UpdateActionCallback::class.java)
//                onClick = actionStartActivity<MainActivity>()
//                onClick = actionStartService<WeatherRepositoryImpl>()
                )
            }
        }
        }


}

class SimpleWeatherWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = WeatherWidget

}

class UpdateActionCallback: ActionCallback{
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {

        updateAppWidgetState(context, glanceId){ prefs ->
            val current = prefs[WeatherWidget.weatherKey]
            if(current  != null){
                prefs[WeatherWidget.weatherKey] = current + 1
            } else {
                prefs[WeatherWidget.weatherKey] = 1
            }
        }

        WeatherWidget.update(context, glanceId)
    }

}