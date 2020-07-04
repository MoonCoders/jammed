package com.github.mooncoders.jammed.ui

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mooncoders.jammed.R
import com.github.mooncoders.jammed.sdk.models.PointsOfInterestParams
import com.github.mooncoders.jammed.ui.foundation.BaseFragment
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions


class MapFragment : BaseFragment(), OnMapReadyCallback {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private lateinit var mMap: GoogleMap

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
        autocompleteFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN)

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
            }

            override fun onError(status: Status) {
                Log.e("TAG", "Error autocompleteFragment: ${status.statusMessage}")
            }
        })

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.pointsOfInterest.apply {
            success.observe(viewLifecycleOwner, Observer { pointsOfInterest ->
                pointsOfInterest.forEach { pointOfInterest ->
                    mMap.addMarker(pointOfInterest.marker())
                }
            })

            error.observe(viewLifecycleOwner, Observer {
                Log.e("TAG", "Error", it)
            })
        }

        viewModel.pedestriansFetcher.apply {
            success.observe(viewLifecycleOwner, Observer {
                Log.e("TAG", "Number of pedestrians: $it")
            })

            error.observe(viewLifecycleOwner, Observer {
                Log.e("TAG", "Error", it)
            })
        }
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    // [START maps_current_place_on_save_instance_state]
    override fun onSaveInstanceState(outState: Bundle) {
        ::mMap.isInitialized.takeIf { it }?.let {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Turn on the My Location layer and the related control on the map.
        updateLocation()

        // Add a marker in Sydney and move the camera
//        val milanMarker = LatLng(45.464664, 9.188540)
//        mMap.addMarker(MarkerOptions().position(milanMarker).title("Marker in Milan"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(milanMarker))
    }

    @SuppressLint("MissingPermission")
    val mapOptions = QuickPermissionsOptions(
        handleRationale = false,
        rationaleMessage = "Custom rational message",
        permanentDeniedMethod = { req ->
            mMap.isMyLocationEnabled = false
            mMap.uiSettings?.isMyLocationButtonEnabled = false
            lastKnownLocation = null

            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    defaultLocation,
                    DEFAULT_ZOOM.toFloat()
                )
            )
        }
    )

    @SuppressLint("MissingPermission")
    private fun updateLocation() = runWithPermissions(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        options = mapOptions
    ) {
        mMap.isMyLocationEnabled = true
        mMap.uiSettings?.isMyLocationButtonEnabled = true
        mMap.uiSettings?.changeMyLocationButtonMargin(0, 180, 180, 0)

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
                viewModel.pointsOfInterest.fetch(currentLocation())
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        defaultLocation,
                        DEFAULT_ZOOM.toFloat()
                    )
                )
                mMap.uiSettings?.isMyLocationButtonEnabled = false
                return@addOnCompleteListener
            }

            // Set the map's camera position to the current location of the device.
            lastKnownLocation = task.result

            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude),
                    DEFAULT_ZOOM.toFloat()
                )
            )
            viewModel.pointsOfInterest.fetch(currentLocation())
        }
    }

    private fun UiSettings.changeMyLocationButtonMargin(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        val locationButton = requireView().findViewById<ImageView>(Integer.parseInt("2"))
        val rlp: RelativeLayout.LayoutParams =
            locationButton.layoutParams as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
        rlp.setMargins(left, top, right, bottom)
        locationButton.requestLayout()
    }

    companion object {
        private const val DEFAULT_ZOOM = 15

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }
}