package com.github.mooncoders.jammed.sdk.apimodels

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