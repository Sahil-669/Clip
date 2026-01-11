package com.example.clip

import com.example.clip.di.appModule
import org.koin.core.context.startKoin

fun doInitKoin() {
    startKoin {
        modules(appModule)
    }
}