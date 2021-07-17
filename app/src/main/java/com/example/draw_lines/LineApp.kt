package com.example.draw_lines

import android.app.Application
import com.example.draw_lines.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LineApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LineApp)
            modules(repositoryModule)
        }
    }
}
