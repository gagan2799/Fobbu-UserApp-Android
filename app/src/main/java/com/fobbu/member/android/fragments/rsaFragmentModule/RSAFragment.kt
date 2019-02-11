package com.fobbu.member.android.fragments.rsaFragmentModule

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Location
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.bumptech.glide.Glide
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboardActivity.DashboardActivity
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenBlue

import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.backgroundServices.FetchStatusAPI
import com.fobbu.member.android.fragments.rsaFragmentModule.adapter.FobbuServiceAdapter
import com.fobbu.member.android.fragments.rsaFragmentModule.presenter.RsaFragmentHandler
import com.fobbu.member.android.fragments.rsaFragmentModule.presenter.RsaFragmnetPresenter
import com.fobbu.member.android.fragments.rsaFragmentModule.view.RsaFragmentView
import com.fobbu.member.android.interfaces.HeaderIconChanges
import com.fobbu.member.android.interfaces.TopBarChanges
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.utils.RecyclerItemClickListener
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
import kotlinx.android.synthetic.main.fragment_rsa.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.lang.Double
import java.util.*
import kotlin.collections.ArrayList

class RSAFragment : Fragment(), GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks, LocationListener, RsaFragmentView {

    private lateinit var mMapView: MapView
    private lateinit var googleMap: GoogleMap
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
    private var serviceSelectedID = ""
    private var serviceSelectedAmount = ""
    private var strLatitude = ""
    private var strLongitude = ""
    private var strFuelType = ""
    private var strTowingType = ""
    private var ifTowRequired = false

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

    private lateinit var llTextAll: LinearLayout
    private lateinit var llTowing: LinearLayout
    private lateinit var llLifting: LinearLayout
    private lateinit var llFlatbed: LinearLayout

    private lateinit var tvFlatBedText: TextView
    private lateinit var tvFlatBedPrice: TextView
    private lateinit var tvLiftingText: TextView
    private lateinit var tvLiftingPrice: TextView
    private lateinit var ivLifting: ImageView
    private lateinit var ivFlatBed: ImageView

    private lateinit var viewLeftTop: View
    private lateinit var viewLeftBottom: View
    private lateinit var viewRightTop: View
    private lateinit var viewRightBottom: View

    private var strBurstTopLeft = ""
    private var strBurstTopRight = ""
    private var strBurstBottomLeft = ""
    private var strBurstBottomRight = ""
    private var listSelectedBurstTyre = ArrayList<String>()

    private lateinit var tvYes: TextView
    private lateinit var tvNo: TextView
    private var strHaveSpareTyre = "yes"
    private lateinit var llMarkTyreBurst: LinearLayout

    private lateinit var tvContinue: TextView
    private lateinit var llTowRequired: LinearLayout
    private lateinit var rlTopDrawer: RelativeLayout
    private var topBarChanges: TopBarChanges? = null

    private lateinit var rsaFragmentHandler: RsaFragmentHandler

