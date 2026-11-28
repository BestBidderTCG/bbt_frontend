package com.gps_g6.bestbiddertcg.feature.notification.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.gps_g6.bestbiddertcg.R

object NotificationChannelManager {

    const val CHANNEL_ID = "bestbidder_notifications"

    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(
                R.string.notification_channel_description
            )
        }

        val manager = context.getSystemService(
            NotificationManager::class.java
        )

        manager.createNotificationChannel(channel)
    }
}
