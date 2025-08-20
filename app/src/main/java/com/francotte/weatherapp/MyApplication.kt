package com.francotte.weatherapp

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import androidx.work.WorkManager
import com.francotte.weatherapp.glance.UnlockReceiver
import com.francotte.weatherapp.glance.WeatherRefreshWorker
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    private var unlockReceiver: UnlockReceiver? = null

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()


    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(this, workManagerConfiguration)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_USER_PRESENT)   // user vient de déverrouiller
            addAction(Intent.ACTION_SCREEN_ON)      // écran vient de s’allumer
        }
        unlockReceiver = UnlockReceiver { ctx ->
            WeatherRefreshWorker.enqueueAll(ctx)    // ← refresh tous les widgets
        }

        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(unlockReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("DEPRECATION")
            registerReceiver(unlockReceiver, filter)
        }

        // Widget: refresh quand l'app passe en arrière-plan
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                WeatherRefreshWorker.enqueueAll(applicationContext)
            }
            override fun onStop(owner: LifecycleOwner) {
                // L’app n’a plus d’Activity au premier plan → on refresh tous les widgets
                WeatherRefreshWorker.enqueueAll(applicationContext)
            }
        })

    }


}