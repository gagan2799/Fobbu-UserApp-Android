package com.fobbu.member.android.fragments.odsModule.odsFragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboardActivity.DashboardActivity
import com.fobbu.member.android.fragments.odsModule.odsFragment.adapter.OdsFragmentAdapter
import com.fobbu.member.android.fragments.odsModule.odsFragment.presenter.MapServiceHandler
import com.fobbu.member.android.fragments.odsModule.odsFragment.presenter.MapServicePresenter
import com.fobbu.member.android.fragments.odsModule.odsFragment.view.MapServiceView
import com.fobbu.member.android.fragments.odsModule.odsServiceOperations.OdsOperationFragment
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.fragments.rsaFragmentModule.presenter.RsaFragmentHandler
import com.fobbu.member.android.fragments.rsaFragmentModule.presenter.RsaFragmnetPresenter
import com.fobbu.member.android.fragments.rsaFragmentModule.view.RsaFragmentView
import com.fobbu.member.android.interfaces.TopBarChanges
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.utils.RecyclerItemClickListener
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_ods.*

import kotlinx.android.synthetic.main.fragment_ods.view.*
import kotlinx.android.synthetic.main.inflate_marker_title.view.*
import kotlinx.android.synthetic.main.inflate_ods_dialog.*
import java.lang.Double
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@Suppress("DEPRECATION")
class OdsFragment : Fragment(), GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks,LocationListener,RsaFragmentView,MapServiceView
{
    var addressList=ArrayList<HashMap<String,Any>>()

    lateinit var rlLoader:RelativeLayout

    private lateinit var serviceHandler:RsaFragmentHandler

    private var mLastLocation: Location? = null

    private lateinit var mMapView: MapView

    private var topBarChanges: TopBarChanges? = null

    private lateinit var googleMap: GoogleMap

    lateinit var dataList:ArrayList<HashMap<String,Any>>

    private lateinit var odsAdapter:OdsFragmentAdapter

    var selectedPosition=0

    private lateinit var geocoder:Geocoder

    private lateinit var mapServiceHandler:MapServiceHandler

    private var odsService= arrayOf("Trip Ready","General Service","Washing","VAS")

    lateinit var commonClass:CommonClass

    var wheels=""

