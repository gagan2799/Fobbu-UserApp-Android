package com.fobbu.member.android.fragments


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenBlue
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenWhite
import com.fobbu.member.android.fcm.FcmPushTypes
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.fragments.rsaFragmentModule.presenter.RsaLivePresenter
import com.fobbu.member.android.interfaces.HeaderIconChanges
import com.fobbu.member.android.interfaces.TopBarChanges
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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.inflate_marker_title.view.*
import org.json.JSONArray
import java.lang.Double
import java.util.*

class RSALiveFragment : Fragment(), GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks, LocationListener, ActivityView
{
    private var headerIconChanges: HeaderIconChanges? = null

    private lateinit var coordinatorLayout: CoordinatorLayout

    private lateinit var mMapView: MapView

    private lateinit var googleMap: GoogleMap

    private val locationPermissionRequestCode = 12312

    var currentAddress = ""

    var geocoder: Geocoder? = null

    private var strLatitude = ""

    private var strLongitude = ""

    var displayName = ""

    lateinit var rsaLiveHandler: RsaLivePresenter

    private lateinit var rlInformation: RelativeLayout

    private lateinit var ivTool: ImageView

    private lateinit var tvText: TextView

    private lateinit var tvCode: TextView

    private lateinit var tvNamePartner: TextView

    private lateinit var imgProfile: ImageView

    private var strWhere = ""

    private var topBarChanges: TopBarChanges? = null

    private var mobileNumber = ""

    private lateinit var ivImageCall: ImageView

    private lateinit var ivLeftDotted: ImageView

    private lateinit var ivRightDotted: ImageView

    private lateinit var tvTrack: TextView

    private lateinit var rlPickingFuel: RelativeLayout

    private lateinit var tvPickingFuel: TextView

    private lateinit var rlTools: RelativeLayout

    private var checkFirstTime = true

