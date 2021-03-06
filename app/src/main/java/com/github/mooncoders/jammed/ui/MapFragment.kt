package com.github.mooncoders.jammed.ui

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.StackedValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mooncoders.jammed.R
import com.github.mooncoders.jammed.sdk.extensions.marker
import com.github.mooncoders.jammed.sdk.models.CrowdIndicator
import com.github.mooncoders.jammed.sdk.models.PlaceCategory
import com.github.mooncoders.jammed.sdk.models.PointOfInterest
import com.github.mooncoders.jammed.sdk.models.PointsOfInterestParams
import com.github.mooncoders.jammed.ui.foundation.changeMyLocationButtonMargin
import com.github.mooncoders.jammed.ui.foundation.getDirections
import com.github.mooncoders.jammed.ui.foundation.openUrl
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import kotlinx.android.synthetic.main.place_info_sheet.*
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private var mMap: GoogleMap? = null

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null

    private var cameraPosition: CameraPosition? = null

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Milan, Italy) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(45.464664, 9.188540)

    // Radius in meters around the location
    private val radiusInMeters = 0.01

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.map_fragment, container, false)

    private fun currentLocation(): PointsOfInterestParams {
        val location = lastKnownLocation

        return if (location == null) {
            PointsOfInterestParams(
                latitude = defaultLocation.latitude,
                longitude = defaultLocation.longitude,
                radius = radiusInMeters
            )
        } else {
            PointsOfInterestParams(
                latitude = location.latitude,
                longitude = location.longitude,
                radius = radiusInMeters
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        // Construct a PlacesClient
        Places.initialize(
            requireActivity().applicationContext,
            getString(R.string.google_maps_api_key)
        )
        placesClient = Places.createClient(requireActivity())

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.search_bar)
                as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.LAT_LNG, Place.Field.NAME))
        autocompleteFragment.setActivityMode(AutocompleteActivityMode.OVERLAY)

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                viewModel.pointOfInterest.fetch(
                    PointsOfInterestParams(
                        place.latLng!!.latitude,
                        place.latLng!!.longitude,
                        0.0
                    )
                )
                mMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        place.latLng,
                        DEFAULT_ZOOM.toFloat()
                    )
                )
            }

            override fun onError(status: Status) {
                Log.e("TAG", "Error autocompleteFragment: ${status.statusMessage}")
            }
        })

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.pointOfInterest.apply {
            success.observe(viewLifecycleOwner, Observer { pointsOfInterest ->
                mMap?.also { map ->
                    map.addMarker(pointsOfInterest.marker(requireContext())).tag = pointsOfInterest
                }
            })

            error.observe(viewLifecycleOwner, Observer {
                Log.e("TAG", "Error", it)
            })
        }

        viewModel.mockPointsOfInterestAroundYou.apply {
            success.observe(viewLifecycleOwner, Observer { pointsOfInterest ->
                mMap?.also { map ->
                    pointsOfInterest.forEach { pointOfInterest ->
                        map.addMarker(pointOfInterest.marker(requireContext())).tag =
                            pointOfInterest
                    }
                }
            })

            error.observe(viewLifecycleOwner, Observer {
                Log.e("TAG", "Error", it)
            })
        }

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset >= 0.52) preview.alpha = slideOffset
                else preview.alpha = 0f
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) = Unit
        })

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    // [START maps_current_place_on_save_instance_state]
    override fun onSaveInstanceState(outState: Bundle) {
        mMap?.let {
            outState.putParcelable(KEY_CAMERA_POSITION, it.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap.apply {
            setOnMarkerClickListener { marker ->
                (marker.tag as? PointOfInterest)?.let {
                    viewModel.selectedPointOfInterest.postValue(it)
                    true
                } ?: false
            }
        }


        // Turn on the My Location layer and the related control on the map.
        updateLocation()

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        viewModel.selectedPointOfInterest.observe(this, Observer { selectedPoi ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            bottom_sheet_title.text = selectedPoi.title
            bottom_sheet_title.gravity = Gravity.LEFT
            bottom_sheet_subtitle.text = selectedPoi.address

            chips.removeAllViews()

            live.setOnClickListener {
                requireActivity().openUrl(selectedPoi.provider.liveUrl)
            }

            when (selectedPoi.crowdIndicator) {
                CrowdIndicator.Low -> {
                    crowdness.setImageResource(R.drawable.ic_marker_low)
                    crowdness_details.setText(R.string.low_crowdness)
                }
                CrowdIndicator.Medium -> {
                    crowdness.setImageResource(R.drawable.ic_marker_mid)
                    crowdness_details.setText(R.string.mid_crowdness)
                }
                CrowdIndicator.High -> {
                    crowdness.setImageResource(R.drawable.ic_marker_high)
                    crowdness_details.setText(R.string.hi_crowdness)
                }
            }

            selectedPoi.categories.forEach {
                val chip =
                    Chip(
                        ContextThemeWrapper(
                            requireContext(),
                            R.style.Widget_MaterialComponents_Chip_Action
                        )
                    )
                when (it) {
                    PlaceCategory.Beach -> {
                        chip.text = getString(R.string.beach)
                        chip.chipIcon = getDrawable(requireContext(), R.drawable.ic_beach)
                    }
                    PlaceCategory.TouristAttraction -> {
                        chip.text = getString(R.string.tourist)
                        chip.chipIcon = getDrawable(requireContext(), R.drawable.ic_flag)
                    }
                }
                chip.setOnClickListener { findNavController().navigate(R.id.saved) }
                chips.addView(chip)
            }

            chips.addView(Chip(
                ContextThemeWrapper(
                    requireContext(),
                    R.style.Widget_MaterialComponents_Chip_Action
                )
            ).apply {
                text = getString(R.string.directions)
                chipIcon = getDrawable(requireContext(), R.drawable.ic_directions)
                setOnClickListener {
                    lastKnownLocation?.let {
                        requireActivity().getDirections(
                            LatLng(it.latitude, it.longitude),
                            LatLng(selectedPoi.latitude, selectedPoi.longitude)
                        )
                    }
                }
            })

            chips.addView(Chip(
                ContextThemeWrapper(
                    requireContext(),
                    R.style.Widget_MaterialComponents_Chip_Action
                )
            ).apply {
                text = getString(R.string.save)
                chipIcon = getDrawable(requireContext(), R.drawable.ic_saved)
                setOnClickListener { findNavController().navigate(R.id.saved) }
            })

            val values = mutableListOf<BarEntry>()
            val colors = mutableListOf<Int>()

            var i = 0f

            selectedPoi.affluence.forEach { entry ->
                val barEntry = BarEntry(i, floatArrayOf(8f, 8f, 8f))
                colors.addAll(entry.value.getColors())

                values.add(barEntry)
                i += 1
            }

            val set = BarDataSet(values, "")

            set.colors = colors
            set.setDrawValues(false)
            val dataSets = ArrayList<IBarDataSet>()

            dataSets.add(set)

            val data = BarData(dataSets)
            data.setValueFormatter(StackedValueFormatter(false, "", 1))

            chart.data = data

            chart.setPinchZoom(false)
            chart.isDoubleTapToZoomEnabled = false
            chart.setDrawValueAboveBar(false)
            chart.isHighlightFullBarEnabled = false
            chart.axisLeft.setDrawGridLines(false)
            chart.xAxis.setDrawGridLines(false)
            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            chart.setDrawGridBackground(false)
            chart.axisRight.isEnabled = false
            chart.description.isEnabled = false
            chart.legend.isEnabled = false

            chart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${selectedPoi.affluence.keys.toList().get(value.toInt()).name.first()}"
                }
            }

            chart.axisLeft.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return when (value) {
                        5f -> "12:00"
                        15f -> "18:00"
                        25f -> "24:00"
                        else -> ""
                    }
                }
            }

            chart.invalidate()
            Glide.with(this).load(selectedPoi.provider.imageUrl).into(preview)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        })

    }

    fun List<CrowdIndicator>.getColors() = map {
        ContextCompat.getColor(
            requireContext(), when (it) {
                CrowdIndicator.Low -> R.color.colorLow
                CrowdIndicator.Medium -> R.color.colorMid
                CrowdIndicator.High -> R.color.colorHigh
            }
        )
    }

    @SuppressLint("MissingPermission")
    val mapOptions = QuickPermissionsOptions(
        handleRationale = false,
        rationaleMessage = "Custom rational message",
        permanentDeniedMethod = { req ->
            mMap?.apply {
                isMyLocationEnabled = false
                uiSettings?.isMyLocationButtonEnabled = false

                moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        defaultLocation,
                        DEFAULT_ZOOM.toFloat()
                    )
                )
            }

            lastKnownLocation = null
        }
    )

    @SuppressLint("MissingPermission")
    private fun updateLocation() = runWithPermissions(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        options = mapOptions
    ) {
        mMap?.apply {
            isMyLocationEnabled = true
            uiSettings?.isMyLocationButtonEnabled = true
            uiSettings?.changeMyLocationButtonMargin(requireView(), 0, 230, 230, 0)
        }

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            task.takeIf { it.isSuccessful }?.result ?: kotlin.run {
                viewModel.mockPointsOfInterestAroundYou.fetch(currentLocation())
                mMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        defaultLocation,
                        DEFAULT_ZOOM.toFloat()
                    )
                )
                mMap?.uiSettings?.isMyLocationButtonEnabled = false
                return@addOnCompleteListener
            }

            // Set the map's camera position to the current location of the device.
            lastKnownLocation = task.result

            mMap?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude),
                    DEFAULT_ZOOM.toFloat()
                )
            )
            viewModel.mockPointsOfInterestAroundYou.fetch(currentLocation())
        }
    }

    companion object {
        private const val DEFAULT_ZOOM = 15

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }
}