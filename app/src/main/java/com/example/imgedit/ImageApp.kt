package com.example.imgedit

import android.app.Application
import com.example.imgedit.di.AppComponent
import com.example.imgedit.di.ContextModule
import com.example.imgedit.di.DaggerAppComponent

class ImageApp : Application() {
    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component =
            DaggerAppComponent.builder().contextModule(ContextModule(this.applicationContext))
                .build()
    }
}