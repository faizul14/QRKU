package com.example.appqrku.data.dataLayer

import com.example.appqrku.data.DataSource
import com.example.appqrku.data.IDataSource
import com.example.appqrku.data.Repository
import com.example.appqrku.domain.IRepository
import org.koin.dsl.module

val DataModule = module{
    factory <IDataSource> {
        DataSource()
    }

    factory <IRepository> {
        Repository(get())
    }


}