package com.softtech360.totalservey.utils

import android.app.Application

class SurveyApp  : Application() {

    override fun onCreate() {
        super.onCreate()

        PreferenceUtil.init(this)

   //     registerActivityLifecycleCallbacks(AppLifecycleHandler.getInstance());


    }


}
