package com.github.mooncoders.jammed.sdk

import android.app.Application
import com.github.mooncoders.jammed.BuildConfig
import com.github.mooncoders.jammed.sdk.helpers.CacheMaxAgeInterceptor
import com.github.mooncoders.jammed.sdk.helpers.Iso8601DateTimeTypeAdapter
import com.github.mooncoders.jammed.sdk.helpers.OfflineCacheInterceptor
import com.github.mooncoders.jammed.sdk.helpers.UserAgentInterceptor
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.moshi.Moshi
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class JammedSdk(
    application: Application,
    baseUrl: String,
    httpClient: OkHttpClient = OkHttpClient()
) {
    private val moshi by lazy {
        Moshi.Builder()
            .add(Iso8601DateTimeTypeAdapter())
            .build()
    }

    init {
        AndroidThreeTen.init(application)
    }

    val client = Retrofit.Builder()
        .client(
            httpClient.newBuilder()
                .cache(Cache(application.cacheDir, 10 * 1024 * 1024))
                .addInterceptor(OfflineCacheInterceptor())
                .addNetworkInterceptor(CacheMaxAgeInterceptor())
                .addNetworkInterceptor(
                    UserAgentInterceptor(
                        "jammed/${BuildConfig.VERSION_NAME}"
                    )
                ).build()
        )
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(JammedAPI::class.java)
}