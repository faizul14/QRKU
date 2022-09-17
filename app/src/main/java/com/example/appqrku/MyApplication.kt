package com.example.appqrku

import android.app.Application
import com.example.appqrku.data.dataLayer.DataModule
import com.example.appqrku.presentation.PresentationModule
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

open class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            modules(
                listOf(
                    DataModule,
                    PresentationModule
                )
            )
        }
    }
}