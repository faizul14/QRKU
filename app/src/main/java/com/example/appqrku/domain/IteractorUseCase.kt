package com.example.appqrku.domain

class IteractorUseCase(val repository: IRepository) : UseCase {
    override fun getResult(result: String): EntityResult {
        return repository.getResult(result)
    }
}