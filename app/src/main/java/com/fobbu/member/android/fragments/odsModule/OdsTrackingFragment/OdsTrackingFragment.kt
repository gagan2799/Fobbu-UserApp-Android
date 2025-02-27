package com.fobbu.member.android.fragments.odsModule.OdsTrackingFragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.fobbu.member.android.R
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenWhite
import com.fobbu.member.android.fragments.rsaFragmentModule.presenter.RsaLiveHandler
import com.fobbu.member.android.fragments.rsaFragmentModule.presenter.RsaLivePresenter
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
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
import kotlinx.android.synthetic.main.fragment_ods_tracking.view.*
import java.lang.Double

@Suppress("DEPRECATION")
class OdsTrackingFragment : Fragment(),GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks,LocationListener,ActivityView
{
    private lateinit var mMapView: MapView

    private lateinit var googleMap: GoogleMap

    private lateinit var livetTrackingHandler:RsaLiveHandler

    private var mobileNumber = "123"

    private lateinit var coordinatorLayout: CoordinatorLayout

    lateinit var  commonClass: CommonClass

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ods_tracking, container, false)

        if (view != null)
        {
            initView(view, savedInstanceState)   // function for initialising all the variables if the class

            clicks(view)   // function for handling clicks of the class
        }
        return view
    }

    // function for initialising all the variables if the class
    private fun initView(view: View, savedInstanceState: Bundle?)
    {
        commonClass= CommonClass(activity!!,activity!!)

        coordinatorLayout = view.findViewById(R.id.coordinator) as CoordinatorLayout

        livetTrackingHandler=RsaLivePresenter(activity!!,this)

        initPersistentBottomsheet()   //insert bottom sheet

        mapInitialise(view, savedInstanceState)

        setUpGoogleClient()  // setting up google client for map in this method
    }

    // function for handling clicks of the class
    @SuppressLint("SetTextI18n")
    private fun clicks(view: View)
    {
        view.ivCallOdsTrack.setOnClickListener {
            checkPermissionForCall()  // function for checking call permission
        }

        view.tvLiveTrackOds.setOnClickListener {
            if (view.tvStatusOdsTrack.visibility==View.VISIBLE)
            {
                view.tvStatusOdsTrack.visibility=View.INVISIBLE

                view.rlOdsTrackOtp.visibility=View.VISIBLE
            }

            else if (view.rlOdsTrackOtp.visibility==View.VISIBLE)
            {
                startActivity(Intent(activity!!,WaitingScreenWhite::class.java).putExtra("from_where","code_valid_ods"))

                activity!!.finish()
            }
        }
    }

    // function for checking call permission
    private fun checkPermissionForCall()
    {
        val apiLevel = android.os.Build.VERSION.SDK_INT

        if (apiLevel >= 23)
        {
            //phone state
            val permission1 = ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.CALL_PHONE)

            if (permission1 != PackageManager.PERMISSION_GRANTED)
                makeRequest()  // function for requesting permission

             else
                dialNumber()  //function for calling
        }
        else
            dialNumber()  //function for calling
    }

    //function for calling
    private fun dialNumber()
    {
        if (mobileNumber != "")
            CommonClass(activity!!, activity!!).callOnPhone(mobileNumber)
    }

    // function for requesting permission
    private fun makeRequest()
    {
        requestPermissions(arrayOf("android.permission.CALL_PHONE"), 1)
    }

    //########################### MAP#################################//

    private var googleApiClient: GoogleApiClient? = null

    private var locationRequest = LocationRequest.create()!!

    private var location = false

    private val locationPermissionRequestCode = 12313


    //  initializing map in this method
    private fun mapInitialise(view: View, savedInstanceState: Bundle?)
    {
        mMapView = view.findViewById(R.id.mvOdsTracking) as MapView

        mMapView.onCreate(savedInstanceState)

        mMapView.onResume()// needed to get the map to display immediately

        try
        {
            MapsInitializer.initialize(activity!!.applicationContext)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

        //  setting up a callback object which will be triggered when the GoogleMap instance is ready to be used.
        checkWhenMapIsReady()
    }

    // function for setting up a callback object which will be triggered when the GoogleMap instance is ready to be used.
    @SuppressLint("MissingPermission")
    private fun checkWhenMapIsReady()
    {
        mMapView.getMapAsync { mMap ->
            googleMap = mMap

            val permission = ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION)

            if (permission == PackageManager.PERMISSION_GRANTED)
                googleMap.isMyLocationEnabled = true

            googleMap.setInfoWindowAdapter(InfoWindow())
        }
    }

