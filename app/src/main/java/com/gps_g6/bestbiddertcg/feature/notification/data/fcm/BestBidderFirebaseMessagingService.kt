package com.gps_g6.bestbiddertcg.feature.notification.data.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gps_g6.bestbiddertcg.core.network.NetworkModule
import com.gps_g6.bestbiddertcg.feature.notification.presentation.NotificationChannelManager
import com.gps_g6.bestbiddertcg.feature.notification.presentation.NotificationDisplayer
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class BestBidderFirebaseMessagingService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "FCM token generated or renewed")

        NetworkModule.initialize(applicationContext)

        if (!NetworkModule.sessionManager.isAuthenticated()) {
            Log.d(TAG, "No active session; token will be registered after login")
            return
        }

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                FcmTokenManager().registerToken(token)

                Log.d(TAG, "Renewed FCM token registered successfully")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Unable to register renewed FCM token", e)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d(TAG, "FCM message received: ${message.messageId}")

        NotificationChannelManager.createChannel(this)

        val title = message.notification?.title
            ?: message.data["title"]

        val body = message.notification?.body
            ?: message.data["body"]

        if (!title.isNullOrBlank() && !body.isNullOrBlank()) {
            NotificationDisplayer.show(
                context = this,
                title = title,
                body = body
            )
        }
    }

    companion object {
        private const val TAG = "BestBidderFCM"
    }
}
