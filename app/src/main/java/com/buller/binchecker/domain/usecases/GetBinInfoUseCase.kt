package com.buller.binchecker.domain.usecases

import android.util.Log
import com.buller.binchecker.domain.Result
import com.buller.binchecker.domain.repositories.BinRepository
import com.buller.binchecker.models.BinInfo
import kotlinx.coroutines.flow.Flow

class GetBinInfoUseCase(private val repository: BinRepository) {
    fun invoke(number: String): Flow<Result<BinInfo>> {
        Log.d("MyLog", "usecase getBin")
        return repository.getBinInfo(number)
    }
}