// inner class for customising the info window of the marker
    private inner class InfoWindow : GoogleMap.InfoWindowAdapter
{
    override fun getInfoContents(p0: Marker?): View?
    {
        return null
        }

        @SuppressLint("SetTextI18n", "InflateParams")
        override fun getInfoWindow(p0: Marker?): View?
        {
            return null
        }
    }

    // method for checking whether GPS is enabled or not
    private fun checkGPSEnable() {
        val apiLevel = android.os.Build.VERSION.SDK_INT

        if (apiLevel >= 23)
        {
            val permission = ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION)

            if (permission != PackageManager.PERMISSION_GRANTED)
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionRequestCode)

             else
                enableGPSAutoMatically()  // Method  for enabling Device GPS
        }
        else
            enableGPSAutoMatically()  // Method  for enabling Device GPS

    }


    // Method  for enabling Device GPS
    @SuppressLint("MissingPermission")
    fun enableGPSAutoMatically()
    {
        if (googleApiClient!!.isConnected)
        {
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

            val result: PendingResult<LocationSettingsResult> = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())

            result.setResultCallback { p0 ->
                val status: Status = p0.status

                println("STATUS OF LOCATION $status")

                when (status.statusCode)
                {
                    LocationSettingsStatusCodes.SUCCESS ->
                    {
                        try
                        {
                            LocationServices.FusedLocationApi.requestLocationUpdates(
                                googleApiClient, locationRequest,
                                this@OdsTrackingFragment
                            )
                        }
                        catch (e: IntentSender.SendIntentException) { }
                    }

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    {
                        status.startResolutionForResult(activity!!, 1000)
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> { }

                    LocationSettingsStatusCodes.CANCELED ->
                    {
                        checkGPSEnable()  // method for checking whether GPS is enabled or not
                    }
                }
            }
        }
    }

    // Method for setting up  marker on Map
    private fun throwMarkerOnMap(latitude: String, longitude: String)
    {
        // create marker
        val marker = MarkerOptions().position(LatLng(Double.valueOf(latitude), Double.valueOf(longitude)))//.title(data["id"].toString())

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_mark_blue))

        // adding marker
        googleMap.clear()

        googleMap.addMarker(marker)

        val cameraPosition = CameraPosition.Builder().target(LatLng(Double.valueOf(latitude), Double.valueOf(longitude))).zoom(14f).build()

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
        checkGPSEnable()  // method for checking whether GPS is enabled or not
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChanged(p0: Location?)
    {
        println("LOCATION >>>>>>>>>>>>>>>>>>>> " + p0!!.latitude)

        if (!location)
        {
            throwMarkerOnMap(p0.latitude.toString(), p0.longitude.toString())   // Method for setting up  marker on Map

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
                    checkGPSEnable()  // method for checking whether GPS is enabled or not

                else
                    checkGPSEnable()  // method for checking whether GPS is enabled or not
            }

            1 ->
            {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    showMessageDialog(resources.getString(R.string.permission_message))    // Method for opening dialog containing message

                else
                    dialNumber()  //function for calling

                return
            }
        }
    }

    // Method for opening dialog containing message
    private fun showMessageDialog(message: String)
    {
        val alertDialog = AlertDialog.Builder(activity!!, R.style.MyDialogTheme).create()

        alertDialog.setMessage(message)

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialog.show()

        val messageText: TextView = alertDialog!!.findViewById(android.R.id.message)!!

        messageText.gravity = Gravity.LEFT
    }

    //insert bottom sheet
    private fun initPersistentBottomsheet()
    {
        val persistentbottomSheet = coordinatorLayout.findViewById<LinearLayout>(R.id.bottomsheet)

        val rlBottomSheet = persistentbottomSheet.findViewById(R.id.rlBottomSheet) as RelativeLayout

        val behavior = BottomSheetBehavior.from<View>(persistentbottomSheet)

        rlBottomSheet.setOnClickListener {
            if (behavior!!.state == BottomSheetBehavior.STATE_COLLAPSED)
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED)

            else
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }

        behavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback()
        {
            override fun onStateChanged(bottomSheet: View, newState: Int)
            {
                //showing the different states
                when (newState)
                {
                    BottomSheetBehavior.STATE_HIDDEN -> { }

                    BottomSheetBehavior.STATE_EXPANDED -> { }

                    BottomSheetBehavior.STATE_COLLAPSED -> { }

                    BottomSheetBehavior.STATE_DRAGGING -> { }

                    BottomSheetBehavior.STATE_SETTLING -> { }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float)
            {
                // React to dragging events
            }
        })
    }

    //****************************** requests API *********************//

    /*private fun getService()
    {
        if (commonClass.checkInternetConn(activity!!))
            livetTrackingHandler.getService(
                commonClass.getString("x_access_token"),
                commonClass.getString(
                    RsaConstants.ServiceSaved.fobbuRequestId
                )
            )

        else
            commonClass.showToast(activity!!.resources.getString(R.string.internet_is_unavailable))
    }*/

    override fun onRequestSuccessReport(mainPojo: MainPojo) {

    }

    override fun showLoader() {

    }

    override fun hideLoader() {

    }


}

