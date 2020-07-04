package com.github.mooncoders.jammed.sdk.extensions

import android.content.Context
import com.github.mooncoders.jammed.R
import com.github.mooncoders.jammed.sdk.models.CrowdIndicator
import com.github.mooncoders.jammed.sdk.models.PointOfInterest
import com.github.mooncoders.jammed.ui.foundation.vectorToBitmap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

fun PointOfInterest.marker(context: Context) = MarkerOptions().apply {
    position(LatLng(latitude, longitude))
    title(title)
    icon(customIcon(context))
}

private fun PointOfInterest.customIcon(context: Context) = context.vectorToBitmap(
    when (crowdIndicator) {
        CrowdIndicator.Low -> R.drawable.ic_marker_low
        CrowdIndicator.Medium -> R.drawable.ic_marker_mid
        CrowdIndicator.High -> R.drawable.ic_marker_high
    }
)