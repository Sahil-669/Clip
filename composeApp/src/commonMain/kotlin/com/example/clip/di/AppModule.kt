package com.example.clip.di

import com.example.clip.data.AppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule = module {

    single { get<AppDatabase>().ClipDao() }
}
expect val platformModule: Module