package com.francotte.weatherapp.glance

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.francotte.weatherapp.domain.UserDataRepository
import com.francotte.weatherapp.domain.WeatherRepository
import com.francotte.weatherapp.util.WeatherType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit


@HiltWorker
class WeatherRefreshWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(appContext, params) {


    override suspend fun doWork(): Result {
        return try {
            val deps = applicationContext.workerDeps()
            val weatherRepository = deps.weatherRepository()
            val userDataRepository = deps.userDataRepository()

            Log.d("WeatherWM", "doWork() start, inputData=$inputData")

            val manager = GlanceAppWidgetManager(applicationContext)
            val glanceIds = run {
                val appWidgetId = inputData.getInt(KEY_APP_WIDGET_ID, -1)
                if (appWidgetId != -1) {
                    manager.getGlanceIdBy(appWidgetId)?.let { listOf(it) } ?: emptyList()
                } else {
                    manager.getGlanceIds(WeatherWidget::class.java)
                }
            }
            Log.d("WeatherWM", "glanceIds count=${glanceIds.size}")
            if (glanceIds.isEmpty()) return Result.success()

            val userPrefs = userDataRepository.userData.first()
            val savedCity = userPrefs.userSavedCities?.firstOrNull()
            if (savedCity == null) {
                Log.d("WeatherWM", "No saved city in Proto")
                return Result.success()
            }

            Log.d("WeatherWM", "City=${savedCity.name} lat=${savedCity.latitude} lon=${savedCity.longitude}")

            val current = weatherRepository.getCurrentWeatherData(savedCity.latitude, savedCity.longitude).firstOrNull()
            Log.d("WeatherWM", "Current result isNull=${current==null}")
            if (current == null) return Result.retry()
            val weatherType = current.weatherType
            val temp = current.temperatureCelsius?.toString() ?: "—"
            val descResId = weatherType.weatherDesc           // @StringRes
            val descText = runCatching { applicationContext.getString(descResId) }.getOrDefault("—")

            val isDay = current.isDay
            val iconResId = when {
                weatherType == WeatherType.ClearSky && isDay -> weatherType.iconForDay
                weatherType == WeatherType.ClearSky && !isDay -> weatherType.iconForNight
                else -> weatherType.iconRes
            }

            glanceIds.forEach { glanceId ->
                updateAppWidgetState(applicationContext, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                    prefs.toMutablePreferences().apply {
                        this[WidgetPrefs.KEY_CITY] = savedCity.name
                        this[WidgetPrefs.KEY_TEMP] = temp
                        iconResId?.let { this[WidgetPrefs.KEY_ICON_ID] = it }
                        this[WidgetPrefs.KEY_IS_DAY] = isDay
                        this[WidgetPrefs.KEY_DESC] = descText
                    }
                }
                WeatherWidget().update(applicationContext, glanceId)
            }

            Log.d("WeatherWM", "doWork() success, updated=${glanceIds.size}")
            Result.success()
        } catch (t: Throwable) {
            Log.e("WeatherWM", "doWork() error", t)
            Result.retry()
        }
    }

    companion object {
        private const val UNIQUE_PERIODIC_NAME = "weather_widget_periodic"
        private const val UNIQUE_ONEOFF_PREFIX = "weather_widget_oneoff_"
        private const val KEY_APP_WIDGET_ID = "appWidgetId"

        fun enqueueNow(context: Context, appWidgetId: Int) {
            Log.d("debug_enqueue", "debug")
            val appCtx = context.applicationContext
            val work = OneTimeWorkRequestBuilder<WeatherRefreshWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(workDataOf(KEY_APP_WIDGET_ID to appWidgetId))
                .setConstraints(
                    androidx.work.Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(appCtx).enqueueUniqueWork(
                UNIQUE_ONEOFF_PREFIX + appWidgetId,
                ExistingWorkPolicy.APPEND,
                work
            )

            // Dump l’état pour vérifier qu’il est bien ENQUEUED/RUNNING/…
            WorkManager.getInstance(appCtx)
                .getWorkInfosForUniqueWork(UNIQUE_ONEOFF_PREFIX + appWidgetId)
                .get()
                .forEach { info ->
                    Log.d("WeatherWM", "work ${info.id} state=${info.state} tags=${info.tags}")
                }
        }

        fun enqueueAll(context: Context) {
            val appCtx = context.applicationContext
            val work = OneTimeWorkRequestBuilder<WeatherRefreshWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .addTag("weather_widget_on_leave")
                .build()

            WorkManager.getInstance(appCtx).enqueueUniqueWork(
                "weather_widget_on_leave",
                ExistingWorkPolicy.REPLACE, // 1 job à la fois
                work
            )
        }

        fun schedulePeriodic(context: Context, repeatHours: Long = 1L) {
            val work = PeriodicWorkRequestBuilder<WeatherRefreshWorker>(repeatHours, TimeUnit.HOURS)
                .setConstraints(
                    androidx.work.Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_PERIODIC_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                work
            )
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WorkerDepsEntryPoint {
    fun weatherRepository(): WeatherRepository
    fun userDataRepository(): UserDataRepository
}

fun Context.workerDeps(): WorkerDepsEntryPoint =
    EntryPointAccessors.fromApplication(applicationContext, WorkerDepsEntryPoint::class.java)