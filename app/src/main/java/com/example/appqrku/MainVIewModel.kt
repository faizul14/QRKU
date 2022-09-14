package com.example.appqrku

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainVIewModel : ViewModel() {

    private val _result = MutableLiveData<String>()
    val result : LiveData<String> =_result


    fun setData(msg : String){
       if (msg != null){
           _result.value = msg
       }
    }
}