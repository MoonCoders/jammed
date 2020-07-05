package com.github.mooncoders.jammed.sdk

import com.github.mooncoders.jammed.sdk.models.*
import com.github.mooncoders.jammed.ui.foundation.randomEnum
import com.google.android.gms.maps.model.LatLng
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MockServer {

    private val pois = hashMapOf<LatLng, PointOfInterest>().apply {
        fontanaDiTrevi().also { put(LatLng(it.latitude, it.longitude), it) }
        gelateriaScoop().also { put(LatLng(it.latitude, it.longitude), it) }
        piazzaDuomo().also { put(LatLng(it.latitude, it.longitude), it) }
        puntaCelesi().also { put(LatLng(it.latitude, it.longitude), it) }
    }

    private fun getDistributedLocation(
        params: PointsOfInterestParams,
        distributeInRadius: Boolean = true
    ): Pair<Double, Double> {
        if (!distributeInRadius) return Pair(params.latitude, params.longitude)
        val r = params.radius * sqrt(Math.random())
        val theta = Math.random() * 2 * Math.PI

        val latitude = params.latitude + r * cos(theta)
        val longitude = params.longitude + r * sin(theta)

        return Pair(latitude, longitude)
    }

    fun getPointOfInterest(params: PointsOfInterestParams) =
        pois[LatLng(params.latitude, params.longitude)]!!

    private fun gitHubRaw(file: String) =
        "https://raw.githubusercontent.com/MoonCoders/resources/master/$file"

    fun getMockPointsOfInterest(params: PointsOfInterestParams): List<PointOfInterest> {
        return listOf(mockPoi(params),
            mockPoi(params),
            mockPoi(params),
            mockPoi(params),
            mockPoi(params),
            mockPoi(params))
    }

    fun mockPoi(params: PointsOfInterestParams): PointOfInterest {
        val location = getDistributedLocation(params)
        return PointOfInterest(
            title = "Punta Celesi",
            address = "Molo Punta Celesi, 90149 Palermo",
            latitude = location.first,
            longitude = location.second,
            squareMeters = 300.0,
            crowdIndicator = randomEnum(CrowdIndicator::class.java),
            categories = listOf(PlaceCategory.Beach),
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
            ),
            affluence = linkedMapOf(
                DayOfWeek.Monday to listOf(
                    CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
                ),
                DayOfWeek.Tuesday to listOf(
                    CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
                ),
                DayOfWeek.Wednesday to listOf(
                    CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
                ),
                DayOfWeek.Thursday to listOf(
                    CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
                ),
                DayOfWeek.Friday to listOf(
                    CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
                ),
                DayOfWeek.Saturday to listOf(
                    CrowdIndicator.Medium, CrowdIndicator.High, CrowdIndicator.Medium
                ),
                DayOfWeek.Sunday to listOf(
                    CrowdIndicator.Medium, CrowdIndicator.High, CrowdIndicator.Medium
                )
            )
        )
    }

    fun puntaCelesi() = PointOfInterest(
        title = "Punta Celesi",
        address = "Molo Punta Celesi, 90149 Palermo",
        latitude = 38.1974288,
        longitude = 13.3358272,
        squareMeters = 300.0,
        crowdIndicator = CrowdIndicator.Low,
        categories = listOf(PlaceCategory.Beach),
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
        ),
        affluence = linkedMapOf(
            DayOfWeek.Monday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Tuesday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Wednesday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Thursday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Friday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Saturday to listOf(
                CrowdIndicator.Medium, CrowdIndicator.High, CrowdIndicator.Medium
            ),
            DayOfWeek.Sunday to listOf(
                CrowdIndicator.Medium, CrowdIndicator.High, CrowdIndicator.Medium
            )
        )
    )

    fun piazzaDuomo() = PointOfInterest(
        title = "Piazza del Duomo",
        address = "50122 Firenze",
        latitude = 43.7734359,
        longitude = 11.2565184,
        squareMeters = 606.0,
        crowdIndicator = CrowdIndicator.Low,
        categories = listOf(PlaceCategory.TouristAttraction),
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
        ),
        affluence = linkedMapOf(
            DayOfWeek.Monday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Tuesday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Wednesday to listOf(
                CrowdIndicator.Low, CrowdIndicator.High, CrowdIndicator.Medium
            ),
            DayOfWeek.Thursday to listOf(
                CrowdIndicator.Low, CrowdIndicator.High, CrowdIndicator.Medium
            ),
            DayOfWeek.Friday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Saturday to listOf(
                CrowdIndicator.Low, CrowdIndicator.High, CrowdIndicator.Medium
            ),
            DayOfWeek.Sunday to listOf(
                CrowdIndicator.Low, CrowdIndicator.High, CrowdIndicator.Medium
            )
        )
    )

    fun gelateriaScoop() = PointOfInterest(
        title = "Gelateria Yogurteria",
        address = "84011 Amalfi",
        latitude = 40.6360299,
        longitude = 14.6020915,
        squareMeters = 100.0,
        crowdIndicator = CrowdIndicator.Low,
        categories = listOf(PlaceCategory.TouristAttraction),
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
        ),
        affluence = linkedMapOf(
            DayOfWeek.Monday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Tuesday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Wednesday to listOf(
                CrowdIndicator.Low, CrowdIndicator.High, CrowdIndicator.Medium
            ),
            DayOfWeek.Thursday to listOf(
                CrowdIndicator.Low, CrowdIndicator.High, CrowdIndicator.Medium
            ),
            DayOfWeek.Friday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Saturday to listOf(
                CrowdIndicator.Low, CrowdIndicator.High, CrowdIndicator.Medium
            ),
            DayOfWeek.Sunday to listOf(
                CrowdIndicator.Low, CrowdIndicator.High, CrowdIndicator.Medium
            )
        )
    )

    fun fontanaDiTrevi() = PointOfInterest(
        title = "Fontana di Trevi",
        address = "Piazza di Trevi, 00187 Roma",
        latitude = 41.9009325,
        longitude = 12.483313,
        squareMeters = 100.0,
        crowdIndicator = CrowdIndicator.Low,
        categories = listOf(PlaceCategory.TouristAttraction),
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
        ),
        affluence = linkedMapOf(
            DayOfWeek.Monday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Tuesday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Wednesday to listOf(
                CrowdIndicator.Low, CrowdIndicator.High, CrowdIndicator.Medium
            ),
            DayOfWeek.Thursday to listOf(
                CrowdIndicator.Low, CrowdIndicator.High, CrowdIndicator.Medium
            ),
            DayOfWeek.Friday to listOf(
                CrowdIndicator.Low, CrowdIndicator.Medium, CrowdIndicator.Low
            ),
            DayOfWeek.Saturday to listOf(
                CrowdIndicator.Low, CrowdIndicator.High, CrowdIndicator.Medium
            ),
            DayOfWeek.Sunday to listOf(
                CrowdIndicator.Low, CrowdIndicator.High, CrowdIndicator.Medium
            )
        )
    )
}