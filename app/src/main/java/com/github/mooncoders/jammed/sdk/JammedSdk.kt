package com.github.mooncoders.jammed.sdk

import android.app.Application
import android.location.Location
import com.github.mooncoders.jammed.BuildConfig
import com.github.mooncoders.jammed.sdk.helpers.Iso8601DateTimeTypeAdapter
import com.github.mooncoders.jammed.sdk.helpers.UserAgentInterceptor
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.moshi.Moshi
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

    private val client = Retrofit.Builder()
        .client(
            httpClient.newBuilder()
                .addNetworkInterceptor(
                    UserAgentInterceptor(
                        "jammed/${BuildConfig.VERSION_NAME}"
                    )
                ).build()
        )
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(TimAPI::class.java)

    private val server = MockServer()

    suspend fun getNumberOfPedestrians(pointOfInterest: PointOfInterest): Int {
        return when {
            (pointOfInterest.image == "spiaggia_affollata.jpg") -> {
                15
            }
            else -> 0
        }
    }

    suspend fun getPointsOfInterest(params: PointsOfInterestParams): List<PointOfInterest> {
        return server.getPointsOfInterest(params)
    }
}