    private var mLastLocation: Location? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rsa_live, container, false)

        if (isAdded)
        {
            if (view != null)
            {
                mapInitialise(view, savedInstanceState)  // function for initialising map

                checkGPSEnable()  // function checking whether the location permission is given or not

                initialise(view)  // function for initialising the views and variables of the class

                handleClick()  // function for handling clicks of the class

                rsaLiveHandler = RsaLivePresenter(this.activity!!, this)

                getServices()    // implementing requests API
            }
        }
        return view
    }

    // function for initialising the views and variables of the class
    @SuppressLint("ResourceAsColor")
    private fun initialise(view: View?)
    {
        headerIconChanges = activity as HeaderIconChanges?

        headerIconChanges!!.changeHeaderIcons(false, true, true)

        topBarChanges = activity as TopBarChanges

        topBarChanges!!.showGoneTopBar(true)

        coordinatorLayout = view!!.findViewById(R.id.coordinator) as CoordinatorLayout

        initPersistentBottomsheet()  // function for showing bottom sheet

        rlInformation = view.findViewById(R.id.rlInformation)

        ivTool = view.findViewById(R.id.ivTool)

        tvText = view.findViewById(R.id.tvText)

        tvCode = view.findViewById(R.id.tvCode)

        imgProfile = view.findViewById(R.id.imgProfile)

        tvNamePartner = view.findViewById(R.id.tvNamePartner)

        ivLeftDotted = view.findViewById(R.id.ivLeftDotted)

        ivRightDotted = view.findViewById(R.id.ivRightDotted)

        tvTrack = view.findViewById(R.id.tvTrack)

        rlPickingFuel = view.findViewById(R.id.rlPickingFuel)

        tvPickingFuel = view.findViewById(R.id.tvPickingFuel)

        rlTools = view.findViewById(R.id.rlTools)

        ivImageCall = view.findViewById(R.id.ivImageCall)

        checkStatusAndNavigate()   // function for changing UI as per the status provided by  the request API
    }

    // function for handling clicks of the class
    @SuppressLint("ResourceAsColor")
    private fun handleClick()
    {

        /*  rlInformation.setOnClickListener {

              when (strWhere) {
                  "" -> {
                      ivTool.setImageResource(R.drawable.man_riding_bike)
                      tvText.text = resources.getString(R.string.fobbu_on_way)
                      strWhere = "share"

                      ivLeftDotted.setImageResource(R.drawable.dotted)
                      ivRightDotted.setImageResource(R.drawable.dotted)
                      tvTrack.setBackgroundResource(R.drawable.solid_color_grey)
                      rlPickingFuel.visibility = View.GONE
                      tvPickingFuel.visibility = View.GONE
                      rlTools.visibility = View.VISIBLE
                  }
                  "share" -> {
                      strWhere = "next"
                      ivTool.setImageResource(R.drawable.mechanic_with_cap)
                      tvText.text = resources.getString(R.string.share_4_digit_code)
                      tvCode.visibility = View.VISIBLE
                  }
                  else -> startActivity(
                      Intent(activity!!, WaitingScreenWhite::class.java).putExtra(
                          "from_where",
                          "code_valid"
                      )
                  )
              }
          }*/

        ivImageCall.setOnClickListener {
            checkPermissionForCall()
        }
    }

    /*
    @SuppressLint("SetTextI18n", "InflateParams")
    fun showPaymentPopupFinal(name: String) {

        val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val cViewFinalPopup = inflater.inflate(R.layout.fragment_builder_confirm, null)

        val builderFinal = Dialog(activity!!)

        builderFinal.requestWindowFeature(Window.FEATURE_NO_TITLE)

        builderFinal.setCancelable(false)

        builderFinal.setContentView(cViewFinalPopup)

        builderFinal.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val tvCancel = cViewFinalPopup.findViewById(R.id.tvCancel) as TextView

        val tvText = cViewFinalPopup.findViewById(R.id.tvText) as TextView
        tvText.text = name

        val tvConfirm = cViewFinalPopup.findViewById(R.id.tvConfirm) as TextView

        tvCancel.setOnClickListener {
            builderFinal.dismiss()
        }
        tvConfirm.setOnClickListener {
            builderFinal.dismiss()

            activity!!.startActivity(Intent(activity!!, WaitingScreenBlue::class.java))
        }

        builderFinal.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        builderFinal.show()
    }*/

    // function for showing bottom sheet
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

            override fun onSlide(bottomSheet: View, slideOffset: Float) { }
        })
    }

    ////////////////////////FOR MAP/////////////////////////////////////////////////
    private var googleApiClient: GoogleApiClient? = null

    private var locationRequest = LocationRequest.create()!!

    var location = false

    // function for initialising map
    private fun mapInitialise(view: View?, savedInstanceState: Bundle?)
    {
        mMapView = view!!.findViewById(R.id.mapView) as MapView

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
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onLocationChanged(p0: Location?)
    {
        println("LOCATION >>>>>>>>>>>>>>>>>>>> " + p0!!.latitude)

        if (!location)
        {
            strLatitude = p0.latitude.toString()

            strLongitude = p0.longitude.toString()

            //  setting up  marker on Map
            throwMarkerOnMap(p0.latitude.toString(), p0.longitude.toString())

            // setting up default marker on map
            setDefaultMarkerOption(LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude))

            location = true
        }
        else if (location)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this@RSALiveFragment)
        }
    }

    // function for animating camera to current location on map
    private fun addCameraToMap(latLng: LatLng)
    {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private var yourLocationMarker: MarkerOptions? = null

    // function for setting up default marker on map
    private fun setDefaultMarkerOption(location: LatLng)
    {
        if (yourLocationMarker == null)
            yourLocationMarker = MarkerOptions()

        yourLocationMarker!!.position(location)
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    // function for setting up a callback object which will be triggered when the GoogleMap instance is ready to be used.
    @SuppressLint("MissingPermission")
    private fun checkWhenMapIsReady()
    {
        mMapView.getMapAsync { mMap ->
            googleMap = mMap

            googleMap.uiSettings.isMyLocationButtonEnabled = true

            googleMap.isMyLocationEnabled = true

            googleMap.setInfoWindowAdapter(InfoWindow())
        }
    }

    // function checking whether the location permission is given or not
    private fun checkGPSEnable() {
        val apiLevel = android.os.Build.VERSION.SDK_INT

        if (isAdded)
        {
            if (apiLevel >= 23)
            {
                val permission = ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION)

                if (permission != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(activity!!, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionRequestCode)

                 else
                {
                    enableGPSAutoMatically()  // function for enabling device's GPS

                    checkWhenMapIsReady()
                }
            }
            else
            {
                enableGPSAutoMatically()  // function for enabling device's GPS

                checkWhenMapIsReady()
            }
        }
    }

    // function for enabling device's GPS
    @SuppressLint("MissingPermission")
    fun enableGPSAutoMatically()
    {
        googleApiClient = GoogleApiClient.Builder(context!!)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).build()

        googleApiClient!!.connect()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        locationRequest.interval = (1000 * 10).toLong()

        locationRequest.fastestInterval = (1000 * 5).toLong()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        // **************************
        builder.setAlwaysShow(true)// this is the key ingredient
        // **************************

        val result: PendingResult<LocationSettingsResult> = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())

        result.setResultCallback { p0 ->
            val status: Status = p0.status

            p0.locationSettingsStates

            when (status.statusCode)
            {
                LocationSettingsStatusCodes.SUCCESS ->
                {
                    try
                    {
                        /*   LocationServices.FusedLocationApi.requestLocationUpdates(
                               googleApiClient, locationRequest,
                               this@RSALiveFragment
                           )*/

                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)

                        // setting up  marker on Map
                        throwMarkerOnMap(mLastLocation?.latitude.toString(), mLastLocation?.longitude.toString())

                        // setting up default marker on map
                        setDefaultMarkerOption(LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude))

                    }
                    catch (e: IntentSender.SendIntentException) { }
                }

                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                {
                    status.startResolutionForResult(activity!!, 1000)
                }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> { }
            }
        }
    }

    // function for getting address from latitude and longitude
    private fun getAddressFromLocation(lat: kotlin.Double, long: kotlin.Double)
    {
        try
        {
            geocoder = Geocoder(activity!!, Locale.ENGLISH)

            val address: List<Address> = geocoder!!.getFromLocation(lat, long, 1)

            if (address.isNotEmpty())
            {
                CommonClass(activity!!, activity!!).putString(RsaConstants.Ods.address, address[0].getAddressLine(0))

                currentAddress = address[0].getAddressLine(0)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    // Method for setting up  marker on Map
    private fun throwMarkerOnMap(latitude: String, longitude: String)
    {
        try
        {
            // create marker
             val markerOption = MarkerOptions().position(LatLng(Double.valueOf(latitude), Double.valueOf(longitude)))//.title(data["id"].toString())

            // Changing marker icon
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_mark_blue))

            // adding marker
            googleMap.clear()

            //  getting address from latitude and longitude
            getAddressFromLocation(Double.valueOf(latitude), Double.valueOf(longitude))

            if (currentAddress != "")
            {
                // adding marker
                val marker = googleMap.addMarker(markerOption)

                marker.title = currentAddress
            }

            val cameraPosition = CameraPosition.Builder().target(LatLng(Double.valueOf(latitude), Double.valueOf(longitude))).zoom(14f).build()

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(Double.valueOf(latitude), Double.valueOf(longitude))))

// animating camera to current location on map
            addCameraToMap(LatLng(Double.valueOf(latitude), Double.valueOf(longitude)))
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        // googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onPause()
    {
        super.onPause()

        mMapView.onPause()
    }

    override fun onDestroy()
    {
        super.onDestroy()

        destroyEverythingMethod()
    }

    private fun destroyEverythingMethod()
    {
        try {
            mMapView.onDestroy()

            handlerrLiveApi.removeCallbacksAndMessages(null)

            handlerr.removeCallbacksAndMessages(null)

            activity!!.unregisterReceiver(changeRSALiveScreenReceiver)
        }
        catch (e: java.lang.Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onLowMemory()
    {
        super.onLowMemory()

        mMapView.onLowMemory()
    }

    // inner class for customising the info window of the marker
    private inner class InfoWindow : GoogleMap.InfoWindowAdapter
    {
        override fun getInfoContents(p0: Marker?): View? {
            return null
        }

        @SuppressLint("SetTextI18n", "InflateParams")
        override fun getInfoWindow(p0: Marker?): View?
        {
            val view = LayoutInflater.from(activity!!).inflate(R.layout.inflate_marker_title, null)

            view.tvMarkerTitle.text = currentAddress

            return view
        }
    }

    override fun onResume()
    {
        super.onResume()

        mMapView.onResume()

        val filter = IntentFilter(FcmPushTypes.Types.inRouteRequestBroadCast)

        activity!!.registerReceiver(changeRSALiveScreenReceiver, filter)
    }

    // initialising changeRSALiveScreenReceiver Broadcast Receiver
    private val changeRSALiveScreenReceiver = object : BroadcastReceiver()
    {
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)

        @SuppressLint("ResourceAsColor")

        override fun onReceive(context: Context, intent: Intent)
        {
            println("ON RECEIVE BROADCAST " + intent.getStringExtra("navigate_to"))

            checkStatusAndNavigate()  // function for changing UI as per the status provided by  the request API
        }
    }

// function for updating the text which tells the user about the state of the partner
    @SuppressLint("SetTextI18n")
    private fun checkStatusAndUpdateText()
{
     try
     {
         val status = CommonClass(activity!!, activity!!).getString(RsaConstants.RsaTypes.checkStatus)

            when (status)
            {
                FcmPushTypes.Types.accept ->
                    tvText.text = """${activity!!.resources.getString(R.string.please_wait_fobbu_msg)}$displayName ${activity!!.resources.getString(
                                R.string.gathering_tools__msg
                            )}"""

                FcmPushTypes.Types.moneyPaid ->
                {
                    if(CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.serviceNameSelected)== RsaConstants.ServiceName.fuelDelivery)
                    {
                        ivLeftDotted.setImageResource(R.drawable.dotted_gray)

                        ivRightDotted.setImageResource(R.drawable.dotted_gray)

                        tvTrack.setBackgroundResource(R.drawable.solid_color_grey)

                        rlPickingFuel.visibility = View.VISIBLE

                        tvPickingFuel.visibility = View.VISIBLE

                        rlTools.visibility = View.GONE
                    }
                    else
                    {
                        ivTool.setImageResource(R.drawable.man_riding_bike)
                        if(CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.serviceNameSelected)!=
                            RsaConstants.ServiceName.jumpStart)
                            tvText.text = displayName + " " + resources.getString(R.string.fobbu_on_way)

                        else
                            tvText.text = """${activity!!.resources.getString(R.string.please_wait_fobbu_msg)}$displayName ${activity!!.resources.getString(
                                        R.string.gathering_tools__msg
                                    )}"""

                        strWhere = "share"

                        //   updateLiveLocation()

                        ivLeftDotted.setImageResource(R.drawable.dotted)

                        ivRightDotted.setImageResource(R.drawable.dotted)

                        tvTrack.background = resources.getDrawable(R.drawable.solid_color_red)

                        rlPickingFuel.visibility = View.GONE

                        tvPickingFuel.visibility = View.GONE

                        rlTools.visibility = View.VISIBLE
                    }
                }

                FcmPushTypes.Types.inRouteRequest ->
                {
                    ivTool.setImageResource(R.drawable.man_riding_bike)

                    tvText.text = displayName + " " + resources.getString(R.string.fobbu_on_way)

                    strWhere = "share"

                    ivLeftDotted.setImageResource(R.drawable.dotted)

                    ivRightDotted.setImageResource(R.drawable.dotted)

                    tvTrack.background = resources.getDrawable(R.drawable.solid_color_red)

                    rlPickingFuel.visibility = View.GONE

                    tvPickingFuel.visibility = View.GONE

                    rlTools.visibility = View.VISIBLE
                }

                FcmPushTypes.Types.newPin ->
                {
                    ivTool.setImageResource(R.drawable.mechanic_with_cap)

                    tvText.text = resources.getString(R.string.share_4_digit_code)

                    if (CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.otpStart).length == 3)
                        tvCode.text = "0" + CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.otpStart)

                    else
                        tvCode.text = CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.otpStart)

                    tvCode.visibility = View.VISIBLE
                }

                FcmPushTypes.Types.otpGenerated ->
                {
                    ivTool.setImageResource(R.drawable.mechanic_with_cap)

                    tvText.text = resources.getString(R.string.share_4_digit_code)

                    if (CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.otpStart).length == 3)
                        tvCode.text = "0" + CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.otpStart)

                    else
                        tvCode.text = CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.otpStart)

                    tvCode.visibility = View.VISIBLE
                }
                FcmPushTypes.Types.otpVerified ->
                {
                    println("HERE IN LIVE ")

                    println("DISPLAY NAME VERIFIED >>> " + displayName)
                }

                FcmPushTypes.Types.fuelDefaultScreen ->
                {
                    ivLeftDotted.setImageResource(R.drawable.dotted_gray)

                    ivRightDotted.setImageResource(R.drawable.dotted_gray)

                    tvTrack.setBackgroundResource(R.drawable.solid_color_grey)

                    rlPickingFuel.visibility = View.VISIBLE

                    tvPickingFuel.visibility = View.VISIBLE

                    rlTools.visibility = View.GONE
                }

                FcmPushTypes.Types.requestCancelled ->
                {
                    val intent = Intent()

                    intent.action = FcmPushTypes.Types.fromAPIBroadCast

                    activity!!.sendBroadcast(intent)
                }
            }

        }
     catch (e: java.lang.Exception)
     {
            e.printStackTrace()
        }
    }

