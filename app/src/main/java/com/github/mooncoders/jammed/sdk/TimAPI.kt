package com.github.mooncoders.jammed.sdk

import com.github.mooncoders.jammed.sdk.apimodels.PedestrianDetectionResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TimAPI {
    @POST
    suspend fun pedestrianDetection(@Body data: ByteArray): PedestrianDetectionResponse
}