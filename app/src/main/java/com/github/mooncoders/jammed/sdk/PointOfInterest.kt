package com.github.mooncoders.jammed.sdk

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

data class PointOfInterest(
    val latitude: Double,
    val longitude: Double,
    val squareMeters: Double,
    val title: String,
    val image: String,
    val video: String
) {
    fun marker() = MarkerOptions().position(LatLng(latitude, longitude)).title(title)
}