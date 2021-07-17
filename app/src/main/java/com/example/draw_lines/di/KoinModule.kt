package com.example.draw_lines.di

import com.example.draw_lines.data.db.LineDatabase
import com.example.draw_lines.data.repository.LineRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single {
        LineRepository(
            LineDatabase.getDatabase(androidContext()).lineDao()
        )
    }
}