    lateinit var rlTwoWheelerBurst: RelativeLayout
    lateinit var rl4WheelerBurst: RelativeLayout
    lateinit var viewLeftTwoWheeler: View
    lateinit var viewRightTwoWheeler: View


    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rsa, container, false)

        if (view != null) {

            mapInitialise(view, savedInstanceState)

            initialise(view)

            setUpGoogleClient()

            handleClick()

            rsaFragmentHandler = RsaFragmnetPresenter(activity!!, this)

            serviceListApi()
        }
        return view
    }

    // handling click functionality of the  activity in this method
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun handleClick() {

        chooseImagesClick()

        tvFindFobbu.setOnClickListener {
            findFobbuApi()
            //("Puncture cost is Rs $serviceSelectedAmount\n(extra puncture will cost Rs $serviceSelectedAmount per puncture)")
        }

        ivBack.setOnClickListener {

            when {
                llSubPoints.visibility == View.VISIBLE -> {

                    ifTopBarChnagesNull(true)
                    llHomeServices.visibility = View.VISIBLE
                    rlTopDrawer.visibility = View.GONE
                    llSubPoints.visibility = View.GONE
                    llUploadPics.visibility = View.GONE
                    llFindFobbu.visibility = View.GONE
                    llMarkTyreBurst.visibility = View.GONE
                    etVehicleNumber.setText("")
                }

                llUploadPics.visibility == View.VISIBLE -> {

                    when {
                        serviceSelected == RsaConstants.ServiceName.burstTyre -> {
                            llHomeServices.visibility = View.GONE
                            llSubPoints.visibility = View.GONE
                            llUploadPics.visibility = View.GONE
                            llFindFobbu.visibility = View.GONE
                            llMarkTyreBurst.visibility = View.VISIBLE
                            llTowRequired.visibility = View.GONE

                            if(strVehicleType == "2wheeler")
                            {
                                rl4WheelerBurst.visibility=View.GONE
                                rlTwoWheelerBurst.visibility=View.VISIBLE
                            }
                            else
                            {
                                rl4WheelerBurst.visibility=View.VISIBLE
                                rlTwoWheelerBurst.visibility=View.GONE
                            }
                        }
                        ifTowRequired -> {
                            llHomeServices.visibility = View.GONE
                            llSubPoints.visibility = View.GONE
                            llUploadPics.visibility = View.GONE
                            llFindFobbu.visibility = View.GONE
                            llMarkTyreBurst.visibility = View.GONE
                            llTowRequired.visibility = View.VISIBLE
                        }
                        else -> {
                            llHomeServices.visibility = View.GONE
                            llSubPoints.visibility = View.VISIBLE
                            llUploadPics.visibility = View.GONE
                            llFindFobbu.visibility = View.GONE
                            llMarkTyreBurst.visibility = View.GONE
                            llTowRequired.visibility = View.GONE
                        }
                    }
                }

                llFindFobbu.visibility == View.VISIBLE -> {
                    llHomeServices.visibility = View.GONE
                    llSubPoints.visibility = View.GONE
                    llUploadPics.visibility = View.VISIBLE
                    llFindFobbu.visibility = View.GONE
                }

                llMarkTyreBurst.visibility == View.VISIBLE -> {
                    llHomeServices.visibility = View.GONE
                    llSubPoints.visibility = View.VISIBLE
                    llUploadPics.visibility = View.GONE
                    llFindFobbu.visibility = View.GONE
                    llMarkTyreBurst.visibility = View.GONE
                }

                llTowRequired.visibility == View.VISIBLE -> {
                    llHomeServices.visibility = View.GONE
                    llSubPoints.visibility = View.GONE
                    llUploadPics.visibility = View.GONE
                    llFindFobbu.visibility = View.GONE
                    llMarkTyreBurst.visibility = View.VISIBLE
                    llTowRequired.visibility = View.GONE
                    ifTowRequired = false
                    if(strVehicleType == "2wheeler")
                    {
                        rl4WheelerBurst.visibility=View.GONE
                        rlTwoWheelerBurst.visibility=View.VISIBLE
                    }
                    else
                    {
                        rl4WheelerBurst.visibility=View.VISIBLE
                        rlTwoWheelerBurst.visibility=View.GONE
                    }
                }
            }
        }

        llScooterTwo.setOnClickListener {

            strVehicleType = "2wheeler"

            if (etVehicleNumber.text.isNullOrBlank()) {
                Toast.makeText(activity, resources.getString(R.string.please_enter_vehicle_number), Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (serviceSelected == RsaConstants.ServiceName.burstTyre) {
                    llHomeServices.visibility = View.GONE
                    llSubPoints.visibility = View.GONE
                    llUploadPics.visibility = View.GONE
                    llFindFobbu.visibility = View.GONE
                    llMarkTyreBurst.visibility = View.VISIBLE
                    if(strVehicleType == "2wheeler")
                    {
                        rl4WheelerBurst.visibility=View.GONE
                        rlTwoWheelerBurst.visibility=View.VISIBLE
                    }
                    else
                    {
                        rl4WheelerBurst.visibility=View.VISIBLE
                        rlTwoWheelerBurst.visibility=View.GONE
                    }
                } else {
                    llHomeServices.visibility = View.GONE
                    llSubPoints.visibility = View.GONE
                    llUploadPics.visibility = View.VISIBLE
                    llFindFobbu.visibility = View.GONE
                    llMarkTyreBurst.visibility = View.GONE
                }


            }
        }

        llScooterThree.setOnClickListener {

            if (etVehicleNumber.text.isNullOrBlank()) {
                Toast.makeText(activity, resources.getString(R.string.please_enter_vehicle_number), Toast.LENGTH_SHORT)
                    .show()
            } else {
                strVehicleType = "2wheeler"
                strFuelType = "petrol"
                llHomeServices.visibility = View.GONE
                llSubPoints.visibility = View.GONE
                llUploadPics.visibility = View.VISIBLE
                llFindFobbu.visibility = View.GONE
                llMarkTyreBurst.visibility = View.GONE
            }
        }

        llCarFuelDieselThree.setOnClickListener {

            if (etVehicleNumber.text.isNullOrBlank()) {
                Toast.makeText(activity, resources.getString(R.string.please_enter_vehicle_number), Toast.LENGTH_SHORT)
                    .show()
            } else {
                strVehicleType = "2wheeler"
                strFuelType = "diesel"
                llHomeServices.visibility = View.GONE
                llSubPoints.visibility = View.GONE
                llUploadPics.visibility = View.VISIBLE
                llMarkTyreBurst.visibility = View.GONE
                llFindFobbu.visibility = View.GONE
            }
        }

        llCarFuelPetrolThree.setOnClickListener {

            if (etVehicleNumber.text.isNullOrBlank()) {
                Toast.makeText(activity, resources.getString(R.string.please_enter_vehicle_number), Toast.LENGTH_SHORT)
                    .show()
            } else {
                strVehicleType = "2wheeler"
                strFuelType = "petrol"
                llHomeServices.visibility = View.GONE
                llSubPoints.visibility = View.GONE
                llUploadPics.visibility = View.VISIBLE
                llMarkTyreBurst.visibility = View.GONE
                llFindFobbu.visibility = View.GONE
            }
        }

        tvContinue.setOnClickListener {

            llHomeServices.visibility = View.GONE
            llSubPoints.visibility = View.GONE
            llUploadPics.visibility = View.VISIBLE
            llFindFobbu.visibility = View.GONE
            llMarkTyreBurst.visibility = View.GONE
            llTowRequired.visibility = View.GONE

            serviceSelected = RsaConstants.ServiceName.towing


            for (i in dataListServices.indices) {
                if (RsaConstants.ServiceName.towing == dataListServices[i]["static_name"].toString()) {

                    serviceSelectedID = dataListServices[i]["_id"].toString()

                    println("NAME " + dataListServices[i]["service_name"].toString())

                    break
                }
            }

            ifTowRequired = true
        }

        llCarTwo.setOnClickListener {

            strVehicleType = "4wheeler"

            if (etVehicleNumber.text.isNullOrBlank()) {
                Toast.makeText(activity, resources.getString(R.string.please_enter_vehicle_number), Toast.LENGTH_SHORT)
                    .show()
            } else {

                if (serviceSelected == RsaConstants.ServiceName.burstTyre) {
                    llHomeServices.visibility = View.GONE
                    llSubPoints.visibility = View.GONE
                    llUploadPics.visibility = View.GONE
                    llFindFobbu.visibility = View.GONE
                    llMarkTyreBurst.visibility = View.VISIBLE
                    if(strVehicleType == "2wheeler")
                    {
                        rl4WheelerBurst.visibility=View.GONE
                        rlTwoWheelerBurst.visibility=View.VISIBLE
                    }
                    else
                    {
                        rl4WheelerBurst.visibility=View.VISIBLE
                        rlTwoWheelerBurst.visibility=View.GONE
                    }
                } else {
                    llHomeServices.visibility = View.GONE
                    llSubPoints.visibility = View.GONE
                    llUploadPics.visibility = View.VISIBLE
                    llFindFobbu.visibility = View.GONE
                    llMarkTyreBurst.visibility = View.GONE
                }
            }
        }

        tvUploadPics.setOnClickListener {
            showMainFindFobbuView()
        }

        tvSkip.setOnClickListener {
            showMainFindFobbuView()
        }

        llFlatbed.setOnClickListener {
            strTowingType = "Flatbed"
            tvFlatBedPrice.setTextColor(resources.getColor(R.color.colorPrimary))
            tvFlatBedText.setTextColor(resources.getColor(R.color.colorPrimary))
            tvLiftingText.setTextColor(resources.getColor(R.color.drawer_text_color))
            tvLiftingPrice.setTextColor(resources.getColor(R.color.drawer_text_color))
            ivFlatBed.setImageResource(R.drawable.flatbed_selected_blue)
            ivLifting.setImageResource(R.drawable.lifting)
        }

        llLifting.setOnClickListener {
            strTowingType = "Lifting"
            tvFlatBedPrice.setTextColor(resources.getColor(R.color.drawer_text_color))
            tvFlatBedText.setTextColor(resources.getColor(R.color.drawer_text_color))
            tvLiftingText.setTextColor(resources.getColor(R.color.colorPrimary))
            tvLiftingPrice.setTextColor(resources.getColor(R.color.colorPrimary))
            ivFlatBed.setImageResource(R.drawable.flatbed_selected)
            ivLifting.setImageResource(R.drawable.lifting_selected_blue)
        }

        viewLeftTwoWheeler.setOnClickListener {

            if(!listSelectedBurstTyre.contains("LeftTwoWheeler"))
            {
                viewLeftTwoWheeler.setBackgroundResource(R.drawable.red_circle)
                listSelectedBurstTyre.add("LeftTwoWheeler")
            }
            else
            {
                viewLeftTwoWheeler.setBackgroundResource(R.drawable.border_circle)
                listSelectedBurstTyre.remove("LeftTwoWheeler")
            }
        }

        viewRightTwoWheeler.setOnClickListener {

            if(!listSelectedBurstTyre.contains("RightTwoWheeler"))
            {
                viewRightTwoWheeler.setBackgroundResource(R.drawable.red_circle)
                listSelectedBurstTyre.add("RightTwoWheeler")
            }
            else
            {
                viewRightTwoWheeler.setBackgroundResource(R.drawable.border_circle)
                listSelectedBurstTyre.remove("RightTwoWheeler")
            }
        }

        viewLeftTop.setOnClickListener {

            if (strBurstTopLeft == "") {
                strBurstTopLeft = "1"
                viewLeftTop.setBackgroundResource(R.drawable.red_circle)
                listSelectedBurstTyre.add("LeftTop")
            } else {
                strBurstTopLeft = ""
                viewLeftTop.setBackgroundResource(R.drawable.border_circle)
                listSelectedBurstTyre.remove("LeftTop")
            }
        }

        viewLeftBottom.setOnClickListener {

            if (strBurstBottomLeft == "") {
                strBurstBottomLeft = "1"
                viewLeftBottom.setBackgroundResource(R.drawable.red_circle)
                listSelectedBurstTyre.add("LeftBottom")
            } else {
                strBurstBottomLeft = ""
                viewLeftBottom.setBackgroundResource(R.drawable.border_circle)
                listSelectedBurstTyre.remove("LeftBottom")
            }
        }

        viewRightTop.setOnClickListener {

            if (strBurstTopRight == "") {
                strBurstTopRight = "1"
                viewRightTop.setBackgroundResource(R.drawable.red_circle)
                listSelectedBurstTyre.add("RightTop")
            } else {
                strBurstTopRight = ""
                viewRightTop.setBackgroundResource(R.drawable.border_circle)
                listSelectedBurstTyre.remove("RightTop")
            }
        }

        viewRightBottom.setOnClickListener {

            if (strBurstBottomRight == "") {
                strBurstBottomRight = "1"
                viewRightBottom.setBackgroundResource(R.drawable.red_circle)
                listSelectedBurstTyre.add("RightBottom")
            } else {
                strBurstBottomRight = ""
                viewRightBottom.setBackgroundResource(R.drawable.border_circle)
                listSelectedBurstTyre.remove("RightBottom")
            }
        }

        tvYes.setOnClickListener {

            println("SELECTED BURST TYRE " + listSelectedBurstTyre)

            println("""SELECTED BURST TYRE SIZE ${listSelectedBurstTyre.size}""")

            when {
                listSelectedBurstTyre.size == 0 -> Toast.makeText(
                    activity!!,
                    "Please select atleast one Burst tyre.",
                    Toast.LENGTH_SHORT
                ).show()
                listSelectedBurstTyre.size > 1 -> {
                    strHaveSpareTyre = "no"
                    tvYes.setBackgroundResource(R.drawable.rounded_white_background)
                    tvNo.setBackgroundResource(R.drawable.rounded_blue_background)
                    tvYes.setTextColor(resources.getColor(R.color.drawer_text_color))
                    tvNo.setTextColor(resources.getColor(R.color.white))

                    llHomeServices.visibility = View.GONE
                    llSubPoints.visibility = View.GONE
                    llUploadPics.visibility = View.GONE
                    llFindFobbu.visibility = View.GONE
                    llMarkTyreBurst.visibility = View.GONE
                    llTowRequired.visibility = View.VISIBLE
                }
                else -> {
                    strHaveSpareTyre = "yes"
                    tvYes.setBackgroundResource(R.drawable.rounded_blue_background)
                    tvYes.setTextColor(resources.getColor(R.color.white))
                    tvNo.setTextColor(resources.getColor(R.color.drawer_text_color))
                    tvNo.setBackgroundResource(R.drawable.rounded_white_background)

                    llHomeServices.visibility = View.GONE
                    llSubPoints.visibility = View.GONE
                    llUploadPics.visibility = View.VISIBLE
                    llFindFobbu.visibility = View.GONE
                    llMarkTyreBurst.visibility = View.GONE
                    llTowRequired.visibility = View.GONE
                }
            }
        }

        tvNo.setOnClickListener {

            strHaveSpareTyre = "no"
            tvYes.setBackgroundResource(R.drawable.rounded_white_background)
            tvNo.setBackgroundResource(R.drawable.rounded_blue_background)
            tvYes.setTextColor(resources.getColor(R.color.drawer_text_color))
            tvNo.setTextColor(resources.getColor(R.color.white))

            llHomeServices.visibility = View.GONE
            llSubPoints.visibility = View.GONE
            llUploadPics.visibility = View.GONE
            llFindFobbu.visibility = View.GONE
            llMarkTyreBurst.visibility = View.GONE
            llTowRequired.visibility = View.VISIBLE
        }

    }

    ///FIND FOBBU VIEW ACCORDING CONDITIONS
    private fun showMainFindFobbuView() {
        llHomeServices.visibility = View.GONE
        llSubPoints.visibility = View.GONE
        llUploadPics.visibility = View.GONE
        llFindFobbu.visibility = View.VISIBLE

        when (serviceSelected) {
            RsaConstants.ServiceName.flatTyre -> {
                text1.text = resources.getString(R.string.tyre_1)
                text2.text = resources.getString(R.string.tyre_2)
                text3.text = resources.getString(R.string.tyre_3)
                text4.text = resources.getString(R.string.tyre_4)
                llTowing.visibility = View.GONE
                llTextAll.visibility = View.VISIBLE
            }
            RsaConstants.ServiceName.jumpStart -> {
                text1.text = resources.getString(R.string.tyre_1_battery)
                text2.text = resources.getString(R.string.tyre_2)
                text3.text = resources.getString(R.string.tyre_3)
                text4.text = resources.getString(R.string.tyre_4)
                llTowing.visibility = View.GONE
                llTextAll.visibility = View.VISIBLE
            }
            RsaConstants.ServiceName.fuelDelivery -> {

                if (strFuelType == "petrol") {
                    text1.text = resources.getString(R.string.tyre_1_fuel_petrol)
                    text2.text = resources.getString(R.string.tyre_2_fuel_petrol)
                } else {
                    text1.text = resources.getString(R.string.tyre_1_fuel_diesel)
                    text2.text = resources.getString(R.string.tyre_2_fuel_diesel)
                }

                text3.text = resources.getString(R.string.tyre_3_fuel_diesel)
                text4.text = resources.getString(R.string.tyre_4_fuel_diesel)
                llTowing.visibility = View.GONE
                llTextAll.visibility = View.VISIBLE
            }
            RsaConstants.ServiceName.burstTyre -> {
                text1.text = resources.getString(R.string.tyre_1_burst)
                text2.text = resources.getString(R.string.tyre_2)
                text3.text = resources.getString(R.string.tyre_3)
                text4.text = resources.getString(R.string.tyre_4)
                llTowing.visibility = View.GONE
                llTextAll.visibility = View.VISIBLE
            }
            RsaConstants.ServiceName.towing -> {
                text1.text = resources.getString(R.string.tyre_1)
                text2.text = resources.getString(R.string.tyre_2)
                text3.text = resources.getString(R.string.tyre_3)
                text4.text = resources.getString(R.string.tyre_4)
                llTowing.visibility = View.VISIBLE
                llTextAll.visibility = View.GONE
            }
            else -> {
                text1.text = resources.getString(R.string.tyre_1)
                text2.text = resources.getString(R.string.tyre_2)
                text3.text = resources.getString(R.string.tyre_3)
                text4.text = resources.getString(R.string.tyre_4)
                llTowing.visibility = View.GONE
                llTextAll.visibility = View.VISIBLE
            }
        }
    }


    // method for capturing images
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ClickableViewAccessibility", "NewApi")
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

    // initializing all the variables of the class in this method
    private fun initialise(view: View?) {

        webServiceApi = getEnv().getRetrofitMulti()

        headerIconChanges = activity as HeaderIconChanges?
        headerIconChanges!!.changeHeaderIcons(true, false, false)

        rlLoader = view!!.findViewById(R.id.rlLoader)
        rlTopDrawer = view.findViewById(R.id.rlTopDrawer)

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
        recyclerViewServices.layoutManager = GridLayoutManager(activity, 3) as RecyclerView.LayoutManager?
        fobbuServiceAdapter = FobbuServiceAdapter(activity!!, dataListServices)
        recyclerViewServices.adapter = fobbuServiceAdapter
        recyclerViewClicks()
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
        tvHeadingFind = view.findViewById(R.id.tvHeadingFind)
        text1 = view.findViewById(R.id.text1)
        text2 = view.findViewById(R.id.text2)
        text3 = view.findViewById(R.id.text3)
        text4 = view.findViewById(R.id.text4)
        llTerms = view.findViewById(R.id.llTerms)

        llTowing = view.findViewById(R.id.llTowing)
        llTextAll = view.findViewById(R.id.llTextAll)

        llLifting = view.findViewById(R.id.llLifting)
        llFlatbed = view.findViewById(R.id.llFlatbed)

        tvFlatBedPrice = view.findViewById(R.id.tvFlatBedPrice)
        tvFlatBedText = view.findViewById(R.id.tvFlatBedText)
        tvLiftingPrice = view.findViewById(R.id.tvLiftingPrice)
        tvLiftingText = view.findViewById(R.id.tvLiftingText)

        ivLifting = view.findViewById(R.id.ivLifting)
        ivFlatBed = view.findViewById(R.id.ivFlatBed)

        viewLeftTop = view.findViewById(R.id.viewLeftTop)
        viewLeftBottom = view.findViewById(R.id.viewLeftBottom)
        viewRightBottom = view.findViewById(R.id.viewRightBottom)
        viewRightTop = view.findViewById(R.id.viewRightTop)

        tvNo = view.findViewById(R.id.tvNo)
        tvYes = view.findViewById(R.id.tvYes)

        llMarkTyreBurst = view.findViewById(R.id.llMarkTyreBurst)

        tvContinue = view.findViewById(R.id.tvContinue)
        llTowRequired = view.findViewById(R.id.llTowRequired)

        rlTwoWheelerBurst = view.findViewById(R.id.rlTwoWheelerBurst)
        rl4WheelerBurst = view.findViewById(R.id.rl4WheelerBurst)
        viewLeftTwoWheeler = view.findViewById(R.id.viewLeftTwoWheeler)
        viewRightTwoWheeler = view.findViewById(R.id.viewRightTwoWheeler)
    }


    ///RECYCLER VIEW CLICKS HANDLED
    private fun recyclerViewClicks() {

        recyclerViewServices.addOnItemTouchListener(
            RecyclerItemClickListener(activity!!, object : RecyclerItemClickListener.OnItemClickListener {
                @SuppressLint("SetTextI18n")
                override fun onItemClick(view: View, position: Int) {

                    serviceSelected = dataListServices[position]["static_name"].toString()
                    serviceSelectedID = dataListServices[position]["_id"].toString()
                    serviceSelectedAmount = dataListServices[position]["service_price"].toString()

                    if (serviceSelectedAmount.contains("\\.".toRegex())) {
                        tvHeadingFind.text = "Rs ${serviceSelectedAmount.split("\\.".toRegex())[0]}/-"
                    } else
                        tvHeadingFind.text = "Rs $serviceSelectedAmount/-"



                    when (serviceSelected) {
                        RsaConstants.ServiceName.flatTyre -> {
                            llHomeServices.visibility = View.GONE
                            llSubPoints.visibility = View.VISIBLE
                            llThree.visibility = View.GONE
                            llTwo.visibility = View.VISIBLE
                            setAnimationRight(linearLayoutCarRightTwo, activity!!)
                            setAnimationLeft(linearLayoutScooterLeftTwo, activity!!)

                            ifTopBarChnagesNull(false)

                            Handler().postDelayed(
                                {
                                    rlTopDrawer.visibility = View.VISIBLE
                                }, 200
                            )

                            tvHeading.text = resources.getString(R.string.flat_tyre_worries)
                            tvSubheading.text = resources.getString(R.string.fix_on_the_spot)
                        }
                        RsaConstants.ServiceName.jumpStart -> {
                            llHomeServices.visibility = View.GONE
                            llSubPoints.visibility = View.VISIBLE
                            llThree.visibility = View.GONE
                            llTwo.visibility = View.VISIBLE
                            setAnimationRight(linearLayoutCarRightTwo, activity!!)
                            setAnimationLeft(linearLayoutScooterLeftTwo, activity!!)

                            ifTopBarChnagesNull(false)
                            rlTopDrawer.visibility = View.VISIBLE
                            tvHeading.text = resources.getString(R.string.dead_battery_worries)
                            tvSubheading.text = resources.getString(R.string.jump_start)
                        }
                        RsaConstants.ServiceName.fuelDelivery -> {
                            llHomeServices.visibility = View.GONE
                            llSubPoints.visibility = View.VISIBLE
                            llThree.visibility = View.VISIBLE
                            llTwo.visibility = View.GONE
                            setAnimationRight(llCarFuelPetrolThree, activity!!)
                            setAnimationLeft(llScooterThree, activity!!)
                            setAnimationFade(llCarFuelDieselThree, activity!!)

                            ifTopBarChnagesNull(false)
                            rlTopDrawer.visibility = View.VISIBLE
                            tvHeading.text = resources.getString(R.string.empty_tanks_worries)
                            tvSubheading.text = resources.getString(R.string.deliver_real_quick)
                        }
                        RsaConstants.ServiceName.burstTyre -> {
                            llHomeServices.visibility = View.GONE
                            llSubPoints.visibility = View.VISIBLE
                            llThree.visibility = View.GONE
                            llTwo.visibility = View.VISIBLE
                            setAnimationRight(linearLayoutCarRightTwo, activity!!)
                            setAnimationLeft(linearLayoutScooterLeftTwo, activity!!)

                            ifTopBarChnagesNull(false)
                            rlTopDrawer.visibility = View.VISIBLE
                            tvHeading.text = resources.getString(R.string.burst_tyre_worries)
                            tvSubheading.text = resources.getString(R.string.help_you_fix)
                        }
                        RsaConstants.ServiceName.towing -> {
                            llHomeServices.visibility = View.GONE
                            llSubPoints.visibility = View.VISIBLE
                            llThree.visibility = View.GONE
                            llTwo.visibility = View.VISIBLE
                            setAnimationRight(linearLayoutCarRightTwo, activity!!)
                            setAnimationLeft(linearLayoutScooterLeftTwo, activity!!)

                            ifTopBarChnagesNull(false)
                            rlTopDrawer.visibility = View.VISIBLE
                            tvHeading.text = resources.getString(R.string.double_trouble)
                            tvSubheading.text = resources.getString(R.string.we_will_connect_towing)
                        }
                        RsaConstants.ServiceName.iDunno -> {

                        }
                        else -> {
                            llHomeServices.visibility = View.VISIBLE
                            llSubPoints.visibility = View.GONE
                            rlTopDrawer.visibility = View.GONE
                        }
                    }
                }
            })
        )
    }


    /////////////////////////////FOR API"S//////////////////////////////////////////////////

    // fetch service Api (API-partners/services)
    private fun serviceListApi() {
        if (CommonClass(activity!!, activity!!).checkInternetConn(activity!!)) {

            val tokenHeader = CommonClass(activity!!, activity!!).getString("x_access_token")
            rsaFragmentHandler.fetchService(tokenHeader)
        }
    }

    // handling fetch service api response (API-partners/services)
    override fun fetchingServiceReport(mainPojo: MainPojo)
    {
        //val mainPojo = response!!.body()
        val serviceList = mainPojo.services

        dataListServices.clear()

        for (i in serviceList.indices) {
            if (serviceList[i]["service_type"].toString() == "RSA")
                dataListServices.add(serviceList[i])
        }

        println("service list $dataListServices")

        for (i in dataListServices.indices)
        {
            if (i == 0)
            {
                dataListServices[i]["select"] = "1"

                serviceSelectedID = dataListServices[i]["_id"].toString()

                serviceSelectedAmount = dataListServices[i]["service_price"].toString()
            }
            else
                dataListServices[i]["select"] = "0"
        }

        fobbuServiceAdapter.notifyDataSetChanged()
    }


    //////////////////FIND FOBBU REQUEST API  /////////////////////////

    // find fobbu Api (API-users/requests)
    private fun findFobbuApi() {
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

        val serviceSelected = RequestBody.create(MediaType.parse("text/plain"), serviceSelectedID)

        val strLatitude = RequestBody.create(MediaType.parse("text/plain"), strLatitude)

        val strLongitude = RequestBody.create(MediaType.parse("text/plain"), strLongitude)

        val strVehicleType = RequestBody.create(MediaType.parse("text/plain"), strVehicleType)

        val strVehicleNumber = RequestBody.create(
            MediaType.parse("text/plain"),
            etVehicleNumber.text.toString()
        )


        rsaFragmentHandler.findFobbuRequest(
            userId, serviceSelected, strLatitude, strLongitude
            , strVehicleType, strVehicleNumber, fileList, tokenHeader
        )
    }

    // handle  find  fobbu Api response (API-users/requests)
    override fun findingFobbuReport(mainPojo: MainPojo) {
        try {
            // val mainPojo = response!!.body()

            activity!!.startService(Intent(activity!!, FetchStatusAPI::class.java))

            if (mainPojo.success == "true") {

                /*  if (serviceSelected == RsaConstants.ServiceName.towing) {
                      Toast.makeText(activity!!, mainPojo.message, Toast.LENGTH_LONG).show()

                      activity!!.startActivity(
                          Intent(activity!!, DashboardActivity::class.java)
                              .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                      )

                      CommonClass(activity!!, activity!!).workDoneReviewSend()

                      activity!!.finish()
                  } else {*/
                    //fleetRequestApi(mainPojo.getData()._id)

                    CommonClass(activity!!, activity!!).putString(RsaConstants.RsaTypes.checkIfOnGoingRsaRequest, "YES")

                    CommonClass(activity!!, activity!!).putString(
                        RsaConstants.ServiceSaved.fobbuRequestId,
                        mainPojo.getData()._id
                    )

                    CommonClass(activity!!, activity!!).putString(
                        RsaConstants.ServiceSaved.serviceNameSelected,
                        serviceSelected
                    )

                    activity!!.startActivity(
                        Intent(activity!!, WaitingScreenBlue::class.java)
                            .putExtra("navigate_to", "0")
                    )
               // }
            } else {
                CommonClass(activity!!, activity!!).showToast(mainPojo.message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //////////////////FLEET REQUEST API  /////////////////////////

    // Fleet request Api (API-users/request/{requestId})
    private fun fleetRequestApi(id: String) {

        if (CommonClass(activity!!, activity!!).checkInternetConn(activity!!)) {

            val tokenHeader = CommonClass(activity!!, activity!!).getString("x_access_token")

            rsaFragmentHandler.findFleetOrUser(tokenHeader, id)
        }
    }

    // Handler Fleet request Api response (Api-users/request/{requestId})
    override fun fleetSuccessReport(mainPojo: MainPojo) {
        if (mainPojo!!.success == "true") {

        } else {
        }
    }


    ////////////////////////FOR MAP/////////////////////////////////////////////////

    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest = LocationRequest.create()!!
    var location = false


    //  initializing map in this method
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
        checkGPSEnable()
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

            val permission =
                ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION)

            if (permission == PackageManager.PERMISSION_GRANTED) {
                googleMap.isMyLocationEnabled = true
            }


            googleMap.setInfoWindowAdapter(InfoWindow())

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

    // setting up google client for map in this method
    private fun setUpGoogleClient() {
        googleApiClient = GoogleApiClient.Builder(context!!)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).build()
        googleApiClient!!.connect()
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
                this@RSAFragment
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

                    LocationSettingsStatusCodes.CANCELED -> {
                        checkGPSEnable()
                    }
                }
            }
        }
    }


    // Method for setting up  marker on Map
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

    fun ifTopBarChnagesNull(boolean: Boolean) {
        if (topBarChanges == null)
            topBarChanges = activity as TopBarChanges

        topBarChanges!!.showGoneTopBar(boolean)
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()


        if (llSubPoints.visibility == View.VISIBLE ||
            llUploadPics.visibility == View.VISIBLE ||
            llFindFobbu.visibility == View.VISIBLE ||
            llMarkTyreBurst.visibility == View.VISIBLE ||
            llTowRequired.visibility == View.VISIBLE
        )
            ifTopBarChnagesNull(false)
        else
            ifTopBarChnagesNull(true)

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


    // method for  deleting images
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

    // method for uploading images either by capturing from camera or from gallery
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
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
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Camera") { _, _ ->
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
        requestPermissions(
            arrayOf(
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.CAMERA"
            ),
            1
        )
    }

    private fun makeRequest2() {
        requestPermissions(
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

    // Method for  capturing images via Camera
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

    // Method for creating new files and deleting old files
    private fun createFileAndDeleteOldFile() {
        mFileTemp =
                File(Environment.getExternalStorageDirectory(), "vehicle_images" + System.currentTimeMillis() + ".jpg")
        if (mFileTemp!!.exists()) {
            mFileTemp!!.delete()
        }
    }

    // Method for opening gallery
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
                                "Phone Settings -> Apps --> Fobbu Member --> Permissions\n" +
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

                    checkGPSEnable()
                }
            }
        }
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

                    dataList.add(imgFile)
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

                    dataList.add(imgFile)
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


    // Method for opening dialog containing message
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

    // Method for compressing image
    private fun compressImage(imgFile: File): File {
        //////old code
        val bos = ByteArrayOutputStream()

        // var myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
        val myBitmap = imageRotation(BitmapFactory.decodeFile(imgFile.absolutePath), imgFile.absolutePath)

        val nh = (myBitmap.height * (512.0 / myBitmap.width)).toInt()

        val scaled = Bitmap.createScaledBitmap(myBitmap, 512, nh, true)

        scaled.compress(Bitmap.CompressFormat.JPEG, 80, bos)

        val bitmapdata = bos.toByteArray()

        val f = imgFile
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        return f
    }

    // Method for rotating images
    private fun imageRotation(bitmap: Bitmap, path: String): Bitmap {
        val ei = ExifInterface(path)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        var rotatedBitmap: Bitmap = bitmap

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

    // Method for setting Slide Right Animation
    fun setAnimationRight(layout: LinearLayout, activity: Activity) {

        val animation: Animation = AnimationUtils.loadAnimation(activity, R.anim.slide_left)
        layout.startAnimation(animation)
    }

    // Method for setting Slide Left Animation
    fun setAnimationLeft(layout: LinearLayout, activity: Activity) {
        val animation: Animation = AnimationUtils.loadAnimation(activity, R.anim.slide_right)
        layout.startAnimation(animation)
    }

    // Method for setting Slide Fade Animation
    fun setAnimationFade(layout: LinearLayout, activity: Activity) {
        val animation: Animation = AnimationUtils.loadAnimation(activity, R.anim.fade_rsa)
        layout.startAnimation(animation)
    }

    override fun showLoader() {
        rlLoader.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        rlLoader.visibility = View.GONE
    }
}