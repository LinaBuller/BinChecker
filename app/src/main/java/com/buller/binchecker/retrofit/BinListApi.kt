package com.buller.binchecker.retrofit

import com.buller.binchecker.data.dto.BinInfoDto
import com.buller.binchecker.models.BinInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BinListApi {
    @GET("{bin}")
    suspend fun getBinInfo(@Path("bin") bin: String): BinInfoDto
}