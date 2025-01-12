package com.buller.binchecker.domain.usecases

import com.buller.binchecker.domain.utils.Result
import com.buller.binchecker.domain.repositories.BinInfoRepository
import com.buller.binchecker.domain.models.BinInfo
import kotlinx.coroutines.flow.Flow

class GetBinInfoUseCase(private val repository: BinInfoRepository) {
    fun invoke(number: String): Flow<Result<BinInfo>> {
        return repository.getBinInfo(number)
    }
}