package com.github.mooncoders.jammed.sdk

import com.github.mooncoders.jammed.sdk.models.*
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
                    crowdIndicator = CrowdIndicator.High,
                    provider = Provider(
                        name = "Skyline",
                        imageUrl = gitHubRaw("punta_celesi.jpg"),
                        timelapseUrl = gitHubRaw("punta_celesi.mp4"),
                        liveUrl = "https://www.skylinewebcams.com/en/webcam/italia/sicilia/palermo/punta-celesi.html"
                    ),
                    advertising = Advertising(
                        brand = "Kelly Cannoli",
                        iconUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTSTt4uzOUMnvBIOJE_9YXNZCzGbL5RbFoUxA&usqp=CAU",
                        headline = "La tradizione palermitana"
                    )
                )
            },
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Piazza del Duomo - Firenze",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 20.0,
                    crowdIndicator = CrowdIndicator.Low,
                    provider = Provider(
                        name = "Skyline",
                        imageUrl = gitHubRaw("duomo_firenze.jpg"),
                        timelapseUrl = gitHubRaw("duomo_firenze.mp4"),
                        liveUrl = "https://www.skylinewebcams.com/en/webcam/italia/toscana/firenze/piazza-duomo-firenze.html"
                    ),
                    advertising = Advertising(
                        brand = "Antica Osteria",
                        iconUrl = "https://www.ondesign.de/files/website-elemente/Projekte/slideshow-logos/portfolio-slideshow-logo-23.jpg",
                        headline = "Le fiorentine gustose"
                    )
                )
            },
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Fontana di Trevi - Roma",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 30.0,
                    crowdIndicator = CrowdIndicator.Low,
                    provider = Provider(
                        name = "Skyline",
                        imageUrl = gitHubRaw("fontana_trevi.jpg"),
                        timelapseUrl = gitHubRaw("fontana_trevi.mp4"),
                        liveUrl = "https://www.skylinewebcams.com/en/webcam/italia/lazio/roma/fontana-di-trevi.html"
                    ),
                    advertising = Advertising(
                        brand = "SuperTech",
                        iconUrl = "https://icon-library.com/images/small-camera-icon/small-camera-icon-1.jpg",
                        headline = "Scatta anche tu splendide foto"
                    )
                )
            },
            getRandomLocation(params).let {
                PointOfInterest(
                    title = "Piazza del Duomo - Amalfi",
                    latitude = it.first,
                    longitude = it.second,
                    squareMeters = 40.0,
                    crowdIndicator = CrowdIndicator.Medium,
                    provider = Provider(
                        name = "Lega Navale",
                        imageUrl = gitHubRaw("amalfi.jpg"),
                        timelapseUrl = gitHubRaw("amalfi.mp4"),
                        liveUrl = "https://leganavaleamalfi.it/webcams/#live"
                    ),
                    advertising = Advertising(
                        brand = "Gelateria Scoop",
                        iconUrl = "https://cdn.dribbble.com/users/3659972/screenshots/6698848/scoooop.jpg",
                        headline = "Gusta il tuo gelato con la vista mozzafiato"
                    )
                )
            }
        )
    }
}