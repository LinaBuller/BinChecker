package com.buller.binchecker.domain.usecases

import com.buller.binchecker.domain.repositories.LocalRepository
import com.buller.binchecker.domain.models.BinInfo

class SetBinInfoToDatabaseUseCase(private val repository: LocalRepository) {
    suspend fun invoke(bin: String, item: BinInfo){
        repository.setBinInfo(bin,item)
    }
}