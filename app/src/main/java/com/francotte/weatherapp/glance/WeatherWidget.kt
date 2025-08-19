package com.francotte.weatherapp.glance

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.IconImageProvider
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.currentState
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.TextDefaults
import androidx.glance.unit.ColorProvider
import com.francotte.weatherapp.ui.home.HomeActivity
import kotlin.jvm.java

class WeatherWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        WeatherRefreshWorker.enqueueNow(context, appWidgetId)

        // Lis des données locales (DataStore/Room) renseignées par ton worker
        provideContent {
            val prefs = currentState<Preferences>()
            val city = prefs[stringPreferencesKey("city")] ?: "—"
            val temp = prefs[stringPreferencesKey("temp")] ?: "—"
            val desc = prefs[stringPreferencesKey("desc")] ?: "—"
            val iconId = prefs[intPreferencesKey("iconId")]

            GlanceTheme {
                WeatherWidgetContent(city, temp, iconId, desc)
            }
        }
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(
            DpSize(260.dp, 48.dp),
            DpSize(260.dp, 110.dp)
        )
    )
}

@Composable
private fun WeatherWidgetContent(
    city: String,
    temp: String,
    iconId: Int?,
    desc: String
) {
    val ctx = LocalContext.current

    Row(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .clickable(onClick = actionStartActivity(Intent(ctx, HomeActivity::class.java))),
        verticalAlignment = androidx.glance.layout.Alignment.CenterVertically
    ) {
        Column(GlanceModifier.defaultWeight()) {
            Text(
                text = "$temp°",
                style = TextStyle(fontSize = 36.sp, color = GlanceTheme.colors.surface)
            )
            Spacer(GlanceModifier.height(4.dp))
            Text(
                text = city,
                style = TextStyle(fontSize = 16.sp, color = GlanceTheme.colors.surface)
            )
        }
        Column(GlanceModifier.defaultWeight(),horizontalAlignment = androidx.glance.layout.Alignment.End) {
                iconId?.let {
                    val canShow = runCatching { ctx.resources.getResourceName(it) }.isSuccess
                    if (canShow) {
                        Image(
                            provider = ImageProvider(resId = it),
                            contentDescription = null,
                            modifier = GlanceModifier.size(40.dp)
                        )
                        Spacer(GlanceModifier.height(6.dp))
                        Text(
                            text = desc,
                            style = TextStyle(fontSize = 16.sp, color = GlanceTheme.colors.surface)
                        )
                    }
                }
            }
    }
}
//Image(
//provider = ImageProvider(resId = android.R.drawable.ic_popup_sync),
//contentDescription = null,
//modifier = GlanceModifier
//.size(30.dp)
//.clickable(onClick = actionRunCallback<RefreshAction>())
//)

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        // Récupère l’appWidgetId pour identifier l’instance précise du widget
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(glanceId)
        Log.d("WeatherWM", "RefreshAction → appWidgetId=$appWidgetId")
        WeatherRefreshWorker.enqueueNow(context, appWidgetId)

        // Redessine immédiatement avec les données cache (optionnel)
        WeatherWidget().update(context, glanceId)
    }
}

class WeatherWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WeatherWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        WeatherRefreshWorker.schedulePeriodic(context, repeatHours = 1)
        WeatherRefreshWorker.enqueueAll(context)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        WeatherRefreshWorker.enqueueAll(context)
    }


}