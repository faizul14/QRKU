package com.example.appqrku.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appqrku.domain.EntityResult
import com.example.appqrku.domain.UseCase

class MainVIewModel(val useCase: UseCase) : ViewModel() {

    private val _result = MutableLiveData<EntityResult>()
    val result : LiveData<EntityResult> =_result


    fun setData(msg : String){
//       if (msg != null){
//           _result.value = msg
//       }

        if (msg != null){
            _result.value = useCase.getResult(msg)
        }
    }

}