// function for changing UI as per the status provided by  the request API
    @SuppressLint("SetTextI18n")
    private fun checkStatusAndNavigate()
{
    try {
            val status = CommonClass(activity!!, activity!!).getString(RsaConstants.RsaTypes.checkStatus)

            println("HERE IN LIVE FRAGMENT $status")

            when (status)
            {
                FcmPushTypes.Types.accept ->
                    tvText.text = """${activity!!.resources.getString(R.string.please_wait_fobbu_msg)}$displayName ${activity!!.resources.getString(
                                R.string.gathering_tools__msg
                            )}"""

                FcmPushTypes.Types.inRouteRequest ->
                {
                    ivTool.setImageResource(R.drawable.man_riding_bike)

                    tvText.text = displayName + " " + resources.getString(R.string.fobbu_on_way)

                    strWhere = "share"

                    //updateLiveLocation()

                    ivLeftDotted.setImageResource(R.drawable.dotted)

                    ivRightDotted.setImageResource(R.drawable.dotted)

                    tvTrack.background = resources.getDrawable(R.drawable.solid_color_red)

                    rlPickingFuel.visibility = View.GONE

                    tvPickingFuel.visibility = View.GONE

                    rlTools.visibility = View.VISIBLE
                }

                FcmPushTypes.Types.moneyPaid ->
                {
                    if(CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.serviceNameSelected)==
                        RsaConstants.ServiceName.fuelDelivery)
                    {
                        ivLeftDotted.setImageResource(R.drawable.dotted_gray)

                        ivRightDotted.setImageResource(R.drawable.dotted_gray)

                        tvTrack.setBackgroundResource(R.drawable.solid_color_grey)

                        rlPickingFuel.visibility = View.VISIBLE

                        tvPickingFuel.visibility = View.VISIBLE

                        rlTools.visibility = View.GONE
                    }
                    else
                    {
                        ivTool.setImageResource(R.drawable.man_riding_bike)

                        tvText.text = displayName + " " + resources.getString(R.string.fobbu_on_way)

                        strWhere = "share"

                        // updateLiveLocation()

                        ivLeftDotted.setImageResource(R.drawable.dotted)

                        ivRightDotted.setImageResource(R.drawable.dotted)

                        tvTrack.background = resources.getDrawable(R.drawable.solid_color_red)

                        rlPickingFuel.visibility = View.GONE

                        tvPickingFuel.visibility = View.GONE

                        rlTools.visibility = View.VISIBLE
                    }
                }

                FcmPushTypes.Types.newPin ->
                {
                    ivTool.setImageResource(R.drawable.mechanic_with_cap)

                    tvText.text = resources.getString(R.string.share_4_digit_code)

                    if (CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.otpStart).length == 3)
                        tvCode.text = "0" + CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.otpStart)

                    else
                        tvCode.text = CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.otpStart)

                    tvCode.visibility = View.VISIBLE
                }

                FcmPushTypes.Types.otpGenerated ->
                {
                    ivTool.setImageResource(R.drawable.mechanic_with_cap)

                    tvText.text = resources.getString(R.string.share_4_digit_code)

                    if (CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.otpStart).length == 3)
                        tvCode.text = "0" + CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.otpStart)

                    else
                        tvCode.text = CommonClass(activity!!, activity!!).getString(RsaConstants.ServiceSaved.otpStart)

                    tvCode.visibility = View.VISIBLE
                }
                FcmPushTypes.Types.otpVerified ->
                {
                    println("HERE IN LIVE ")

                    println("DISPLAY NAME VERIFIED >>> " + displayName)

                    startActivity(Intent(activity!!, WaitingScreenWhite::class.java).putExtra("from_where", "code_valid"))
                }

                FcmPushTypes.Types.fuelDefaultScreen ->
                {
                    ivLeftDotted.setImageResource(R.drawable.dotted_gray)

                    ivRightDotted.setImageResource(R.drawable.dotted_gray)

                    tvTrack.setBackgroundResource(R.drawable.solid_color_grey)

                    rlPickingFuel.visibility = View.VISIBLE

                    tvPickingFuel.visibility = View.VISIBLE

                    rlTools.visibility = View.GONE
                }

                FcmPushTypes.Types.requestCancelled ->
                {
                    val intent = Intent()

                    intent.action = FcmPushTypes.Types.fromAPIBroadCast

                    activity!!.sendBroadcast(intent)
                }
            }
        }
    catch (e: Exception)
    {
            e.printStackTrace()
        }
    }



    override fun showLoader() {
    }

    override fun hideLoader() {

    }

    // function for checking call permission
    private fun checkPermissionForCall()
    {
        val apiLevel = android.os.Build.VERSION.SDK_INT

        if (isAdded) {
            if (apiLevel >= 23)
            {
                //phone state
                val permission1 = ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.CALL_PHONE)

                if (permission1 != PackageManager.PERMISSION_GRANTED)
                    makeRequest()

                else
                    dialNumber()  // function for calling
            }
            else
                dialNumber()  // function for calling

        }
    }

    // function for calling
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode)
        {
            1 ->
            {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    // Method for opening dialog containing message
                    showMessageDialog(activity!!.resources.getString(R.string.permission_message))
                } else
                    dialNumber()  // function for calling

                return
            }

            locationPermissionRequestCode ->
            {
                if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    checkGPSEnable()  // function checking whether the location permission is given or not

                 else
                    checkGPSEnable()  // function checking whether the location permission is given or not
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

    // *************************** update_location_request API **************************//

    // implementing update_location_request API
    private fun updateLiveLocation()
    {
        if (CommonClass(activity!!, activity!!).checkInternetConn(activity!!))
        {
            handlerrLiveApi = Handler()

            handlerrLiveApi.postDelayed(object : Runnable {
                override fun run()
                {
                    //do something
                    println("HIT LIVE LOCATION API>>>>>>>")

                    getServices()    // implementing requests API

                    handlerrLiveApi.postDelayed(this, 5000)
                }
            }, 500)

            // schedule the task to run starting now and then every minute...
            //  timer.schedule (hourlyTask, 1L, 1000*60)
        }
        else
            CommonClass(activity!!, activity!!).showToast(activity!!.resources.getString(R.string.internet_is_unavailable))
    }

    //*************************** requests API ************************//

    // implementing requests API
    private fun getServices()
    {
        println("HERE IN RSA SERVICE MAIN>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

        try
        {
            if (CommonClass(activity!!, activity!!).checkInternetConn(activity!!))

                rsaLiveHandler.getService(CommonClass(activity!!, activity!!).getString("x_access_token"), CommonClass(this.activity!!, this.activity!!).getString(RsaConstants.ServiceSaved.fobbuRequestId))

            else
                CommonClass(activity!!, activity!!).showToast(activity!!.resources.getString(R.string.internet_is_unavailable))
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

// handling the response of the request API
    @SuppressLint("SetTextI18n")
    override fun onRequestSuccessReport(mainPojo: MainPojo)
{
        if (mainPojo.success == "true")
        {
            displayName = mainPojo.getData().partner.display_name

            println("DISPLAY NAME FIRST>>> " + mainPojo.getData().partner.display_name)

            if (mainPojo.getData().otp != "")
            {
                val otp = if (mainPojo.getData().otp.contains("\\.".toRegex()))
                    mainPojo.getData().otp.split("\\.".toRegex())[0]

            else
                    mainPojo.getData().otp

                try
                {
                    CommonClass(activity!!, activity!!).putString(RsaConstants.ServiceSaved.otpStart, otp)
                }
                catch (e: java.lang.Exception)
                {
                    e.printStackTrace()
                }
            }

            if (checkFirstTime)
            {
                checkFirstTime = false

                if (!mainPojo.getData().user.profile.isBlank())
                {
                    if (mainPojo.getData().partner.profile.isNotEmpty() || mainPojo.getData().partner.profile !=null)
                        Picasso.get().load(mainPojo.getData().partner.profile).error(R.drawable.dummy_pic).into(imgProfile)

                    else
                        imgProfile.setImageResource(R.drawable.dummy_pic)
                }

                tvNamePartner.text = displayName

                mobileNumber = mainPojo.getData().partner.mobile_number

                try
                {
                    println("STATUS >>>> " + mainPojo.getData().status)
                    if (mainPojo.getData().status != "")
                        CommonClass(activity!!, activity!!).putString(RsaConstants.RsaTypes.checkStatus, mainPojo.getData().status)

                }
                catch (e: java.lang.Exception)
                {
                    e.printStackTrace()
                }
                checkStatusAndUpdateText()  // updating the text which tells the user about the state of the partner

                checkStatusAndNavigate()  // function for changing UI as per the status provided by  the request API
            }
            tvNamePartner.text = displayName

            mobileNumber = mainPojo.getData().partner.mobile_number

            checkStatusAndUpdateText()  // updating the text which tells the user about the state of the partner

            if (!(mainPojo.getData().lat_long.isNullOrEmpty()))
            {
                val jsonArray = JSONArray(mainPojo.getData().lat_long)

                val polyLineList: ArrayList<LatLng> = ArrayList()

                for (i in 0..(jsonArray.length() - 1))
                {
                    val item = jsonArray.getJSONObject(i)

                    val hmap = HashMap<String, String>()

                    hmap["lat"] = item.getString("lat")

                    hmap["long"] = item.getString("long")

                    hmap["set"] = "0"

                    val latLng = LatLng(item.getString("lat").toDouble(), item.getString("long").toDouble())

                    // if (!listCheck.contains(item.getString("lat")))
                    //{
                    listCheck.add(item.getString("lat"))

                    polyLineList.add(latLng)
                    // }
                }
                println("lat long list:::::::: $polyLineList")

                liveTracking(polyLineList)  // function for live tracking
            }
        }
    }

    var listCheck = java.util.ArrayList<String>()

    lateinit var marker: Marker

    var handlerr = Handler()

    var handlerrLiveApi = Handler()

    val delay: Long = 500 //milliseconds

    val delayanim: Long = 3000 //milliseconds

    private var index: Int = 0

    private var next: Int = 0

    private var startPosition: LatLng? = null

    private var endPosition: LatLng? = null

    private var v: Float = 0.toFloat()

    private var lat: kotlin.Double = 0.toDouble()

    private var lng: kotlin.Double = 0.toDouble()

    private var firstTime = true

        // function for live tracking
    private fun liveTracking(polyLineList: ArrayList<LatLng>)
    {
        if (firstTime)
        {
            firstTime = false

            marker = googleMap.addMarker(MarkerOptions().position(polyLineList!![0]).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_mark_blue)))

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(polyLineList!![0]).zoom(15.5f).build()))
        }
        else
        {
            if (polyLineList!!.size >= 2)
            {
                /* marker = googleMap.addMarker(
                     MarkerOptions().position(polyLineList!![0])
                         .flat(true)
                         .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_mark_blue))
                 )

                 googleMap.moveCamera(
                     CameraUpdateFactory
                         .newCameraPosition(
                             CameraPosition.Builder()
                                 .target(polyLineList!![0])
                                 .zoom(15.5f)
                                 .build()
                         )
                 )*/
                handlerr = Handler()

                index = -1

                next = 1

                handlerr.postDelayed(object : Runnable
                {
                    override fun run()
                    {
                        if (index < polyLineList!!.size - 1)
                        {
                            index++

                            next = index + 1
                        }

                        if (index < polyLineList!!.size - 1)
                        {
                            startPosition = polyLineList!![index]

                            endPosition = polyLineList!![next]
                        }

                        if (next < (polyLineList!!.size - 1))
                        {
                            println("CHECK VALUE HERE START Lat >> " + polyLineList!![index].latitude)

                            println("CHECK VALUE HERE START Long >> " + polyLineList!![index].longitude)

                            val valueAnimator = ValueAnimator.ofFloat(0f, 1f)

                            valueAnimator.duration = delayanim

                            valueAnimator.interpolator = LinearInterpolator()

                            valueAnimator.addUpdateListener { valueAnimator ->
                                v = valueAnimator.animatedFraction

                                lng = v * endPosition!!.longitude + (1 - v) * startPosition!!.longitude

                                lat = v * endPosition!!.latitude + (1 - v) * startPosition!!.latitude

                                val newPos = LatLng(lat, lng)

                                marker.position = newPos

                                marker.setAnchor(0.5f, 0.5f)

                                if (next % 5 == 0)
                                    marker.rotation = getBearing(startPosition!!, newPos)  // function for getting bearing

                                googleMap.moveCamera(
                                    CameraUpdateFactory
                                        .newCameraPosition(
                                            CameraPosition.Builder()
                                                .target(newPos)
                                                .zoom(15.5f)
                                                .bearing(90f) // Sets the orientation of the camera to east
                                                .tilt(30f)
                                                .build()))
                            }
                            valueAnimator.start()

                            println("CHECK VALUE HERE END Lat >> " + polyLineList!![next].latitude)

                            println("CHECK VALUE HERE END Long >> " + polyLineList!![next].longitude)
                        }
                        println("CHECK VALUE HERE >> $index >> $next >> $lat >> $lng >> SIZE >> ${polyLineList!!.size}")

                        handlerr.postDelayed(this, delayanim)
                    }
                }, delayanim)
            }
        }
    }

    // function for getting bearing
    private fun getBearing(begin: LatLng, end: LatLng): Float
    {
        val lat = Math.abs(begin.latitude - end.latitude)

        val lng = Math.abs(begin.longitude - end.longitude)

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return Math.toDegrees(Math.atan(lng / lat)).toFloat()

        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 90).toFloat()

        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (Math.toDegrees(Math.atan(lng / lat)) + 180).toFloat()

        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 270).toFloat()

        return -1f
    }

}