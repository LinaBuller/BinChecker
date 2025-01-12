package com.buller.binchecker.domain.repositories

import com.buller.binchecker.domain.utils.Result
import com.buller.binchecker.domain.models.BinInfo
import kotlinx.coroutines.flow.Flow

interface BinInfoRepository {
    fun getBinInfo(number: String): Flow<Result<BinInfo>>
}