package com.github.mooncoders.jammed.sdk

import com.github.mooncoders.jammed.sdk.apimodels.PedestrianDetectionResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface TimAPI {
    @POST("peddetect/detect")
    suspend fun pedestrianDetection(@Body data: RequestBody): PedestrianDetectionResponse
}