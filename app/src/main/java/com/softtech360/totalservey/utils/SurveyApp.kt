package com.softtech360.totalservey.utils

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import java.util.*

class SurveyApp  : Application(),LifeCycleDelegate {

    override fun onCreate() {
        super.onCreate()
        val lifeCycleHandler = AppLifecycleHandler(this)
        PreferenceUtil.init(this)
   //     registerActivityLifecycleCallbacks(AppLifecycleHandler.getInstance());
        registerLifecycleHandler(lifeCycleHandler)



    }

    override fun onAppBackgrounded() {
        Log.e("Awww", "App in background")

        if(PreferenceUtil.getBoolean(PreferenceUtil.ISLOGIN,false)){
            val cal = Calendar.getInstance()
            cal.add(Calendar.SECOND, 86400) //24 hour // 86400
            val alarmManager = getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
            val intent = Intent(applicationContext, MyAlarm::class.java)
            val pendingIntent = PendingIntent.getBroadcast(applicationContext,100,intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis , pendingIntent)
        }

    }

    override fun onAppForegrounded() {
        Log.e("Yeeey", "App in foreground")
        val intent = Intent(applicationContext, MyAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 100 , intent, PendingIntent.FLAG_NO_CREATE)
        if (pendingIntent != null){
            val alarmManager = getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun registerLifecycleHandler(lifeCycleHandler: AppLifecycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler)
        registerComponentCallbacks(lifeCycleHandler)
    }


/*    class ArchLifecycleApp : Application(), LifecycleObserver {

        override fun onCreate() {
            super.onCreate()
            PreferenceUtil.init(this)
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onAppBackgrounded() {
            Log.e("Awww__", "App in background")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onAppForegrounded() {
            Log.e("Yeeey___", "App in foreground")
        }


    }*/



    }
