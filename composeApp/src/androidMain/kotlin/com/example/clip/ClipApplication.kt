package com.example.clip

import android.app.Application
import com.example.clip.di.appModule
import com.example.clip.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ClipApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ClipApplication)
            modules(appModule, platformModule)
        }
    }
}