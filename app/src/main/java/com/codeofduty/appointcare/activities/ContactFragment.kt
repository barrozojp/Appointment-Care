package com.codeofduty.appointcare.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codeofduty.appointcare.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ContactFragment : Fragment() {

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        // Initialize the MapView
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        // Get a reference to the GoogleMap when it's ready
        mapView.getMapAsync { googleMap ->
            // Check if mapView is not null before calling methods on it
            mapView?.let {
                // Move the camera to the specified location
                val location = LatLng(16.046557439184518, 120.34136334821387)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

                // Add a marker at the specified location
                googleMap.addMarker(MarkerOptions().position(location).title("Marker Title"))
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Make sure to call the lifecycle methods of the MapView
        mapView.onResume()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
