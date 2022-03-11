package com.sabsrocambole.myapplicationo.Controller

import android.app.Application
import com.sabsrocambole.myapplicationo.Utilities.SharedPrefs

class App : Application(){

    companion object{
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}