    var currentAddress=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_ods, container, false)

        if (isAdded)
        {
            if (view != null)
            {
                initView(view, savedInstanceState)

                clicks(view)
            }
        }
        // Inflate the layout for this fragment
        return view
    }

    // function for initialising all the variables of the class
    private fun initView(view: View, savedInstanceState: Bundle?)
    {
        rlLoader=view.findViewById(R.id.rlLoader)

        commonClass= CommonClass(activity!!,activity!!)

        dataList=ArrayList()

        mapInitialise(view, savedInstanceState)

        setUpGoogleClient()

        fetchService()

        setRecycler(view)

        mapServiceHandler=MapServicePresenter(activity!!,this)
    }


    // function for handling the clicks of the class
    private fun clicks(view: View)
    {
        view.rvOdsService.addOnItemTouchListener(RecyclerItemClickListener(activity!!,object :RecyclerItemClickListener.OnItemClickListener
        {
            override fun onItemClick(view: View, position: Int)
            {
                selectedPosition=position

                if (currentAddress=="")
                    commonClass.showToast(getString(R.string.gathering_info))

                else
                    showDialog(activity!!,position)
            }
        }))

        view.llScooterOds.setOnClickListener {
            view.tvCarOds.setTextColor(activity!!.resources.getColor(R.color.drawer_text_color))

            view.tvCarOds.setTypeface(null, Typeface.NORMAL)

            if (view.tvScooterOds.currentTextColor==activity!!.resources.getColor(R.color.drawer_text_color))
            {
                wheels="2"

                view.tvScooterOds.setTextColor(activity!!.resources.getColor(R.color.colorPrimary))

                view.tvScooterOds.typeface= Typeface.DEFAULT_BOLD
            }
            else
            {
                wheels=""

                view.tvScooterOds.setTextColor(activity!!.resources.getColor(R.color.drawer_text_color))

                view.tvScooterOds.setTypeface(null, Typeface.NORMAL)
            }
        }

        view.llCarOds.setOnClickListener {
            view.tvScooterOds.setTextColor(activity!!.resources.getColor(R.color.drawer_text_color))

            view.tvScooterOds.setTypeface(null, Typeface.NORMAL)

            if (view.tvCarOds.currentTextColor==activity!!.resources.getColor(R.color.drawer_text_color))
            {
                wheels="4"

                view.tvCarOds.setTextColor(activity!!.resources.getColor(R.color.colorPrimary))

                view.tvCarOds.typeface= Typeface.DEFAULT_BOLD
            }
            else
            {
                wheels=""

                view.tvCarOds.setTextColor(activity!!.resources.getColor(R.color.drawer_text_color))

                view.tvCarOds.setTypeface(null, Typeface.NORMAL)
            }
        }


        view.tvContinueOds.setOnClickListener {
            commonClass.putString(RsaConstants.Ods.service_name,odsService[selectedPosition])

            when
            {
                wheels==""->
                    commonClass.showToast(getString(R.string.provide_vehicle_msg))

                etVehicleModelOds.text.isNullOrEmpty()->
                    commonClass.showToast(getString(R.string.car_model_message))

                else->
                    changeFragment(OdsOperationFragment())
            }
        }

        view.tvContinueOdsCarDetails.setOnClickListener {
            commonClass.putString(RsaConstants.Ods.service_name,odsService[selectedPosition])

            when{
                etCarModelOds.text.isNullOrEmpty()->
                    commonClass.showToast(getString(R.string.car_model_message))


                etCarRegestrationOds.text.isNullOrEmpty()->
                    commonClass.showToast(getString(R.string.registeration_no_message))

                else->
                {
                    commonClass.putString(RsaConstants.Ods.vehicleType,wheels)

                    commonClass.putString(RsaConstants.Ods.vehicleNumber,etCarModelOds.text.toString())

                    commonClass.putString(RsaConstants.Ods.regNo,etCarRegestrationOds.text.toString())

                    commonClass.putString(RsaConstants.Ods.model,etCarModelOds.text.toString())

                    changeFragment(OdsOperationFragment())
                }
            }
        }

        view.ivBack.setOnClickListener {
            ifTopBarChnagesNull(true)

            wheels=""

            view.tvCarOds.setTextColor(activity!!.resources.getColor(R.color.drawer_text_color))

            view.tvCarOds.setTypeface(null, Typeface.NORMAL)

            view.tvScooterOds.setTextColor(activity!!.resources.getColor(R.color.drawer_text_color))

            view.tvScooterOds.setTypeface(null, Typeface.NORMAL)

            view.llOdsServices.visibility = View.VISIBLE

            view.llCarDetailsOds.visibility=View.INVISIBLE

            view.llVehicleTypeOds.visibility=View.INVISIBLE

            view.rlTopDrawerOds.visibility=View.INVISIBLE
        }
    }


    private fun changeFragment(fragment:Fragment)
    {
        val ft: FragmentTransaction  = activity!!.supportFragmentManager.beginTransaction()

        ft.replace(R.id.content_frame, fragment)

        ft.commit()
    }


    private fun setRecycler(view: View) {
        odsAdapter= OdsFragmentAdapter(activity!!,dataList)

        view.rvOdsService.layoutManager= GridLayoutManager(activity!!,2)

        view.rvOdsService.adapter=odsAdapter
    }


    fun showDialog(activity: Context,position:Int)
    {
        val dialog= Dialog(activity)

        dialog.setContentView(R.layout.inflate_ods_dialog)

        dialog.tvServiceAddress.text=currentAddress

        dialog.tvChange.setOnClickListener {
            dialog.dismiss()
        }

        dialog.tvConfirm.setOnClickListener {
            commonClass.putString(RsaConstants.Ods.address,currentAddress)

            val singleService=ArrayList<HashMap<String,Any>>()

            for( i in dataList.indices)
            {
                if (dataList[position][RsaConstants.Ods.service_name]==dataList[i][RsaConstants.Ods.service_name])
                {
                    val map=dataList[i]

                    singleService.add(map)
                }
            }

            commonClass.putStringList(RsaConstants.Ods.singleServiceList,singleService)

            dialog.dismiss()

            manageServiceLayout(position)
        }

        dialog.setCancelable(false)

        dialog.show()
    }

    // function for managing ods service layout after the confirmatin
    private fun manageServiceLayout(position: Int) {
        when(position)
        {
            2->
            {
                llOdsServices.visibility=View.INVISIBLE

                llVehicleTypeOds.visibility=View.INVISIBLE

                llCarDetailsOds.visibility=View.VISIBLE

                tvVehicleHeadingOds.text=activity!!.resources.getString(R.string.your_road_ptrip_vehicle)

                //etVehicleModelOds.hint=activity!!.resources.getString(R.string.enter_car_model)

                ifTopBarChnagesNull(false)

                Handler().postDelayed({
                    rlTopDrawerOds.visibility=View.VISIBLE
                },200)

            }

            else->
            {
                llOdsServices.visibility=View.INVISIBLE

                llVehicleTypeOds.visibility=View.VISIBLE

                llCarDetailsOds.visibility=View.INVISIBLE

                tvVehicleHeadingOds.text=activity!!.resources.getString(R.string.your_road_ptrip_vehicle)

                //etVehicleModelOds.hint=activity!!.resources.getString(R.string.enter_car_model)

                ifTopBarChnagesNull(false)

                Handler().postDelayed({
                    rlTopDrawerOds.visibility=View.VISIBLE
                },200)
            }
        }
    }

    private fun ifTopBarChnagesNull(boolean: Boolean)
    {
        if (topBarChanges == null)
            topBarChanges = activity as TopBarChanges

        topBarChanges!!.showGoneTopBar(boolean)
    }

    ////////////////////////FOR MAP/////////////////////////////////////////////////

    private var googleApiClient: GoogleApiClient? = null

    private var locationRequest = LocationRequest.create()!!

    var location = false

    private val locationPermissionRequestCode = 12313


    //  initializing map in this method
    private fun mapInitialise(view: View?, savedInstanceState: Bundle?)
    {
        mMapView = view!!.findViewById(R.id.mvOds) as MapView

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

        checkWhenMapIsReady()
    }


    @SuppressLint("MissingPermission")
    private fun checkWhenMapIsReady()
    {
        try {
            mMapView.getMapAsync { mMap ->
                googleMap = mMap


                    val permission =
                        ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION)

                    if (permission == PackageManager.PERMISSION_GRANTED)
                    {
                        googleMap.isMyLocationEnabled = true
                    }

                    googleMap.setInfoWindowAdapter(InfoWindow())

            }
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }

    }

    // method for checking whether GPS is enabled or not
    private fun checkGPSEnable()
    {
        val apiLevel = android.os.Build.VERSION.SDK_INT
        if (isAdded)
        {
            if (apiLevel >= 23)
            {
                val permission =
                    ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION)

                if (permission != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        locationPermissionRequestCode
                    )
                }
                else
                    enableGPSAutoMatically()
            }
            else
                enableGPSAutoMatically()
        }
    }

    private fun getAddressFromLocation(lat:kotlin.Double, long:kotlin.Double)
    {


        try
        {
            commonClass.putString(RsaConstants.Ods.lat, lat.toString())

            commonClass.putString(RsaConstants.Ods.long, long.toString())

            geocoder = Geocoder(activity!!, Locale.ENGLISH)

            val address: List<Address> = geocoder.getFromLocation(lat, long, 1)

            if (address.isNotEmpty())
            {
                commonClass.putString(RsaConstants.Ods.address, address[0].getAddressLine(0))

                currentAddress = address[0].getAddressLine(0)
            }
        }

        catch (e: Exception) { }
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
                this@OdsFragment
            )

            val result: PendingResult<LocationSettingsResult> = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build())

            result.setResultCallback { p0 ->
                val status: Status = p0.status

                println("STATUS OF LOCATION $status")

                when (status.statusCode)
                {
                    LocationSettingsStatusCodes.SUCCESS ->
                    {
                        try
                        {
                           /* LocationServices.FusedLocationApi.requestLocationUpdates(
                                googleApiClient, locationRequest,
                                this@OdsFragment
                            )*/

                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                            /*assignLocationValues(mLastLocation!!)*/
                            throwMarkerOnMap(mLastLocation?.latitude.toString(),mLastLocation?.longitude.toString())
                            setDefaultMarkerOption(LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude))

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
                        checkGPSEnable()
                    }
                }
            }
        }
    }

    // Method for setting up  marker on Map
    private fun throwMarkerOnMap(latitude: String, longitude: String)
    {
        getAddressFromLocation(Double.valueOf(latitude),Double.valueOf(longitude))

        // create marker
        val markerOption = MarkerOptions().position(
            LatLng(Double.valueOf(latitude), Double.valueOf(longitude))
        )//.title(data["id"].toString())

        // Changing marker icon
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_mark_blue))

        // adding marker
        googleMap.clear()

        if (currentAddress!="")
        {
            val marker =googleMap.addMarker(markerOption)

            marker.title=currentAddress
        }

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(Double.valueOf(latitude), Double.valueOf(longitude))).zoom(14f).build()

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(Double.valueOf(latitude), Double.valueOf(longitude))))

        addCameraToMap(LatLng(Double.valueOf(latitude), Double.valueOf(longitude)))
    }


    private fun addCameraToMap(latLng:LatLng)
    {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }


    private var yourLocationMarker: MarkerOptions? = null

    fun setDefaultMarkerOption(location:LatLng)
    {
        if (yourLocationMarker == null) {
            yourLocationMarker = MarkerOptions()
        }
        yourLocationMarker!!.position(location)
    }


    private inner class InfoWindow : GoogleMap.InfoWindowAdapter
    {
        override fun getInfoContents(p0: Marker?): View?
        {
            return null
        }

        @SuppressLint("SetTextI18n", "InflateParams")
        override fun getInfoWindow(p0: Marker?): View?
        {
            val view=LayoutInflater.from(activity!!).inflate(R.layout.inflate_marker_title,null)

            view.tvMarkerTitle.text=currentAddress

            return view
        }
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

    override fun onLocationChanged(p0: Location?)
    {
        println("LOCATION >>>>>>>>>>>>>>>>>>>> " + p0!!.latitude)

        if (!location)
        {
           // val latlng=LatLng(p0.latitude,p0.longitude)

            //getAddress(latlng)

            throwMarkerOnMap(p0.latitude.toString(), p0.longitude.toString())

            setDefaultMarkerOption(LatLng(p0.latitude, p0.longitude))

            mapClicks()

            location = true
        }
        else if (location)
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this@OdsFragment)

    }

    // for handling clicks on the map
    private fun mapClicks()
    {
        googleMap.setOnMapClickListener {
            //val latlng=LatLng(it.latitude,it.longitude)

            //getAddress(latlng)

            throwMarkerOnMap(it.latitude.toString(),it.longitude.toString())
        }

        googleMap.setOnInfoWindowClickListener{

            try {
                val intent =  PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(activity!!)
                startActivityForResult(intent, 123)

            } catch (e: GooglePlayServicesRepairableException) {
                // TODO: Handle the error.
            } catch (e: GooglePlayServicesNotAvailableException ) {
                // TODO: Handle the error.
            }


        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {}

    override fun onConnected(p0: Bundle?) {
        if (isAdded)
            checkGPSEnable()
    }

    override fun onConnectionSuspended(p0: Int) {}

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


    //***************************** services API ******************************//

    //implementing services API
    private fun fetchService()
    {
        serviceHandler=RsaFragmnetPresenter(activity!!,this)

        if (commonClass.checkInternetConn(activity!!))
            serviceHandler.fetchService(commonClass.getString("x_access_token"))

        else
            commonClass.showToast(activity!!.resources.getString(R.string.internet_is_unavailable))
    }

    // handling the response of the service API
    override fun fetchingServiceReport(mainPojo: MainPojo)
    {
        for (i in mainPojo.services.indices)
        {
            if (mainPojo.services[i]["service_type"]=="ODS")
            {
                val map = mainPojo.services[i]

                map["selected"] = "0"

                dataList.add(map)
            }
        }
        odsAdapter.notifyDataSetChanged()
    }

    override fun findingFobbuReport(mainPojo: MainPojo) {}

    override fun fleetSuccessReport(mainPojo: MainPojo) {}

    override fun showLoader()
    {
        rlLoader.visibility = View.VISIBLE
    }

    override fun hideLoader()
    {
        rlLoader.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            123
            -> {
                if (resultCode == RESULT_OK)
                {
                    val place: Place = PlaceAutocomplete.getPlace(activity!!, data)

                    val lat=place.latLng.latitude

                    val long=place.latLng.longitude

                   // val latlng=LatLng(lat,long)

                    //getAddress(latlng)

                    throwMarkerOnMap(lat.toString(),long.toString())
                }

                else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
                {
                    val status: Status = PlaceAutocomplete.getStatus(activity!!, data)
                }
            }
        }
    }

    // ******************************* MAP API (geocoding) ***********************//

    // implementing geocoding API
    fun getAddress(latlng:LatLng)
    {
        println("latLng:::: ${latlng}")

        if (commonClass.checkInternetConn(activity!!))
            mapServiceHandler.getAddress(latlng,"false",activity!!.resources.getString(R.string.google_maps_key))

        else
            commonClass.showToast(activity!!.resources.getString(R.string.internet_is_unavailable))
    }


    override fun onAddressSuccessReport(mainPojo: MainPojo) {
        if (mainPojo.results.isNotEmpty())
        {
            addressList=mainPojo.results

            print("address list:::: $addressList")
        }

        commonClass.showToast(mainPojo.error_message)

    }

}
