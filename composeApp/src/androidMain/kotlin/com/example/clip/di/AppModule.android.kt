package com.example.clip.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.clip.data.AndroidClipboardSource
import com.example.clip.data.AppDatabase
import com.example.clip.data.ClipboardSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<AppDatabase> {
        val dbFile = androidContext().getDatabasePath("clip.db")

        Room.databaseBuilder<AppDatabase>(
            context = androidContext(),
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single<ClipboardSource> { AndroidClipboardSource(androidContext()) }
}