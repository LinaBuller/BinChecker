package com.buller.binchecker.data.di

import com.buller.binchecker.retrofit.BinListApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

val remoteModule = module {
    single {
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers = trustManagerFactory.trustManagers
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustManagers, null)
        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManagers[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }.build()

        Retrofit.Builder()
            .baseUrl("https://lookup.binlist.net/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<BinListApi> {
        get<Retrofit>().create(BinListApi::class.java)
    }
}