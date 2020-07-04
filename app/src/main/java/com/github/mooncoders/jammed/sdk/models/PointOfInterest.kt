package com.github.mooncoders.jammed.sdk.models

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * ogni provider ha:
 * - name
 * - providerLogo
 * - imageUrl
 * - timelapseUrl
 * - liveUrl
 *
 * ciascun provider concorre al crowd indicator, ma solo quello reputato "best" avrà i suoi URL
 * restituiti nella risposta che Jammed leggerà dal server, perché la liveURL aprirà una pagina web
 * del provider sia per vedere il liveURL, che per poter fare proximity marketing.
 */
data class PointOfInterest(
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val squareMeters: Double, // valore nominale secondo planimetria
    val advertising: Advertising,
    val provider: Provider,
    val crowdIndicator: CrowdIndicator
) {
    fun marker() = MarkerOptions().position(LatLng(latitude, longitude)).title(title)
}

data class Advertising(
    val brand: String,
    val iconUrl: String,
    val headline: String
)

data class Provider(
    val name: String,
    val imageUrl: String,
    val timelapseUrl: String,
    val liveUrl: String
)