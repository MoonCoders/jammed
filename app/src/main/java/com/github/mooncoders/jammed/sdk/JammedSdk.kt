package com.github.mooncoders.jammed.sdk

import android.app.Application
import android.util.Log
import com.github.mooncoders.jammed.BuildConfig
import com.github.mooncoders.jammed.R
import com.github.mooncoders.jammed.sdk.apimodels.SmsPayload
import com.github.mooncoders.jammed.sdk.extensions.parallelMap
import com.github.mooncoders.jammed.sdk.helpers.ApiKeyInterceptor
import com.github.mooncoders.jammed.sdk.helpers.Iso8601DateTimeTypeAdapter
import com.github.mooncoders.jammed.sdk.helpers.UserAgentInterceptor
import com.github.mooncoders.jammed.sdk.models.CrowdIndicator
import com.github.mooncoders.jammed.sdk.models.PointOfInterest
import com.github.mooncoders.jammed.sdk.models.PointsOfInterestParams
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.moshi.Moshi
import kotlinx.coroutines.delay
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class JammedSdk(
    application: Application,
    timBaseUrl: String,
    httpClient: OkHttpClient = OkHttpClient()
) {
    private val areaPerPerson = 4 // square meters

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

    suspend fun sendSms(target: String, message: String) {
        client.sendSms(
            SmsPayload(
                address = "tel:+39$target",
                message = message
            )
        )
    }

    suspend fun getPointsOfInterest(params: PointsOfInterestParams): List<PointOfInterest> {
        val pointsOfInterest = server.getPointsOfInterest(params)

        // Parallel calls not possible because of rate limiting -_-
        /*return pointsOfInterest.parallelMap {
            val peopleCount = countPedestrians(it.provider.imageUrl)
            it.copy(crowdIndicator = affluence(it.squareMeters, areaPerPerson, peopleCount))
        }*/

        // Applying rate limiting constraints
        return pointsOfInterest.map {
            val peopleCount = countPedestrians(it.provider.imageUrl)
            val affluence = affluence(it.squareMeters, areaPerPerson, peopleCount)
            delay(1100)
            Log.e("PEDESTRIANS", "Got $peopleCount in ${it.title}, ${it.address}. Affluence: $affluence")
            it.copy(crowdIndicator = affluence)
        }
    }

    private fun affluence(
        totalSquareMeters: Double,
        areaPerPerson: Int,
        people: Int
    ): CrowdIndicator {
        val slots = totalSquareMeters / areaPerPerson
        val affluence = people.toDouble() / slots

        return when {
            affluence <= 0.35 -> CrowdIndicator.Low
            affluence > 0.35 && affluence <= 0.75 -> CrowdIndicator.Medium
            else -> CrowdIndicator.High
        }
    }

    private suspend fun countPedestrians(imageUrl: String): Int {
        val imageData = contentClient.getData(imageUrl).bytes()
        val response = client.pedestrianDetection(
            imageData.toRequestBody(
                "image/*".toMediaTypeOrNull(),
                0,
                imageData.size
            )
        )
        return response.people?.size ?: -1
    }
}