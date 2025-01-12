package com.buller.binchecker.domain.usecases

import com.buller.binchecker.domain.repositories.LocalRepository
import com.buller.binchecker.domain.models.BinInfo

class SetBinInfoToDatabaseUseCase(private val repository: LocalRepository) {
    suspend fun invoke(item: BinInfo){
        repository.setBinInfo(item)
    }
}