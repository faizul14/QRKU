package com.example.appqrku.data

import com.example.appqrku.domain.EntityResult
import com.example.appqrku.domain.IRepository

class Repository(val dataSource: IDataSource) : IRepository {
    override fun getResult(result: String): EntityResult {
        return dataSource.getResultData(result)
    }
}