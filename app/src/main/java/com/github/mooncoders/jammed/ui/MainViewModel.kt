package com.github.mooncoders.jammed.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mooncoders.jammed.App
import com.github.mooncoders.jammed.sdk.models.PointOfInterest
import com.github.mooncoders.jammed.sdk.models.PointsOfInterestParams
import com.github.mooncoders.jammed.ui.foundation.CoroutineFetcher

class MainViewModel : ViewModel() {

    val selectedPointOfInterest = MutableLiveData<PointOfInterest>()

    val pointOfInterest =
        CoroutineFetcher<PointsOfInterestParams, PointOfInterest>(viewModelScope) {
            App.sdk.getPointOfInterest(it)
        }

    val mockPointsOfInterestAroundYou =
        CoroutineFetcher<PointsOfInterestParams, List<PointOfInterest>>(viewModelScope) {
            App.sdk.getMockPointsOfInterest(it)
        }

}