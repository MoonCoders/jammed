package com.github.mooncoders.jammed.sdk

import com.github.mooncoders.jammed.sdk.models.CrowdIndicator
import com.github.mooncoders.jammed.sdk.models.PointOfInterest
import com.github.mooncoders.jammed.sdk.models.PointsOfInterestParams
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MockServer {

    private fun getRandomLocation(params: PointsOfInterestParams): Pair<Double, Double> {
        val r = params.radius * sqrt(Math.random())
        val theta = Math.random() * 2 * Math.PI

        val latitude = params.latitude + r * cos(theta)
        val longitude = params.longitude + r * sin(theta)

        return Pair(latitude, longitude)
    }

    private fun gitHubRaw(file: String) =
        "https://raw.githubusercontent.com/MoonCoders/resources/master/$file"

    fun getPointsOfInterest(params: PointsOfInterestParams): List<PointOfInterest> {
        return listOf(
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Punta Celesi - Palermo",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 10.0,
                    imageUrl = gitHubRaw("punta_celesi.jpg"),
                    timelapseUrl = gitHubRaw("punta_celesi.mp4"),
                    liveUrl = "https://www.skylinewebcams.com/en/webcam/italia/sicilia/palermo/punta-celesi.html",
                    crowdIndicator = CrowdIndicator.High
                )
            },
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Piazza del Duomo - Firenze",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 20.0,
                    imageUrl = gitHubRaw("duomo_firenze.jpg"),
                    timelapseUrl = gitHubRaw("duomo_firenze.mp4"),
                    liveUrl = "https://www.skylinewebcams.com/en/webcam/italia/toscana/firenze/piazza-duomo-firenze.html",
                    crowdIndicator = CrowdIndicator.Low
                )
            },
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Fontana di Trevi - Roma",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 30.0,
                    imageUrl = gitHubRaw("fontana_trevi.jpg"),
                    timelapseUrl = gitHubRaw("fontana_trevi.mp4"),
                    liveUrl = "https://www.skylinewebcams.com/en/webcam/italia/lazio/roma/fontana-di-trevi.html",
                    crowdIndicator = CrowdIndicator.Low
                )
            },
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Piazza del Duomo - Amalfi",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 40.0,
                    imageUrl = gitHubRaw("amalfi.jpg"),
                    timelapseUrl = gitHubRaw("amalfi.mp4"),
                    liveUrl = "https://leganavaleamalfi.it/webcams/#live",
                    crowdIndicator = CrowdIndicator.Medium
                )
            }
        )
    }
}