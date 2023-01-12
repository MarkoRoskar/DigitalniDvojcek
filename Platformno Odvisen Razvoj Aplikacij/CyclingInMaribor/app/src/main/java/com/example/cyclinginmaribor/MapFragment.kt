package com.example.cyclinginmaribor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cyclinginmaribor.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    private lateinit var selectedLocation: String

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onActivityCreated(savedInstanceState)",
        "androidx.fragment.app.Fragment"
    ))
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mapView = binding.mapView
        // to display the map immediately
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        // add all hotel markers to the map
        mapView.getMapAsync{ googleMap ->
            // remove all markers
            googleMap.clear()

            // animation to zoom in where the marker is
            val mariborLocation = LatLng(46.5546385762219, 15.645714707066432)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mariborLocation, 14f))

            googleMap.setOnMapClickListener { coordinates ->
                Log.i("clicked location", coordinates.latitude.toString() + ", " + coordinates.longitude.toString())
                selectedLocation = coordinates.latitude.toString() + "," + coordinates.longitude.toString()
                googleMap.addMarker(
                    MarkerOptions()
                        .position(coordinates)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
                )
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 16f))
            }

            // add marker to the map
            /*googleMap.addMarker(
                MarkerOptions()
                    .position(hotelLocation1)
                    .title("S Hotel")
                    .snippet("Smetanova ulica 20, 2000 Maribor")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_marker_2))
            )*/

            /*googleMap.setOnMarkerClickListener { marker -> // on marker click we are getting the title of our marker
                // which is clicked and displaying it in a toast message.
                val markerName = marker.title
                Toast.makeText(
                    context,
                    "Clicked location is $markerName",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }*/

            // clicking on the marker info window takes us to the input screen
            // we also send the hotel name

            //googleMap.setInfoWindowAdapter(InfoWindowAdapter(this))

        }
    }

    override fun onMapReady(map: GoogleMap) {
        map.let {
            googleMap = it
        }


        /*map.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            }
            else {
                marker.showInfoWindow()
            }
            true
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapBinding.inflate(inflater, container, false)

        val okButton = binding.okButton
        okButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("location", selectedLocation)
            bundle.putString("category", arguments?.getString("category"))
            if (arguments?.getString("description") != "") {
                bundle.putString("description", arguments?.getString("description"))
            }
            Log.i("map-category", arguments?.getString("category").toString())
            findNavController().navigate(R.id.eventFragment, bundle)
        }

        val backButton = binding.backButton
        backButton.setOnClickListener {
            findNavController().navigate(R.id.eventFragment)     // navigate to home screen/fragment
        }

        return binding.root
    }
}