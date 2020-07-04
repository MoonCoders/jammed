package com.github.mooncoders.jammed.sdk.apimodels

data class PersonRectangle(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int,
    val score: Int
)

data class PedestrianDetectionResponse(
    val error: Boolean,
    val e_message: String,
    val people: List<PersonRectangle>?
)