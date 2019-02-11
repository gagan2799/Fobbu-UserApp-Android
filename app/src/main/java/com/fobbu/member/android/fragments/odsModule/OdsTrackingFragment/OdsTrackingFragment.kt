package com.fobbu.member.android.fragments.odsModule.OdsTrackingFragment

import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fobbu.member.android.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import java.lang.Double

@Suppress("DEPRECATION")
class OdsTrackingFragment : Fragment(),GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks,LocationListener
{
    private lateinit var mMapView: MapView

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_ods_tracking, container, false)

        initView(view,savedInstanceState)

        return view
    }


    // function for initialising all the variables if the class
    private fun initView(view: View, savedInstanceState: Bundle?)
    {
        mapInitialise(view, savedInstanceState)
    }

    //########################### MAP#################################//

    private var googleApiClient: GoogleApiClient? = null

    private var locationRequest = LocationRequest.create()!!

    var location = false

    private val locationPermissionRequestCode = 12313


    //  initializing map in this method
    private fun mapInitialise(view: View, savedInstanceState: Bundle?)
    {
        mMapView = view.findViewById(R.id.mvOdsTracking) as MapView

        mMapView.onCreate(savedInstanceState)

        mMapView.onResume()// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        checkWhenMapIsReady()
    }


    @SuppressLint("MissingPermission")
    private fun checkWhenMapIsReady() {
        mMapView.getMapAsync { mMap ->
            googleMap = mMap

            val permission =
                ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION)

            if (permission == PackageManager.PERMISSION_GRANTED) {
                googleMap.isMyLocationEnabled = true
            }


            googleMap.setInfoWindowAdapter(InfoWindow())

        }
    }


    private inner class InfoWindow : GoogleMap.InfoWindowAdapter {

        override fun getInfoContents(p0: Marker?): View? {

            return null
        }

        @SuppressLint("SetTextI18n", "InflateParams")
        override fun getInfoWindow(p0: Marker?): View? {
            return null
        }
    }

    // method for checking whether GPS is enabled or not
    private fun checkGPSEnable() {
        val apiLevel = android.os.Build.VERSION.SDK_INT

        if (apiLevel >= 23) {

            val permission =
                ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION)

            if (permission != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    locationPermissionRequestCode
                )
            } else {
                enableGPSAutoMatically()
            }
        } else {
            enableGPSAutoMatically()
        }
    }


    // Method  for enabling Device GPS
    @SuppressLint("MissingPermission")
    fun enableGPSAutoMatically() {

        if (googleApiClient!!.isConnected) {

            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = (1000 * 3).toLong()
            locationRequest.fastestInterval = (1000 * 3).toLong()

            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

            // **************************
            builder.setAlwaysShow(true)// this is the key ingredient
            // **************************

            LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest,
                this@OdsTrackingFragment
            )

            val result: PendingResult<LocationSettingsResult> = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build())

            result.setResultCallback { p0 ->
                val status: Status = p0.status

                println("STATUS OF LOCATION $status")

                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> {
                        try {
                            LocationServices.FusedLocationApi.requestLocationUpdates(
                                googleApiClient, locationRequest,
                                this@OdsTrackingFragment
                            )
                        } catch (e: IntentSender.SendIntentException) {
                        }
                    }
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        status.startResolutionForResult(activity!!, 1000)
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                    }

                    LocationSettingsStatusCodes.CANCELED -> {
                        checkGPSEnable()
                    }
                }
            }
        }
    }

    // Method for setting up  marker on Map
    private fun throwMarkerOnMap(latitude: String, longitude: String)
    {
        // create marker
        val marker = MarkerOptions().position(
            LatLng(Double.valueOf(latitude), Double.valueOf(longitude))
        )//.title(data["id"].toString())

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_mark_blue))

        // adding marker
        googleMap.clear()

        googleMap.addMarker(marker)

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(Double.valueOf(latitude), Double.valueOf(longitude))).zoom(14f).build()

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    // setting up google client for map in this method
    private fun setUpGoogleClient()
    {
        googleApiClient = GoogleApiClient.Builder(context!!)

            .addApi(LocationServices.API)

            .addConnectionCallbacks(this)

            .addOnConnectionFailedListener(this).build()

        googleApiClient!!.connect()
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnected(p0: Bundle?) {
        checkGPSEnable()
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChanged(p0: Location?) {

        println("LOCATION >>>>>>>>>>>>>>>>>>>> " + p0!!.latitude)

        if (!location)
        {
            throwMarkerOnMap(p0.latitude.toString(), p0.longitude.toString())

            //mapClicks()

            //getAddressFromLocation(p0.latitude,p0.longitude)

            location = true
        }
        else if (location)
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this@OdsTrackingFragment)

    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode)
        {
            locationPermissionRequestCode ->
            {
                if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    checkGPSEnable()

                else
                    checkGPSEnable()

            }
        }
    }

}
