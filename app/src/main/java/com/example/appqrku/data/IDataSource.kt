package com.example.appqrku.data

import com.example.appqrku.domain.EntityResult

interface IDataSource {
    fun getResultData(result: String) : EntityResult
}