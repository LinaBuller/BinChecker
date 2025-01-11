package com.buller.binchecker.domain.repositories

import com.buller.binchecker.domain.Result
import com.buller.binchecker.models.BinInfo
import kotlinx.coroutines.flow.Flow

interface BinRepository {
    fun getBinInfo(number: String): Flow<Result<BinInfo>>
}