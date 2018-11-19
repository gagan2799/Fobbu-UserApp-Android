package com.fobbu.member.android.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.location.Location
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.WaitingScreenBlue
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.interfaces.HeaderIconChanges
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Double
import java.util.*

class RSAFragment : Fragment(), GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks, LocationListener {


    lateinit var mMapView: MapView
    lateinit var googleMap: GoogleMap
    private val locationPermissionRequestCode = 12312

    private var headerIconChanges: HeaderIconChanges? = null
    private var webServiceApi: WebServiceApi? = null

    private lateinit var llPhoto1: LinearLayout
    private lateinit var llPhoto2: LinearLayout
    private lateinit var llPhoto3: LinearLayout
    private lateinit var llPhoto4: LinearLayout

    private lateinit var ivImage1: ImageView
    private lateinit var ivImage2: ImageView
    private lateinit var ivImage3: ImageView
    private lateinit var ivImage4: ImageView
    private lateinit var imgBig: ImageView
    private lateinit var imgClose: ImageView

    private lateinit var rlBigProfile: RelativeLayout

    private lateinit var tvFindFobbu: TextView

    private var strVehicleType = "2wheeler"
    private var serviceSelected = ""
    private var serviceSelectedAmount = ""
    private var strLatitude = ""
    private var strLongitude = ""

    private lateinit var rlLoader: RelativeLayout

    private lateinit var recyclerViewServices: RecyclerView
    private lateinit var fobbuServiceAdapter: FobbuServiceAdapter

    private lateinit var ivBack: ImageView
    private lateinit var llHomeServices: LinearLayout
    private lateinit var llSubPoints: LinearLayout
    private lateinit var llTwo: LinearLayout
    private lateinit var llThree: LinearLayout

    private lateinit var llScooterTwo: LinearLayout
    private lateinit var llCarTwo: LinearLayout

    private lateinit var llScooterThree: LinearLayout
    private lateinit var llCarFuelDieselThree: LinearLayout
    private lateinit var llCarFuelPetrolThree: LinearLayout

    private lateinit var etVehicleNumber: EditText
    private lateinit var tvHeading: TextView
    private lateinit var tvSubheading: TextView

    private lateinit var llUploadPics: LinearLayout
    private lateinit var tvUploadPics: TextView
    private lateinit var tvSkip: TextView

