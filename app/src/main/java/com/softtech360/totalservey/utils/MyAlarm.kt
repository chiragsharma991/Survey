package com.softtech360.totalservey.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.softtech360.totalservey.R
import com.softtech360.totalservey.activity.Login


class MyAlarm : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {


        Log.e("MyAlarm", "onReceive>>>>>>>>>>")


        val resultIntent = Intent(context, Login::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val resultPendingIntent = PendingIntent.getActivity(context,
                0, resultIntent,
                PendingIntent.FLAG_CANCEL_CURRENT)

        val icon1 = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)

        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText("Big text title")
        bigText.setBigContentTitle("Big content title")
        //bigText.setSummaryText("");

        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "UserAvailablity"
        val notification: Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, "UserAvailablity", NotificationManager.IMPORTANCE_DEFAULT)

            // Configure the notification channel.
            notificationChannel.description = "Make sound and pop screen"
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableLights(true)
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            mNotificationManager.createNotificationChannel(notificationChannel)

        }
        notification = NotificationCompat.Builder(context, channelId)
                .setVibrate(longArrayOf(0, 100, 100, 100, 100, 100))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setCategory(Notification.CATEGORY_PROMO)
                .setContentTitle("Application's user availablity is missing")
                .setContentText("System software is not capable to store data for a long days. Please Tap and fix your data")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setLargeIcon(icon1)
                .setAutoCancel(true)
                //.setStyle(bigText)
                .setContentIntent(resultPendingIntent)
                .setPriority(if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O) NotificationManager.IMPORTANCE_HIGH else Notification.PRIORITY_HIGH)
                .build()

        mNotificationManager.notify(System.currentTimeMillis().toInt(), notification)


    }

}