package com.example.myalarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat

class MyBroadcastReceiver: BroadcastReceiver() {
    val TAG = "MyBroadcastReceiver"
    val localNotiChannelId = "2000"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            return;
        }
        Log.i(TAG,">>> onReceive")
        val value = intent.getIntExtra("int", 0)
        Log.i(TAG,"value = $value")

        showLocalNotification(context, "title", "message")
    }
    private fun showLocalNotification(context: Context, title: String, messageBody: String) {
        Log.i(TAG,"messageBody = $messageBody")
        val requestCode = 0
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, localNotiChannelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        val channel = NotificationChannel(
            localNotiChannelId,
            "remote notification",
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        notificationManager.createNotificationChannel(channel)

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}