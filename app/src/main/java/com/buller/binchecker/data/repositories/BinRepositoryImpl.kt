package com.buller.binchecker.data.repositories

import android.util.Log
import com.buller.binchecker.data.mappers.toBinInfoDataMap
import com.buller.binchecker.domain.Result
import com.buller.binchecker.domain.repositories.BinRepository
import com.buller.binchecker.models.BinInfo
import com.buller.binchecker.retrofit.BinListApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class BinRepositoryImpl(private val api: BinListApi) : BinRepository {
    override fun getBinInfo(number: String): Flow<Result<BinInfo>> = flow {
        emit(Result.Loading)
        val data = api.getBinInfo(number)
        Log.d("MyLog", "${data.scheme} ${data.type} ${data.country?.name}")
        val binInfo = data.toBinInfoDataMap()
        emit(Result.Success(data = binInfo))
    }.catch { e ->
        Log.d("MyLog", "Error:${e.message}")
        emit(Result.Error(e))
    }
}