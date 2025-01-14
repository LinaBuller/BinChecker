package com.buller.binchecker.domain.usecases

import com.buller.binchecker.domain.repositories.LocalRepository
import com.buller.binchecker.domain.models.BinInfo

class GetAllBinInfoFromDatabaseUseCase(private val repository: LocalRepository) {
    suspend fun invoke(): List<BinInfo> {
        return repository.getAllBinInfo()
    }
}