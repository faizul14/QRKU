package com.example.appqrku.domain

interface IRepository {
    fun getResult(result: String) : EntityResult
}