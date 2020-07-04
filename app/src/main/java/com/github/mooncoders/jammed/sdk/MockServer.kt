package com.github.mooncoders.jammed.sdk

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

    fun getPointsOfInterest(params: PointsOfInterestParams): List<PointOfInterest> {
        return listOf(
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Punta Celesi - Palermo",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 10.0,
                    image = "",
                    video = "https://github.com/MoonCoders/resources/blob/master/punta_celesi.mp4?raw=true",
                    crowdIndicator = CrowdIndicator.High
                )
            },
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Piazza del Duomo - Firenze",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 20.0,
                    image = "",
                    video = "https://github.com/MoonCoders/resources/blob/master/duomo_firenze.mp4?raw=true",
                    crowdIndicator = CrowdIndicator.Low
                )
            },
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Fontana di Trevi - Roma",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 30.0,
                    image = "",
                    video = "https://github.com/MoonCoders/resources/blob/master/fontana_trevi.mp4?raw=true",
                    crowdIndicator = CrowdIndicator.Low
                )
            },
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Piazza del Duomo - Amalfi",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 40.0,
                    image = "",
                    video = "https://github.com/MoonCoders/resources/blob/master/amalfi.mp4?raw=true",
                    crowdIndicator = CrowdIndicator.Medium
                )
            }
        )
    }
}