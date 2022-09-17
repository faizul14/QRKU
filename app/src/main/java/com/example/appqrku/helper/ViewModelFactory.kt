package com.example.appqrku.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appqrku.presentation.MainVIewModel
import com.example.appqrku.di.Injection
import com.example.appqrku.domain.UseCase
import java.lang.IllegalArgumentException

class ViewModelFactory(private val useCase : UseCase) : ViewModelProvider.NewInstanceFactory() {

    companion object{
        @Volatile
        var INSTANCE : ViewModelFactory? = null

        fun getInstance() : ViewModelFactory =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: ViewModelFactory(Injection.provideUseCase())
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return super.create(modelClass)
        if (modelClass.isAssignableFrom(MainVIewModel::class.java)){
            return MainVIewModel(useCase) as T
        }

        throw IllegalArgumentException("uknown viewmodel"+ modelClass.name)
    }
}