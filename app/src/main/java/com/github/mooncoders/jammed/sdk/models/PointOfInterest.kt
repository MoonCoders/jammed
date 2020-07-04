package com.github.mooncoders.jammed.sdk.models

import com.github.mooncoders.jammed.sdk.models.CrowdIndicator
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

data class PointOfInterest(
    val latitude: Double,
    val longitude: Double,
    val squareMeters: Double,
    val title: String,
    val image: String,
    val video: String,
    val crowdIndicator: CrowdIndicator
) {
    fun marker() = MarkerOptions().position(LatLng(latitude, longitude)).title(title)
}