package com.fobbu.member.android.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.widget.*
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
import java.lang.Double

class RSALiveFragment : Fragment(), GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks, LocationListener, ActivityView {


    private var headerIconChanges: HeaderIconChanges? = null

    private lateinit var coordinatorLayout: CoordinatorLayout

    private lateinit var mMapView: MapView
    private lateinit var googleMap: GoogleMap
    private val locationPermissionRequestCode = 12312

    private var strLatitude = ""
    private var strLongitude = ""

    var displayName=""

    lateinit var rsaLiveHandler: RsaLivePresenter

    private lateinit var rlInformation: RelativeLayout
    private lateinit var ivTool: ImageView
    private lateinit var tvText: TextView
    private lateinit var tvCode: TextView
    private lateinit var tvName: TextView
    private lateinit var imgProfile: ImageView
    private var strWhere = ""
    private var topBarChanges: TopBarChanges? = null
    private var mobileNumber = ""
    private lateinit var ivImageCall:ImageView

    private lateinit var ivLeftDotted: ImageView
    private lateinit var ivRightDotted: ImageView
    private lateinit var tvTrack: TextView
    private lateinit var rlPickingFuel: RelativeLayout
    private lateinit var tvPickingFuel: TextView
    private lateinit var rlTools: RelativeLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rsa_live, container, false)

        if (isAdded)
        {
            if (view != null)
            {
                mapInitialise(view, savedInstanceState)

                checkGPSEnable()

                initialise(view)

                handleClick()

                rsaLiveHandler = RsaLivePresenter(this.activity!!, this)
                rsaLiveHandler.getService(
                    CommonClass(activity!!, activity!!).getString("x_access_token"),
                    CommonClass(this.activity!!, this.activity!!).getString(
                        RsaConstants.ServiceSaved.fobbuRequestId
                    )
                )
            }
        }

        return view
    }

    @SuppressLint("ResourceAsColor")
    private fun handleClick() {

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

    @SuppressLint("ResourceAsColor")
    private fun initialise(view: View?) {

        headerIconChanges = activity as HeaderIconChanges?
        headerIconChanges!!.changeHeaderIcons(false, true, true)

        topBarChanges = activity as TopBarChanges
        topBarChanges!!.showGoneTopBar(true)

        coordinatorLayout = view!!.findViewById(R.id.coordinator) as CoordinatorLayout
        initPersistentBottomsheet()

        rlInformation = view.findViewById(R.id.rlInformation)
        ivTool = view.findViewById(R.id.ivTool)
        tvText = view.findViewById(R.id.tvText)
        tvCode = view.findViewById(R.id.tvCode)
        imgProfile = view.findViewById(R.id.imgProfile)
        tvName = view.findViewById(R.id.tvName)

        ivLeftDotted = view.findViewById(R.id.ivLeftDotted)
        ivRightDotted = view.findViewById(R.id.ivRightDotted)
        tvTrack = view.findViewById(R.id.tvTrack)
        rlPickingFuel = view.findViewById(R.id.rlPickingFuel)
        tvPickingFuel = view.findViewById(R.id.tvPickingFuel)
        rlTools = view.findViewById(R.id.rlTools)

        ivImageCall = view.findViewById(R.id.ivImageCall)
        checkStatusAndNavigate()
    }

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
    }

    private fun initPersistentBottomsheet() {
        val persistentbottomSheet = coordinatorLayout.findViewById<LinearLayout>(R.id.bottomsheet)
        val rlBottomSheet = persistentbottomSheet.findViewById(R.id.rlBottomSheet) as RelativeLayout
        val behavior = BottomSheetBehavior.from<View>(persistentbottomSheet)


        rlBottomSheet.setOnClickListener {
            if (behavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }

        behavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                //showing the different states
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events

            }
        })

    }

    ////////////////////////FOR MAP/////////////////////////////////////////////////
    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest = LocationRequest.create()!!
    var location = false

    private fun mapInitialise(view: View?, savedInstanceState: Bundle?) {
        mMapView = view!!.findViewById(R.id.mapView) as MapView
        mMapView.onCreate(savedInstanceState)

        mMapView.onResume()// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onLocationChanged(p0: Location?) {

        println("LOCATION >>>>>>>>>>>>>>>>>>>> " + p0!!.latitude)

        if (!location) {

            strLatitude = p0.latitude.toString()
            strLongitude = p0.longitude.toString()

            throwMarkerOnMap(p0.latitude.toString(), p0.longitude.toString())

            location = true
        } else if (location) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this@RSALiveFragment)
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    @SuppressLint("MissingPermission")
    private fun checkWhenMapIsReady() {
        mMapView.getMapAsync { mMap ->
            googleMap = mMap

            googleMap.uiSettings.isMyLocationButtonEnabled = true

            googleMap.isMyLocationEnabled = true

            googleMap.setInfoWindowAdapter(InfoWindow())
        }
    }

    private fun checkGPSEnable() {
        val apiLevel = android.os.Build.VERSION.SDK_INT

        if (isAdded)
        {
            if (apiLevel >= 23)
            {
                val permission =
                    ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION)

                if (permission != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                        activity!!,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        locationPermissionRequestCode
                    )
                } else {
                    enableGPSAutoMatically()
                    checkWhenMapIsReady()
                }
            } else {
                enableGPSAutoMatically()
                checkWhenMapIsReady()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun enableGPSAutoMatically() {

        googleApiClient = GoogleApiClient.Builder(context!!)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).build()
        googleApiClient!!.connect()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (1000 * 10).toLong()
        locationRequest.fastestInterval = (1000 * 5).toLong()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // **************************
        builder.setAlwaysShow(true)// this is the key ingredient
        // **************************

        val result: PendingResult<LocationSettingsResult> = LocationServices.SettingsApi
            .checkLocationSettings(googleApiClient, builder.build())

        result.setResultCallback { p0 ->
            val status: Status = p0.status
            p0.locationSettingsStates
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    try {
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                            googleApiClient, locationRequest,
                            this@RSALiveFragment
                        )
                    } catch (e: IntentSender.SendIntentException) {
                    }
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    status.startResolutionForResult(activity!!, 1000)
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                }
            }
        }
    }

    private fun throwMarkerOnMap(latitude: String, longitude: String) {

        // create marker
        val marker = MarkerOptions().position(
            LatLng(Double.valueOf(latitude), Double.valueOf(longitude))
        )//.title(data["id"].toString())

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_mark_blue))

        // adding marker
        googleMap.addMarker(marker)

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(Double.valueOf(latitude), Double.valueOf(longitude))).zoom(14f).build()

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
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

    override fun onResume() {
        super.onResume()
        mMapView.onResume()

        val filter = IntentFilter(FcmPushTypes.Types.inRouteRequestBroadCast)
        activity!!.registerReceiver(changeRSALiveScreenReceiver, filter)
    }

    private val changeRSALiveScreenReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        @SuppressLint("ResourceAsColor")
        override fun onReceive(context: Context, intent: Intent) {

            println("ON RECEIVE BROADCAST " + intent.getStringExtra("navigate_to"))

            checkStatusAndNavigate()
        }
    }




    @SuppressLint("SetTextI18n")
    private fun checkStatusAndNavigate() {

        val status = CommonClass(activity!!,activity!!).getString(RsaConstants.RsaTypes.checkStatus)

        println("HERE IN LIVE FRAGMENT $status")

        when (status) {
            FcmPushTypes.Types.inRouteRequest ->
            {
                ivTool.setImageResource(R.drawable.man_riding_bike)
                tvText.text = displayName+" "+resources.getString(R.string.fobbu_on_way)
                strWhere = "share"

                ivLeftDotted.setImageResource(R.drawable.dotted)
                ivRightDotted.setImageResource(R.drawable.dotted)
                tvTrack.background = resources.getDrawable(R.drawable.solid_color_red)
                rlPickingFuel.visibility = View.GONE
                tvPickingFuel.visibility = View.GONE
                rlTools.visibility = View.VISIBLE

            }
            FcmPushTypes.Types.newPin -> {
                ivTool.setImageResource(R.drawable.mechanic_with_cap)
                tvText.text = resources.getString(R.string.share_4_digit_code)
                if (CommonClass(activity!!,activity!!).getString(RsaConstants.ServiceSaved.otpStart).length==3)
                    tvCode.text = "0"+CommonClass(activity!!,activity!!).getString(RsaConstants.ServiceSaved.otpStart)

                else
                tvCode.text = CommonClass(activity!!,activity!!).getString(RsaConstants.ServiceSaved.otpStart)
                tvCode.visibility = View.VISIBLE
            }

            FcmPushTypes.Types.otpGenerated -> {
                ivTool.setImageResource(R.drawable.mechanic_with_cap)
                tvText.text = resources.getString(R.string.share_4_digit_code)
                if (CommonClass(activity!!,activity!!).getString(RsaConstants.ServiceSaved.otpStart).length==3)
                    tvCode.text = "0"+CommonClass(activity!!,activity!!).getString(RsaConstants.ServiceSaved.otpStart)

                else
                tvCode.text = CommonClass(activity!!,activity!!).getString(RsaConstants.ServiceSaved.otpStart)
                tvCode.visibility = View.VISIBLE
            }
            FcmPushTypes.Types.otpVerified ->
            {
                println("HERE IN LIVE ")

                startActivity(
                    Intent(
                        activity!!,
                        WaitingScreenWhite::class.java
                    ).putExtra("from_where", "code_valid")
                )
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

            FcmPushTypes.Types.requestCancelled -> {
                val intent = Intent()
                intent.action = FcmPushTypes.Types.fromAPIBroadCast
                activity!!.sendBroadcast(intent)
            }
        }

    }


    @SuppressLint("SetTextI18n")
    override fun onRequestSuccessReport(mainPojo: MainPojo) {
        if (mainPojo.success == "true")
        {
            if (!mainPojo.getData().user.profile.isNullOrBlank())
                {
                    if (mainPojo.getData().partner.profile.isNotEmpty())
                    Picasso.get().load(mainPojo.getData().partner.profile).error(R.drawable.dummy_pic).into(imgProfile)

                    else
                        imgProfile.setImageResource(R.drawable.dummy_pic)
                }

            displayName=mainPojo.getData().partner.display_name
                tvName.text = displayName
            tvText.text=
                    """${activity!!.resources.getString(R.string.please_wait_fobbu_msg)}$displayName ${activity!!.resources.getString(
                        R.string.gathering_tools__msg
                    )}"""

                mobileNumber = mainPojo.getData().partner.mobile_number
        }
    }

    override fun showLoader() {
    }

    override fun hideLoader() {

    }

    private fun checkPermissionForCall() {
        val apiLevel = android.os.Build.VERSION.SDK_INT

        if (isAdded)
        {
            if (apiLevel >= 23) {
                //phone state

                val permission1 =
                    ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.CALL_PHONE)

                if (permission1 != PackageManager.PERMISSION_GRANTED) {
                    makeRequest()
                } else {
                    dialNumber()
                }
            } else {
                dialNumber()
            }
        }
    }

    private fun dialNumber() {

        if (mobileNumber != "")
            CommonClass(activity!!, activity!!).callOnPhone(mobileNumber)
    }

    private fun makeRequest() {
        requestPermissions(
            arrayOf(
                "android.permission.CALL_PHONE"
            ),
            1
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("", "Permission has been denied by user")
                    showMessageDialog(
                        "You need to allow the permissions from\n" +
                                "Phone Settings -> Apps --> Fobbu Member --> Permissions\n" +
                                "to allow the permissions"
                    )

                } else {
                    Log.i("", "Permission has been granted by user")

                    dialNumber()
                }
                return
            }

            locationPermissionRequestCode -> {
                if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    checkGPSEnable()
                } else {

                    checkGPSEnable()
                }
            }
        }
    }

    // Method for opening dialog containing message
    private fun showMessageDialog(message: String)
    {
        val alertDialog = AlertDialog.Builder(
            activity!!
            , R.style.MyDialogTheme
        ).create()
        alertDialog.setMessage(message)
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "Ok"
        ) { dialog
            , _ ->
            dialog.dismiss()
        }

        alertDialog.show()

        val messageText: TextView = alertDialog!!.findViewById(android.R.id.message)!!

        messageText.gravity = Gravity.LEFT
    }

}