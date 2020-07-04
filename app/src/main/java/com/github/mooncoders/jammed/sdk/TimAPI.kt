package com.github.mooncoders.jammed.sdk

import com.github.mooncoders.jammed.sdk.apimodels.AirQualityResponse
import com.github.mooncoders.jammed.sdk.apimodels.PedestrianDetectionResponse
import com.github.mooncoders.jammed.sdk.apimodels.SmsPayload
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TimAPI {
    @POST("peddetect/detect")
    suspend fun pedestrianDetection(@Body data: RequestBody): PedestrianDetectionResponse

    @POST("sms/mt")
    suspend fun sendSms(@Body payload: SmsPayload)

    @GET("airquality/latest")
    suspend fun airQuality(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): AirQualityResponse
}