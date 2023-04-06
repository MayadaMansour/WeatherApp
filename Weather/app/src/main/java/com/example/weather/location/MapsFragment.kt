package com.example.weather.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.mvvm.Model.Reposatory
import com.example.weather.R
import com.example.weather.data.weather.LocalSource.ConcreteLocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.databinding.FragmentMapsBinding
import com.example.weather.models.City
import com.example.weather.ui.favorite.view.FavoriteViewModel
import com.example.weather.ui.favorite.view.FavoriteViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    var lat: Double = 0.0
    var lon: Double = 0.0
    val args: MapsFragmentArgs by navArgs()
    lateinit var binding: FragmentMapsBinding
    lateinit var fusedClient: FusedLocationProviderClient
    lateinit var mapFragment: SupportMapFragment
    lateinit var mMap: GoogleMap
    lateinit var myViewModel: FavoriteViewModel
    lateinit var se: SharedPreferences


    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            lat = it.latitude
            lon = it.latitude
            goToLatLng(it.latitude, it.longitude, 16f)

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        se = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)
        mapInitialize()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    private fun goToLatLng(latitude: Double, longitude: Double, float: Float) {
        var name = "Unknown "
        val geocoder = Geocoder(requireContext()).getFromLocation(latitude, longitude, 1)
        if (geocoder!!.size > 0)
            name = "${geocoder.get(0)?.subAdminArea} , ${geocoder.get(0)?.adminArea}"
        val latLng = LatLng(latitude, longitude)
        val update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, float)
        mMap.addMarker(MarkerOptions().position(latLng))
        mMap.animateCamera(update)


        //Favorite/Home Fragment
        binding.addToFav.setOnClickListener {
            if (args.from.equals("Home")) {
                val action =
                    MapsFragmentDirections.actionMapsFragmentToNavigationHome(
                        lat.toFloat(),
                        lon.toFloat(),
                        true
                    )
                Navigation.findNavController(it).navigate(action)

            } else {
                val favFactory = FavoriteViewModelFactory(
                    Reposatory.getInstance(
                        Client.getInstance(),
                        ConcreteLocalSource(requireContext()),
                        PreferenceManager.getDefaultSharedPreferences(requireContext())
                    )
                )

                myViewModel =
                    ViewModelProvider(
                        requireActivity(),
                        favFactory
                    )[FavoriteViewModel::class.java]

                myViewModel.insertWeathers(City(latitude, longitude, name))
                Toast.makeText(requireContext(), "Added To Favorite", Toast.LENGTH_SHORT).show()
                val action =
                    MapsFragmentDirections.actionMapsFragmentToNavigationFavorite()
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

//----------------------------------------------------------------------------------------//
    private fun goToSearchLocation() {
        val searchLocation = binding.searchEdt.text.toString()
        val list = Geocoder(requireContext()).getFromLocationName(searchLocation, 1)
        if (list != null && list.size > 0) {
            var address: Address = list.get(0)
            goToLatLng(address.latitude, address.longitude, 16f)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun mapInitialize() {
        val locationRequest: LocationRequest = LocationRequest()
        locationRequest.setInterval(5000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setSmallestDisplacement(14f)
        locationRequest.setFastestInterval(3000)
        binding.searchEdt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.action == KeyEvent.ACTION_DOWN
                || event.action == KeyEvent.KEYCODE_ENTER
            ) {
                if (!binding.searchEdt.text.isNullOrEmpty())
                    goToSearchLocation()
            }
            false
        }
        fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

}