package com.francotte.weatherapp.glance

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_LOCKED_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                WeatherRefreshWorker.schedulePeriodic(context, repeatHours = 3)
                WeatherRefreshWorker.enqueueAll(context)
            }
        }
    }
}