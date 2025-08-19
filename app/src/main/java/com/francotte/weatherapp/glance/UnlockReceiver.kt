package com.francotte.weatherapp.glance

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class UnlockReceiver(
    private val onUnlock: (Context) -> Unit
) : BroadcastReceiver() {

    private var lastEnqueueAt = 0L
    private val minIntervalMs = 10_000L // anti-spam

    override fun onReceive(context: Context, intent: Intent) {
        val now = System.currentTimeMillis()
        if (now - lastEnqueueAt < minIntervalMs) return
        lastEnqueueAt = now

        onUnlock(context.applicationContext)
    }
}

