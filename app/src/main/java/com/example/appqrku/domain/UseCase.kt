package com.example.appqrku.domain

interface UseCase {
    fun getResult(result: String) : EntityResult
}