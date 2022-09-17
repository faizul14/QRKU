package com.example.appqrku.data

import com.example.appqrku.domain.EntityResult

class DataSource() : IDataSource {
    override fun getResultData(result: String): EntityResult {
        return EntityResult(
            result
        )
    }
}