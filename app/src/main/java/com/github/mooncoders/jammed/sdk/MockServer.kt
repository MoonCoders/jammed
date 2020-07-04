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
                    title = "Place 1",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 10.0,
                    image = "",
                    video = "",
                    crowdIndicator = CrowdIndicator.High
                )
            },
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Place 2",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 20.0,
                    image = "",
                    video = "",
                    crowdIndicator = CrowdIndicator.Low
                )
            },
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Place 3",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 30.0,
                    image = "",
                    video = "",
                    crowdIndicator = CrowdIndicator.Low
                )
            },
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Place 4",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 40.0,
                    image = "",
                    video = "",
                    crowdIndicator = CrowdIndicator.Medium
                )
            }
        )
    }
}