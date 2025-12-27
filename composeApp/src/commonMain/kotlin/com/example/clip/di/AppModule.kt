package com.example.clip.di

import com.example.clip.data.AppDatabase
import com.example.clip.presentation.ClipViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule = module {

    single { get<AppDatabase>().ClipDao() }
    single { ClipViewModel(get(), get()) }
}
expect val platformModule: Module