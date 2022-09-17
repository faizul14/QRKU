package com.example.appqrku.di

import com.example.appqrku.data.DataSource
import com.example.appqrku.data.IDataSource
import com.example.appqrku.data.Repository
import com.example.appqrku.domain.IRepository
import com.example.appqrku.domain.IteractorUseCase
import com.example.appqrku.domain.UseCase

object Injection {
    fun provideUseCase() : UseCase{
        val nameRepository = provideRepository()
        return IteractorUseCase(provideRepository())
    }

    private fun provideRepository() : IRepository{
        val nameDataSource = provideDataSource()
        return Repository(nameDataSource)
    }

    private fun provideDataSource() : IDataSource{
        return DataSource()
    }
}