    private lateinit var llFindFobbu: LinearLayout
    private lateinit var tvHeadingFind: TextView
    private lateinit var text1: TextView
    private lateinit var text2: TextView
    private lateinit var text3: TextView
    private lateinit var text4: TextView
    private lateinit var llTerms: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rsa, container, false)

        if (view != null) {

            mapInitialise(view, savedInstanceState)

            initialise(view)

            checkGPSEnable()

            handleClick()

            serviceListApi()
        }
        return view
    }

    private fun handleClick() {

        chooseImagesClick()

        tvFindFobbu.setOnClickListener {
            findFobbuApi()
            //("Puncture cost is Rs $serviceSelectedAmount\n(extra puncture will cost Rs $serviceSelectedAmount per puncture)")
        }

        ivBack.setOnClickListener {

            when {
                llSubPoints.visibility == View.VISIBLE -> {
                    llHomeServices.visibility = View.VISIBLE
                    llSubPoints.visibility = View.GONE
                    llUploadPics.visibility = View.GONE
                    llFindFobbu.visibility = View.GONE
                }
                llUploadPics.visibility == View.VISIBLE -> {
                    llHomeServices.visibility = View.GONE
                    llSubPoints.visibility = View.VISIBLE
                    llUploadPics.visibility = View.GONE
                    llFindFobbu.visibility = View.GONE
                }

                llFindFobbu.visibility == View.VISIBLE -> {
                    llHomeServices.visibility = View.GONE
                    llSubPoints.visibility = View.GONE
                    llUploadPics.visibility = View.VISIBLE
                    llFindFobbu.visibility = View.GONE
                }

            }
        }

        llScooterTwo.setOnClickListener {
            strVehicleType = "2wheeler"
            llHomeServices.visibility = View.GONE
            llSubPoints.visibility = View.GONE
            llUploadPics.visibility = View.VISIBLE
            llFindFobbu.visibility = View.GONE
        }

        llScooterThree.setOnClickListener {
            strVehicleType = "2wheeler"
            llHomeServices.visibility = View.GONE
            llSubPoints.visibility = View.GONE
            llUploadPics.visibility = View.VISIBLE
            llFindFobbu.visibility = View.GONE
        }

        llCarTwo.setOnClickListener {
            strVehicleType = "4wheeler"
            llHomeServices.visibility = View.GONE
            llSubPoints.visibility = View.GONE
            llUploadPics.visibility = View.VISIBLE
            llFindFobbu.visibility = View.GONE
        }

        tvUploadPics.setOnClickListener {
            llHomeServices.visibility = View.GONE
            llSubPoints.visibility = View.GONE
            llUploadPics.visibility = View.GONE
            llFindFobbu.visibility = View.VISIBLE
        }

        tvSkip.setOnClickListener {
            llHomeServices.visibility = View.GONE
            llSubPoints.visibility = View.GONE
            llUploadPics.visibility = View.GONE
            llFindFobbu.visibility = View.VISIBLE
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun chooseImagesClick() {
        llPhoto1.setOnClickListener {

            if (isImageOn1) {
                showPopupViewDelete("1")
            } else {
                imageFrom = "1"
                showDocPopup()
            }

        }

        llPhoto2.setOnClickListener {

            if (isImageOn2) {
                showPopupViewDelete("2")
            } else {
                imageFrom = "2"
                showDocPopup()
            }
        }

        llPhoto3.setOnClickListener {
            if (isImageOn3) {
                showPopupViewDelete("3")
            } else {
                imageFrom = "3"
                showDocPopup()
            }
        }

        llPhoto4.setOnClickListener {
            if (isImageOn4) {
                showPopupViewDelete("4")
            } else {
                imageFrom = "4"
                showDocPopup()
            }
        }
        imgClose.setOnClickListener {

            rlBigProfile.visibility = View.GONE
        }

    }

    private fun initialise(view: View?) {

        webServiceApi = getEnv().getRetrofitMulti()

        headerIconChanges = activity as HeaderIconChanges?
        headerIconChanges!!.changeHeaderIcons(true, false, false)

        rlLoader = view!!.findViewById(R.id.rlLoader)

        llPhoto1 = view.findViewById(R.id.llPhoto1)
        llPhoto2 = view.findViewById(R.id.llPhoto2)
        llPhoto3 = view.findViewById(R.id.llPhoto3)
        llPhoto4 = view.findViewById(R.id.llPhoto4)

        ivImage1 = view.findViewById(R.id.ivImage1)
        ivImage2 = view.findViewById(R.id.ivImage2)
        ivImage3 = view.findViewById(R.id.ivImage3)
        ivImage4 = view.findViewById(R.id.ivImage4)

        imgClose = view.findViewById(R.id.imgClose)
        imgBig = view.findViewById(R.id.imgBig)
        rlBigProfile = view.findViewById(R.id.rlBigProfile)

        tvFindFobbu = view.findViewById(R.id.tvFindFobbu)

        recyclerViewServices = view.findViewById(R.id.recyclerViewServices)
        recyclerViewServices.layoutManager = GridLayoutManager(activity, 3)
        fobbuServiceAdapter = FobbuServiceAdapter()
        recyclerViewServices.adapter = fobbuServiceAdapter
        fobbuServiceAdapter.notifyDataSetChanged()

        ivBack = view.findViewById(R.id.ivBack)

        llHomeServices = view.findViewById(R.id.llHomeServices)
        llSubPoints = view.findViewById(R.id.llSubPoints)
        llThree = view.findViewById(R.id.llThree)
        llTwo = view.findViewById(R.id.llTwo)

        llCarTwo = view.findViewById(R.id.llCarTwo)
        llScooterTwo = view.findViewById(R.id.llScooterTwo)

        llCarFuelDieselThree = view.findViewById(R.id.llCarFuelDieselThree)
        llCarFuelPetrolThree = view.findViewById(R.id.llCarFuelPetrolThree)
        llScooterThree = view.findViewById(R.id.llScooterThree)


        llUploadPics = view.findViewById(R.id.llUploadPics)
        tvSkip = view.findViewById(R.id.tvSkip)
        tvUploadPics = view.findViewById(R.id.tvUploadPics)


        tvSubheading = view.findViewById(R.id.tvSubheading)
        tvHeading = view.findViewById(R.id.tvHeading)

        etVehicleNumber = view.findViewById(R.id.etVehicleNumber)

        llFindFobbu = view.findViewById(R.id.llFindFobbu)
        text1 = view.findViewById(R.id.text1)
        text2 = view.findViewById(R.id.text2)
        text3 = view.findViewById(R.id.text3)
        text4 = view.findViewById(R.id.text4)
        llTerms =view.findViewById(R.id.llTerms)
    }

    @SuppressLint("SetTextI18n")
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


        }

        builderFinal.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        builderFinal.show()
    }

    /////////////////////////////FOR API"S//////////////////////////////////////////////////
    private fun serviceListApi() {

        rlLoader.visibility = View.VISIBLE

        if (CommonClass(activity!!, activity!!).checkInternetConn(activity!!)) {

            val tokenHeader = CommonClass(activity!!, activity!!).getString("x_access_token")

            val searchServicesApi = webServiceApi!!.fetchServices(tokenHeader)

            searchServicesApi.enqueue(object : retrofit2.Callback<MainPojo> {
                override fun onResponse(call: Call<MainPojo>?, response: Response<MainPojo>?) {
                    rlLoader.visibility = View.GONE
                    println(response.toString())
                    val mainPojo = response!!.body()
                    val serviceList = mainPojo!!.services
                    dataList.clear()
                    for (i in serviceList.indices) {
                        if (serviceList[i]["service_type"].toString() == "RSA")
                            dataListServices.add(serviceList[i])
                    }
                    println("service list $dataListServices")

                    for (i in dataListServices.indices) {
                        if (i == 0) {
                            dataListServices[i]["select"] = "1"

                            serviceSelected = dataListServices[i]["_id"].toString()
                            serviceSelectedAmount = dataListServices[i]["service_price"].toString()
                        } else
                            dataListServices[i]["select"] = "0"
                    }

                    fobbuServiceAdapter.notifyDataSetChanged()

                    // recyclerViewServices.smoothScrollToPosition(0)
                }

                override fun onFailure(call: Call<MainPojo>?, t: Throwable?) {

                    rlLoader.visibility = View.GONE
                    t!!.printStackTrace()
                }
            })
        }
    }

    inner class FobbuServiceAdapter : RecyclerView.Adapter<FobbuServiceAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
            val view = LayoutInflater.from(activity!!).inflate(
                R.layout.rsa_services_adapter
                , p0, false
            )

            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            holder.relativeLayout.layoutParams = CommonClass(activity!!, activity!!).giveDynamicHeightRelativeGallery()

            holder.tvText.text = dataListServices[position]["service_name"].toString()

            if (dataListServices[position]["service_image"].toString() != "")
                Picasso.get().load(dataListServices[position]["service_image"].toString())
                    .error(R.drawable.dummy_services)
                    .into(holder.ivImage)
            else
                holder.ivImage.setImageResource(R.drawable.dummy_services)

            holder.itemView.setOnClickListener {

                llHomeServices.visibility = View.GONE
                llSubPoints.visibility = View.VISIBLE

                serviceSelected = dataListServices[position]["service_name"].toString()

                when (serviceSelected) {
                    "Flat Tyre" -> {
                        llThree.visibility = View.GONE
                        llTwo.visibility = View.VISIBLE
                        tvHeading.text = resources.getString(R.string.flat_tyre_worries)
                        tvSubheading.text = resources.getString(R.string.fix_on_the_spot)
                    }
                    "Jump Start" -> {
                        llThree.visibility = View.GONE
                        llTwo.visibility = View.VISIBLE
                        tvHeading.text = resources.getString(R.string.dead_battery_worries)
                        tvSubheading.text = resources.getString(R.string.jump_start)
                    }
                    "Fuel Delivery" -> {
                        llThree.visibility = View.VISIBLE
                        llTwo.visibility = View.GONE
                        tvHeading.text = resources.getString(R.string.empty_tanks_worries)
                        tvSubheading.text = resources.getString(R.string.deliver_real_quick)
                    }
                    "Burst Tyre" -> {
                        llThree.visibility = View.GONE
                        llTwo.visibility = View.VISIBLE
                        tvHeading.text = resources.getString(R.string.burst_tyre_worries)
                        tvSubheading.text = resources.getString(R.string.help_you_fix)
                    }
                    "Towing" -> {
                        llThree.visibility = View.GONE
                        llTwo.visibility = View.VISIBLE
                        tvHeading.text = resources.getString(R.string.double_trouble)
                        tvSubheading.text = resources.getString(R.string.we_will_connect_towing)
                    }
                    else -> {
                        llHomeServices.visibility = View.VISIBLE
                        llSubPoints.visibility = View.GONE
                    }
                }

            }
        }

        override fun getItemCount(): Int {
            return (dataListServices.size)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            var ivImage = view.findViewById(R.id.ivImage) as ImageView
            var relativeLayout = view.findViewById(R.id.relativeLayout) as RelativeLayout
            var tvText = view.findViewById(R.id.tvText) as TextView
        }
    }


    //////////////////FIND FOBBU REQUEST API  /////////////////////////
    private fun findFobbuApi() {
        rlLoader.visibility = View.VISIBLE

        val fileList = ArrayList<MultipartBody.Part>()

        for (i in 0 until dataList.size) {
            var imgProfile: MultipartBody.Part? = null
            val file = File(dataList[i].toString())
            // create RequestBody instance from file
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
            // MultipartBody.Part is used to send also the actual file name
            imgProfile = MultipartBody.Part.createFormData("photos", file.name, requestFile)
            fileList.add(imgProfile!!)
        }

        val userId = RequestBody.create(
            MediaType.parse("text/plain"),
            CommonClass(activity!!, activity!!).getString("_id")
        )

        val tokenHeader = CommonClass(activity!!, activity!!).getString("x_access_token")

        val serviceSelected = RequestBody.create(MediaType.parse("text/plain"), serviceSelected)

        val strLatitude = RequestBody.create(MediaType.parse("text/plain"), strLatitude)

        val strLongitude = RequestBody.create(MediaType.parse("text/plain"), strLongitude)

        val strVehicleType = RequestBody.create(MediaType.parse("text/plain"), strVehicleType)

        val validateUserApi = webServiceApi!!.findFobbuRequest(
            userId, serviceSelected, strLatitude, strLongitude,
            strVehicleType, fileList, tokenHeader
        )

        validateUserApi.enqueue(object : Callback<MainPojo> {
            override fun onFailure(call: Call<MainPojo>?, t: Throwable?) {
                t!!.stackTrace
                rlLoader.visibility = View.GONE
                println("api failed")
            }

            override fun onResponse(call: Call<MainPojo>?, response: Response<MainPojo>?) {
                rlLoader.visibility = View.GONE

                try {
                    val mainPojo = response!!.body()

                    println("main pojo data $mainPojo")

                    if (mainPojo!!.success == "true") {

                        fleetRequestApi(mainPojo.getData()._id)

                        CommonClass(activity!!, activity!!).putString("fobbu_request_id", mainPojo.getData()._id)

                        activity!!.startActivity(Intent(activity!!, WaitingScreenBlue::class.java))

                    } else {
                        CommonClass(activity!!, activity!!).showToast(mainPojo.message)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun fleetRequestApi(id: String) {

        if (CommonClass(activity!!, activity!!).checkInternetConn(activity!!)) {

            val tokenHeader = CommonClass(activity!!, activity!!).getString("x_access_token")

            val searchServicesApi = webServiceApi!!.findFleetOrUser(tokenHeader, id)

            searchServicesApi.enqueue(object : retrofit2.Callback<MainPojo> {
                override fun onResponse(call: Call<MainPojo>?, response: Response<MainPojo>?) {

                    println(response.toString())

                    val mainPojo = response!!.body()

                    if (mainPojo!!.success == "true") {

                    } else {
                    }
                }

                override fun onFailure(call: Call<MainPojo>?, t: Throwable?) {

                    t!!.printStackTrace()
                }
            })
        }
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

        checkWhenMapIsReady()
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

            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this@RSAFragment)
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    @SuppressLint("MissingPermission")
    private fun checkWhenMapIsReady() {
        mMapView.getMapAsync { mMap ->
            googleMap = mMap

            googleMap.setInfoWindowAdapter(InfoWindow())

        }
    }

    private fun checkGPSEnable() {
        val apiLevel = android.os.Build.VERSION.SDK_INT

        if (apiLevel >= 23) {

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
            }
        } else {

            enableGPSAutoMatically()
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
                            this@RSAFragment
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


    override fun onResume() {
        super.onResume()
        mMapView.onResume()
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


    ////////////////FOR VEHICLE IMAGE/////////////////////////
    private var isImageOn1 = false
    private var isImageOn2 = false
    private var isImageOn3 = false
    private var isImageOn4 = false

    private var file1: File? = null
    private var file2: File? = null
    private var file3: File? = null
    private var file4: File? = null

    private var imageFrom = ""

    private var dataList: ArrayList<Any> = ArrayList()
    private var dataListServices: ArrayList<HashMap<String, Any>> = ArrayList()

    private fun showPopupViewDelete(s: String) {

        val alertDialog = AlertDialog.Builder(activity!!).create()
        alertDialog.setTitle("Car Images")
        alertDialog.setMessage(null)
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "View") { _, _ ->

            when (s) {
                "1" -> {
                    rlBigProfile.visibility = View.VISIBLE
                    Glide.with(this@RSAFragment)
                        .load(file1)
                        .into(imgBig)
                }
                "2" -> {
                    rlBigProfile.visibility = View.VISIBLE
                    Glide.with(this@RSAFragment)
                        .load(file2)
                        .into(imgBig)
                }
                "3" -> {
                    rlBigProfile.visibility = View.VISIBLE
                    Glide.with(this@RSAFragment)
                        .load(file3)
                        .into(imgBig)
                }
                "4" -> {
                    rlBigProfile.visibility = View.VISIBLE
                    Glide.with(this@RSAFragment)
                        .load(file4)
                        .into(imgBig)
                }
            }
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete") { _, _ ->
            when (s) {
                "1" -> {
                    file1 = null
                    isImageOn1 = false
                    ivImage1.setImageResource(R.drawable.photo_camera)
                }
                "2" -> {
                    file2 = null
                    isImageOn2 = false
                    ivImage2.setImageResource(R.drawable.photo_camera)
                }
                "3" -> {
                    file3 = null
                    isImageOn3 = false
                    ivImage3.setImageResource(R.drawable.photo_camera)
                }
                "4" -> {
                    file4 = null
                    isImageOn4 = false
                    ivImage4.setImageResource(R.drawable.photo_camera)
                }
            }
        }
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        }

        alertDialog.show()
    }

    private fun showDocPopup() {

        val alertDialog = AlertDialog.Builder(activity!!).create()
        alertDialog.setTitle("Upload Car Images")
        alertDialog.setMessage("Please select from where you want to choose")
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Gallery") { dialogInterface, i ->
            val apiLevel = android.os.Build.VERSION.SDK_INT

            if (apiLevel >= 23) {
                //phone state
                val permission1 =
                    ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.READ_EXTERNAL_STORAGE)

                val permission2 =
                    ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                if (permission1 != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED) {
                    makeRequest2()
                } else {
                    openGallery()
                }
            } else {
                openGallery()
            }
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Camera") { dialogInterface, i ->
            val apiLevel = android.os.Build.VERSION.SDK_INT

            if (apiLevel >= 23) {
                //phone state

                val permission1 =
                    ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.READ_EXTERNAL_STORAGE)

                val permission2 =
                    ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                val permission3 = ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)

                if (permission1 != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED
                    || permission3 != PackageManager.PERMISSION_GRANTED
                ) {
                    makeRequest1()
                } else {
                    takePicture()
                }
            } else {
                takePicture()
            }
        }
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        }

        alertDialog.show()
    }

    private fun makeRequest1() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.CAMERA"
            ),
            1
        )
    }

    private fun makeRequest2() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.CAMERA"
            ),
            2
        )
    }

    private val imageCameraRequest = 100

    private val imageCameraCaptureRequest = 200
    private val placeAutoCompleteResult = 300
    private val REQUEST_PICK_CONTACT = 400

    private var mFileTemp: File? = null

    private fun takePicture() {

        createFileAndDeleteOldFile()

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val apiLevel = android.os.Build.VERSION.SDK_INT

        val mImageCaptureUri: Uri

        mImageCaptureUri = if (apiLevel >= 24) {
            FileProvider.getUriForFile(
                activity!!,
                activity!!.packageName + ".provider", mFileTemp!!
            )
        } else {
            Uri.fromFile(mFileTemp)
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri)
        try {
            startActivityForResult(intent, imageCameraCaptureRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createFileAndDeleteOldFile() {
        mFileTemp =
                File(Environment.getExternalStorageDirectory(), "vehicle_images" + System.currentTimeMillis() + ".jpg")
        if (mFileTemp!!.exists()) {
            mFileTemp!!.delete()
        }
    }

    private fun openGallery() {

        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, imageCameraRequest)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED
                    || grantResults[1] != PackageManager.PERMISSION_GRANTED
                    || grantResults[2] != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i("", "Permission has been denied by user")
                    showMessageDialog(
                        "You need to allow the permissions from\n" +
                                "Phone Settings -> Apps --> Fobbu Vendor --> Permissions\n" +
                                "to allow the permissions"
                    )

                } else {
                    Log.i("", "Permission has been granted by user")

                    takePicture()
                }
                return
            }
            2 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED
                    || grantResults[1] != PackageManager.PERMISSION_GRANTED
                    || grantResults[2] != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i("", "Permission has been denied by user")
                    showMessageDialog(
                        "You need to allow the permissions from\n" +
                                "Phone Settings -> Apps --> Fobbu Vendor --> Permissions\n" +
                                "to allow the permissions"
                    )
                } else {
                    Log.i("", "Permission has been granted by user")

                    openGallery()
                }
                return
            }

            locationPermissionRequestCode -> {
                if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    checkGPSEnable()
                } else {

                    return
                }
            }
        }
    }

    @SuppressLint("Recycle")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != AppCompatActivity.RESULT_OK) {
            return
        }

        when (requestCode) {

            imageCameraRequest ->

                try {
                    val projection = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = activity!!.contentResolver.query(
                        data!!.data,
                        projection, null, null, null
                    )
                    val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.moveToFirst()
                    val path = cursor.getString(columnIndex)
                    val imgFile = compressImage(File(path))
                    //val imgFile = File(path)
                    println("IMAGE FILE 1$imgFile")

                    //dataList.add(imgFile)
                    if (imgFile.exists()) {
                        when (imageFrom) {
                            "1" -> {
                                Glide.with(this@RSAFragment)
                                    .load(imgFile)
                                    .into(ivImage1)
                                isImageOn1 = true
                                file1 = imgFile
                            }
                            "2" -> {
                                Glide.with(this@RSAFragment)
                                    .load(imgFile)
                                    .into(ivImage2)
                                isImageOn2 = true
                                file2 = imgFile
                            }
                            "3" -> {
                                Glide.with(this@RSAFragment)
                                    .load(imgFile)
                                    .into(ivImage3)
                                isImageOn3 = true
                                file3 = imgFile
                            }
                            "4" -> {
                                Glide.with(this@RSAFragment)
                                    .load(imgFile)
                                    .into(ivImage4)
                                isImageOn4 = true
                                file4 = imgFile
                            }
                        }

                    }
                } catch (e: Exception) {
                    Log.e("tag", "Error while creating temp file", e)
                }

            imageCameraCaptureRequest ->

                if (requestCode == imageCameraCaptureRequest && resultCode == AppCompatActivity.RESULT_OK) {

                    val imgFile = compressImage(File(mFileTemp!!.path))
                    println("IMAGE FILE 2  $imgFile")

                    // dataList.add(imgFile)
                    if (imgFile.exists()) {
                        when (imageFrom) {
                            "1" -> {
                                Glide.with(this@RSAFragment)
                                    .load(imgFile)
                                    .into(ivImage1)
                                isImageOn1 = true
                                file1 = imgFile
                            }
                            "2" -> {
                                Glide.with(this@RSAFragment)
                                    .load(imgFile)
                                    .into(ivImage2)
                                isImageOn2 = true
                                file2 = imgFile
                            }
                            "3" -> {
                                Glide.with(this@RSAFragment)
                                    .load(imgFile)
                                    .into(ivImage3)
                                isImageOn3 = true
                                file3 = imgFile
                            }
                            "4" -> {
                                Glide.with(this@RSAFragment)
                                    .load(imgFile)
                                    .into(ivImage4)
                                isImageOn4 = true
                                file4 = imgFile
                            }
                        }
                    }
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showMessageDialog(message: String) {
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

    private fun compressImage(imgFile: File): File {
        val bos = ByteArrayOutputStream()

        // var myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
        val myBitmap = imageRotation(BitmapFactory.decodeFile(imgFile.absolutePath), imgFile.absolutePath)

        myBitmap.compress(Bitmap.CompressFormat.JPEG, 25, bos)

        val bitmapdata = bos.toByteArray()

        val f = imgFile
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()

        return f

    }

    private fun imageRotation(bitmap: Bitmap, path: String): Bitmap {
        val ei = ExifInterface(path)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        var rotatedBitmap: Bitmap = bitmap
        println("orientation $orientation")

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 ->
                rotatedBitmap = rotateImage(bitmap, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 ->
                rotatedBitmap = rotateImage(bitmap, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 ->
                rotatedBitmap = rotateImage(bitmap, 270F)
            ExifInterface.ORIENTATION_NORMAL ->
                rotatedBitmap = bitmap
        }

        return rotatedBitmap
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)

        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, false)
    }

    private fun getEnv(): MyApplication {
        return activity!!.application as MyApplication
    }

}