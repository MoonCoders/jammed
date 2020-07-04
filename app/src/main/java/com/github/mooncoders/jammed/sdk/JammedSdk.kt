package com.github.mooncoders.jammed.sdk

import android.app.Application
import com.github.mooncoders.jammed.BuildConfig
import com.github.mooncoders.jammed.R
import com.github.mooncoders.jammed.sdk.helpers.ApiKeyInterceptor
import com.github.mooncoders.jammed.sdk.helpers.Iso8601DateTimeTypeAdapter
import com.github.mooncoders.jammed.sdk.helpers.UserAgentInterceptor
import com.github.mooncoders.jammed.sdk.models.PointOfInterest
import com.github.mooncoders.jammed.sdk.models.PointsOfInterestParams
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class JammedSdk(
    application: Application,
    timBaseUrl: String,
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
                    UserAgentInterceptor("jammed/${BuildConfig.VERSION_NAME}")
                )
                .addNetworkInterceptor(
                    ApiKeyInterceptor(application.getString(R.string.tim_api_key))
                )
                .build()
        )
        .baseUrl(timBaseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(TimAPI::class.java)

    private val contentClient = Retrofit.Builder()
        .client(
            httpClient.newBuilder()
                .addNetworkInterceptor(
                    UserAgentInterceptor("jammed/${BuildConfig.VERSION_NAME}")
                )
                .build()
        )
        .baseUrl("https://dummy.url/")
        .build()
        .create(ContentAPI::class.java)

    private val server = MockServer()

    suspend fun getNumberOfPedestrians(pointOfInterest: PointOfInterest): Int {
        return when {
            (pointOfInterest.imageUrl == "spiaggia_affollata.jpg") -> {
                15
            }
            else -> 0
        }
    }

    suspend fun getPointsOfInterest(params: PointsOfInterestParams): List<PointOfInterest> {
        return server.getPointsOfInterest(params)
    }

    suspend fun countPedestrians(imageUrl: String) : Int {
        val imageData = contentClient.getData(imageUrl).bytes()
        return client.pedestrianDetection(imageData).people?.size ?: -1
    }
}