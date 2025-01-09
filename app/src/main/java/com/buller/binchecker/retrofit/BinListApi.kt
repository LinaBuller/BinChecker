package com.buller.binchecker.retrofit

import com.buller.binchecker.models.BinInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BinListApi {
    @GET("{bin}")
    fun getBinInfo(@Path("bin") bin: String): Call<BinInfo>
}