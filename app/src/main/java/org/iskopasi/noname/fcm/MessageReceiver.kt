package org.iskopasi.noname.fcm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.iskopasi.noname.MainActivity
import org.iskopasi.noname.R
import org.iskopasi.noname.fcm.MessageReceiver.STATICS.NOTIFICATION_ID
import org.iskopasi.noname.fcm.MessageReceiver.STATICS.REQUEST_CODE


/**
 * Created by cora32 on 24.12.2017.
 */
class MessageReceiver : FirebaseMessagingService() {
    object STATICS {
        const val REQUEST_CODE = 101
        const val NOTIFICATION_ID = 101
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["body"]
        val notifTitle = remoteMessage.notification?.title
        val notifMessage = remoteMessage.notification?.body

        showNotifications(title + " " + notifTitle, message + " " + notifMessage)
    }

    private fun showNotifications(title: String?, message: String?) {
        Log.e("FCM_nnm", "Message here!: $title $message")
        val i = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE,
                i, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, "iskopasi_noname_channel")
                .setContentText(message)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}