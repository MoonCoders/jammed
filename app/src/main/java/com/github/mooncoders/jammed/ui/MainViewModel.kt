package com.github.mooncoders.jammed.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mooncoders.jammed.App
import com.github.mooncoders.jammed.sdk.models.PointOfInterest
import com.github.mooncoders.jammed.sdk.models.PointsOfInterestParams
import com.github.mooncoders.jammed.ui.foundation.CoroutineFetcher

class MainViewModel : ViewModel() {

    val pedestriansFetcher = CoroutineFetcher<PointOfInterest, Int>(viewModelScope) { image ->
        App.sdk.getNumberOfPedestrians(image)
    }

    val pointsOfInterest =
        CoroutineFetcher<PointsOfInterestParams, List<PointOfInterest>>(viewModelScope) {
            App.sdk.getPointsOfInterest(it)
        }
}