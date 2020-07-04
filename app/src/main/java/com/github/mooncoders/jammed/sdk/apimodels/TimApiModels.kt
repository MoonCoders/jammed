package com.github.mooncoders.jammed.sdk.apimodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonRectangle(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int,
    val score: Int
)

@JsonClass(generateAdapter = true)
data class PedestrianDetectionResponse(
    val error: String,
    val e_message: String,
    val people: List<PersonRectangle>?
)

@JsonClass(generateAdapter = true)
data class SmsPayload(
    val address: String,
    val message: String
)

@JsonClass(generateAdapter = true)
data class AirQualityResponse(
    val IQAValue: Double,
    val timestamp: Long,
    val pollutants: Pollutants
)

@JsonClass(generateAdapter = true)
data class Pollutants(
    val CO: Double,
    val SO2: Double,
    val O3: Double,
    val NO2: Double,
    val PM1: Double,
    val PM2_5: Double,
    val PM10: Double
)

enum class AirQualityIndex {
    Excellent,
    Good,
    Average,
    Poor,
    Bad,
    VeryBad
}

val AirQualityResponse.index: AirQualityIndex
    get() = when {
        IQAValue <= 50.0 -> AirQualityIndex.Excellent
        IQAValue > 50.0 && IQAValue <= 70 -> AirQualityIndex.Good
        IQAValue > 70 && IQAValue <= 100 -> AirQualityIndex.Average
        IQAValue > 100 && IQAValue <= 150 -> AirQualityIndex.Poor
        IQAValue > 150 && IQAValue <= 200 -> AirQualityIndex.Bad
        else -> AirQualityIndex.VeryBad
    }