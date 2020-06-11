package com.example.salud.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.salud.NuevoMedicamentoActivity
import com.example.salud.R

class PlanificarAlarmas: BroadcastReceiver() {

    var mNotificationManager: NotificationManager? = null
    // Notification ID.
    val NOTIFICATION_ID = 0
    // Notification channel ID.
    val PRIMARY_CHANNEL_ID = "primary_notification_channel"

    /**
     * Called when the BroadcastReceiver receives an Intent broadcast.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    override fun onReceive(context: Context, intent: Intent?) {
        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Deliver the notification.
        deliverNotification(context)
    }

    /**
     * Builds and delivers the notification.
     *
     * @param context, activity context.
     */
    fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, NuevoMedicamentoActivity::class.java)

        val contentPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_access_alarms)
            .setContentTitle("Stand Up Alert")
            .setContentText("You should stand up and walk around now!")
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        mNotificationManager?.notify(NOTIFICATION_ID, builder.build())
    }

}