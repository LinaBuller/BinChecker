package com.buller.binchecker.domain.retrofit

import com.buller.binchecker.data.remote.dto.BinInfoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface BinListApi {
    @GET("{bin}")
    suspend fun getBinInfo(@Path("bin") bin: String): BinInfoDto
}