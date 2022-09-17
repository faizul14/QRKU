package com.example.appqrku.presentation

import com.example.appqrku.domain.IteractorUseCase
import com.example.appqrku.domain.UseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val PresentationModule = module {
    factory <UseCase> {
        IteractorUseCase(get())
    }

    viewModel {
        MainVIewModel(get())
    }
}