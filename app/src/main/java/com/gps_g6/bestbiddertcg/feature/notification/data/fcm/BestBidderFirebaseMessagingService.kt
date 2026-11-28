package com.gps_g6.bestbiddertcg.feature.notification.data.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class BestBidderFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "FCM token renewed")

        // TODO: Sincronizar el nuevo token con backend
        // cuando exista una sesión autenticada.
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d(TAG, "FCM message received: ${message.messageId}")

        // TODO: Interpretar el evento y mostrar la
        // notificación cuando corresponda.
    }

    companion object {
        private const val TAG = "BestBidderFCM"
    }
}
