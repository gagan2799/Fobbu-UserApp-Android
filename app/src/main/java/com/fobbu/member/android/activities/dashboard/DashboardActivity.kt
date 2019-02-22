package com.fobbu.member.android.activities.dashboardActivity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboard.presenter.DashboardHandler
import com.fobbu.member.android.activities.dashboard.presenter.DashboardPresenter
import com.fobbu.member.android.activities.emergencyContactsModule.emergencyContacts.EmergencyContactsActivity
import com.fobbu.member.android.activities.freebies.FreebiesActivity
import com.fobbu.member.android.activities.help.HelpActivity
import com.fobbu.member.android.activities.orderAndPassbookModule.MyOrder.MyOrdersActivity
import com.fobbu.member.android.activities.orderAndPassbookModule.passbook.MyPassbookActivity
import com.fobbu.member.android.activities.paymentModule.GetSetGoActivity
import com.fobbu.member.android.activities.paymentModule.WorkSummaryActivity
import com.fobbu.member.android.activities.paymentSettings.PaymentSettingActivity
import com.fobbu.member.android.activities.profile.ProfileActivity
import com.fobbu.member.android.activities.rsaModule.RSARequestCancelActivity
import com.fobbu.member.android.activities.securitySetting.SecuritySettingActivity
import com.fobbu.member.android.activities.vehicleModule.AddEditVehicleActivity
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenBlue
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenWhite
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.fcm.FcmPushTypes
import com.fobbu.member.android.fragments.HomeFragment

import com.fobbu.member.android.fragments.RSALiveFragment
import com.fobbu.member.android.fragments.odsModule.OdsTrackingFragment.OdsTrackingFragment
import com.fobbu.member.android.fragments.odsModule.odsFragment.OdsFragment
import com.fobbu.member.android.fragments.rsaFragmentModule.RSAFragment
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.fragments.rsaFragmentModule.presenter.FetchStatusPresenter
import com.fobbu.member.android.interfaces.ChangeRSAFragments
import com.fobbu.member.android.interfaces.HeaderIconChanges
import com.fobbu.member.android.interfaces.TopBarChanges
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import com.fobbu.member.android.view.ActivityViewDashboard
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.inflate_drawer.*
import kotlinx.android.synthetic.main.option_menu_layout.*
import java.lang.Exception

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DashboardActivity : AppCompatActivity(), HeaderIconChanges, ChangeRSAFragments, TopBarChanges, ActivityView,
    ActivityViewDashboard
{
    private var topBarChanges: TopBarChanges? = null

    private var fragmentTypeForRSA = ""

    private lateinit var fragmentEarlier: Fragment

    private var fragmentEarlierBool = false

    private lateinit var dashboardHandler: DashboardHandler

    var odsServiceStaticName=""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        initView()
    }

    // function for initialising all the variables and view of the class
    private fun initView()
    {
        dashboardHandler = DashboardPresenter(this, this)

        drawerClicks()

        tabBarClicks()

      /*  if (intent.getStringExtra(RsaConstants.Ods.static_name)!=RsaConstants.OdsServiceStaticName.trip_ready)
            changeFragment(OdsTrackingFragment(),resources.getString(R.string.ods))

        else*/
            checkAndNavigateFromPush()
    }

    override fun showGoneTopBar(showDrawer: Boolean) {
        if (showDrawer)
            rlTopDrawer.visibility = View.VISIBLE
        else
            rlTopDrawer.visibility = View.GONE
    }

    ////BOTTOM BAR CLICKS HANDLED IN THIS METHOD
    private fun tabBarClicks() {

        imageViewOptionMenuDash.setOnClickListener {
            showOptionLayout()
            // linearLayoutOptionMenu.visibility=View.VISIBLE
        }

        llHome.setOnClickListener {
            changeFragment(HomeFragment(), resources.getString(R.string.home))
        }

        llRSA.setOnClickListener {
            setFragmentsFromStackForRSA(fragmentTypeForRSA)
        }

        llOdsDash.setOnClickListener {
            if (intent.hasExtra(RsaConstants.Ods.static_name)/*!=RsaConstants.OdsServiceStaticName.trip_ready*/)
                changeFragment(OdsTrackingFragment(),resources.getString(R.string.ods))

            else
                changeFragment(OdsFragment(),resources.getString(R.string.ods))
        }
    }

    //// This is for Cancel and Call Helpline
    private fun showOptionLayout() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.option_menu_layout)
        val layoutParams: WindowManager.LayoutParams
        layoutParams = dialog.window.attributes
        layoutParams.gravity = Gravity.TOP or Gravity.RIGHT
        layoutParams.x = -100
        layoutParams.y = -100
        layoutParams.windowAnimations = R.style.DialogTheme

        dialog.textViewCancelRSA.setOnClickListener {
            dialog.dismiss()
            startActivity(
                Intent(this, RSARequestCancelActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            overridePendingTransition(R.anim.slide_down, R.anim.fade)
        }
        dialog.show()
    }

    ////CHANGE TABS BACKGROUND WITH CLICK
    private fun changeTabs(string: String)
    {
        when (string) {
            resources.getString(R.string.home) -> {
                ivHome.setImageResource(R.drawable.meters_click_tabbar)
                ivHome.setBackgroundColor(resources.getColor(R.color.white))

                ivRSA.setImageResource(R.drawable.car_tabbar)
                ivRSA.setBackgroundColor(resources.getColor(R.color.colorPrimary))

                ivList.setImageResource(R.drawable.car_docs_tabbar)
                ivList.setBackgroundColor(resources.getColor(R.color.colorPrimary))

                viewHome.visibility = View.VISIBLE
                viewRSA.visibility = View.INVISIBLE
                viewList.visibility = View.INVISIBLE
            }
            resources.getString(R.string.rsa_home), resources.getString(R.string.rsa_live) -> {
                ivHome.setImageResource(R.drawable.meters_tabbar)
                ivHome.setBackgroundColor(resources.getColor(R.color.colorPrimary))

                ivRSA.setImageResource(R.drawable.car_click_tabbar)
                ivRSA.setBackgroundColor(resources.getColor(R.color.white))

                ivList.setImageResource(R.drawable.car_docs_tabbar)
                ivList.setBackgroundColor(resources.getColor(R.color.colorPrimary))

                viewHome.visibility = View.INVISIBLE
                viewRSA.visibility = View.VISIBLE
                viewList.visibility = View.INVISIBLE
            }

            /*string==resources.getString(R.string.ods)|| string==resources.getString(R.string.OdsServiceOperation)*/
            else -> {
                ivHome.setImageResource(R.drawable.meters_tabbar)
                ivHome.setBackgroundColor(resources.getColor(R.color.colorPrimary))

                ivRSA.setImageResource(R.drawable.car_tabbar)
                ivRSA.setBackgroundColor(resources.getColor(R.color.colorPrimary))

                ivList.setImageResource(R.drawable.car_docs_click_tabbar)
                ivList.setBackgroundColor(resources.getColor(R.color.white))

                viewHome.visibility = View.INVISIBLE
                viewRSA.visibility = View.INVISIBLE
                viewList.visibility = View.VISIBLE
            }
        }
    }

    /////SET DATA TO DRAWER IF ANY CHANGES REQUIRED
    @SuppressLint("SetTextI18n")
    private fun setDataToDrawer() {
        tvName.text = CommonClass(this, this).getString("display_name")
        tvMembership.text = resources.getString(R.string.membership_id) +" "+
                CommonClass(this, this).getString("member_id")

        if(CommonClass(this,this).getString("user_image")!="")
        {
            val urlProfile = CommonClass(this,this).getString("user_url")+
                    CommonClass(this,this).getString("user_image")

            println("HERE IN ON RESUME >>>>>> $urlProfile")

            Picasso.get().load(urlProfile)
                .error(R.drawable.dummy_pic)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(ivProfileDrawer)
        }
    }


    /////DRAWER CLICKS HANDLED HERE
    private fun drawerClicks() {

        rlTop.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)

            Handler().postDelayed({
                startActivity(Intent(this, ProfileActivity::class.java))
            }, 500)
        }

        ivDrawer.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        ivCrossDrawer.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }

        tvMyOrders.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)

            Handler().postDelayed({
                startActivity(Intent(this, MyOrdersActivity::class.java))
            }, 500)
        }

        tvMyPassbook.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)

            Handler().postDelayed({
                startActivity(Intent(this, MyPassbookActivity::class.java))
            }, 500)
        }

        tvAddEmergencyContacts.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)

            Handler().postDelayed({
                startActivity(Intent(this, EmergencyContactsActivity::class.java))
            }, 500)
        }

        tvPaymentSettings.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)

            Handler().postDelayed({
                startActivity(Intent(this, PaymentSettingActivity::class.java))
            }, 500)
        }

        tvFreebies.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)

            Handler().postDelayed({
                startActivity(Intent(this, FreebiesActivity::class.java))
            }, 500)
        }

        tvHelp.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)

            Handler().postDelayed({
                startActivity(Intent(this, HelpActivity::class.java))
            }, 500)
        }


        tvSecuritySettings.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)

            Handler().postDelayed({
                startActivity(Intent(this, SecuritySettingActivity::class.java))
            }, 500)
        }


        tvLogout.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)

            Handler().postDelayed({
                showLogoutPopup()
            }, 500)
        }

        tvAddVehicles.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)

            Handler().postDelayed({
                startActivity(Intent(this, AddEditVehicleActivity::class.java))
            }, 500)
        }
    }


    ////CHECK IF RSA REQUEST IS ON WITH SCREENS
    private fun checkIfOnGoingRSAScreen() {

        if (CommonClass(this, this).getString(RsaConstants.RsaTypes.onGoingRsaScreen) == "YES") {

            fragmentTypeForRSA = CommonClass(this, this).getString(RsaConstants.RsaTypes.onGoingRsaScreenType)
            setFragmentsFromStackForRSA(fragmentTypeForRSA)
        }
    }

    // Method for Setting RSA fragment
    override fun setRSAFragments(type: String) {
        if (type != "")
            fragmentTypeForRSA = type

        setFragmentsFromStackForRSA(fragmentTypeForRSA)
    }

    ////SHOW HEADER ICONS AND CHANGES
    override fun changeHeaderIcons(showDrawer: Boolean, showSOS: Boolean, showCrossCancelRSA: Boolean) {

        if (showDrawer)
            ivDrawer.visibility = View.VISIBLE
        else
            ivDrawer.visibility = View.GONE

        if (showSOS)
            tvSOS.visibility = View.VISIBLE
        else
            tvSOS.visibility = View.GONE

        if (showCrossCancelRSA)
            imageViewOptionMenuDash.visibility = View.VISIBLE
        else
            imageViewOptionMenuDash.visibility = View.GONE
    }

    /////CHANGE RSA FRAGMENTS
    private fun setFragmentsFromStackForRSA(type: String) {

        fragmentTypeForRSA = type

        val fragment: Fragment = when (fragmentTypeForRSA) {
            resources.getString(R.string.rsa_home) -> RSAFragment()
            resources.getString(R.string.rsa_live) -> RSALiveFragment()
            else -> {
                fragmentTypeForRSA = resources.getString(R.string.rsa_home)
                RSAFragment()
            }
        }

        changeFragment(fragment, fragmentTypeForRSA)
    }

    /////CODE TO CHANGE FRAGMENTS IN APP
    public fun changeFragment(fragment: Fragment, tag: String) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()

        if (!fragmentEarlierBool)
            transaction.add(R.id.content_frame, fragment, tag)
        else
            transaction.remove(fragmentEarlier).add(R.id.content_frame, fragment, tag)

        if (tag != resources.getString(R.string.home)) {
            transaction.addToBackStack(null)
        }
        transaction.commitAllowingStateLoss()
        fragmentEarlier = fragment
        fragmentEarlierBool = true
        changeTabs(tag)
    }

    ///LOGOUT POPUP
    private fun showLogoutPopup() {

        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(resources.getString(R.string.logout_heading))
        alertDialog.setMessage(resources.getString(R.string.are_you_sure))
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK") { _, _ ->
            alertDialog.dismiss()
            if (CommonClass(this, this).checkInternetConn(this)) {
                val tokenHeader = CommonClass(this, this).getString("x_access_token")

                val userId = CommonClass(this, this).getString("_id")

                dashboardHandler.logout(userId, tokenHeader)
            }

            //logoutApi()
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CANCEL") { _, _ ->
            alertDialog.dismiss()
        }
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        }

        alertDialog.show()
    }

    private fun getEnv(): MyApplication {
        return application as MyApplication
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (supportFragmentManager.findFragmentById(R.id.content_frame) != null) {
            val f: Fragment = supportFragmentManager.findFragmentById(R.id.content_frame)!!
            if (f is HomeFragment) {
                rlTopDrawer.visibility = View.VISIBLE
                changeTabs(resources.getString(R.string.home))
            } else if (f is RSALiveFragment) {

            }
        } else {
            finish()
        }

    }

    private fun checkAndNavigateFromPush()
    {
        when {

            (CommonClass(this, this).getString(RsaConstants.ServiceSaved.isNew) == "1") -> {
                startActivity(
                    Intent(this, WaitingScreenBlue::class.java)
                        .putExtra("navigate_to", "0")
                )
            }

            intent.hasExtra("from_push") -> {
                println("FROM PUSH CHECK AND NAVIGATE")

                if(intent.getStringExtra("from_push")==FcmPushTypes.Types.cancelledByAdmin)
                {
                    println("FROM PUSH CHECK AND NAVIGATE TO HOME BECAUSE CANCELLED BY ADMIN")
                    fragmentTypeForRSA = ""
                    CommonClass(this, this).workDoneReviewSend()
                    changeFragment(HomeFragment(), resources.getString(R.string.home))
                }
                else
                {
                    checkStatusAPI()
                }
            }
            CommonClass(this, this).getString(RsaConstants.RsaTypes.checkStatus) != "" -> {
                val type: String = CommonClass(this, this).getString(RsaConstants.RsaTypes.checkStatus)
                when (type) {

                    FcmPushTypes.Types.accept -> {

                        if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.serviceNameSelected) ==
                            RsaConstants.ServiceName.flatTyre
                            ||
                            CommonClass(this, this).getString(RsaConstants.ServiceSaved.serviceNameSelected) ==
                            RsaConstants.ServiceName.burstTyre
                        ) {
                            CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")
                            CommonClass(this, this).putString(
                                RsaConstants.RsaTypes.onGoingRsaScreenType,
                                resources.getString(R.string.rsa_live)
                            )
                            checkIfOnGoingRSAScreen()
                        } else {
                            CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")
                            startActivity(
                                Intent(this, WorkSummaryActivity::class.java)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                        }
                    }

                    FcmPushTypes.Types.cancelledByAdmin -> {
                        fragmentTypeForRSA = ""
                        CommonClass(this, this).workDoneReviewSend()

                    }

                    FcmPushTypes.Types.fuelDefaultScreen -> {
                        CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")
                        CommonClass(this, this).putString(
                            RsaConstants.RsaTypes.onGoingRsaScreenType,
                            resources.getString(R.string.rsa_live)
                        )
                        checkIfOnGoingRSAScreen()
                    }

                    FcmPushTypes.Types.otpGenerated -> {

                        if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.isBlueScreenON) == "1") {
                            val intent = Intent()
                            intent.action = FcmPushTypes.Types.acceptRequestBroadCast
                            intent.putExtra("navigate_to", "1")
                            sendBroadcast(intent)
                        }

                        CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")
                        CommonClass(this, this).putString(
                            RsaConstants.RsaTypes.onGoingRsaScreenType,
                            resources.getString(R.string.rsa_live)
                        )
                        checkIfOnGoingRSAScreen()
                    }
                    FcmPushTypes.Types.inRouteRequest -> {

                        if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.isBlueScreenON) == "1") {
                            val intent = Intent()
                            intent.action = FcmPushTypes.Types.acceptRequestBroadCast
                            intent.putExtra("navigate_to", "1")
                            sendBroadcast(intent)
                        }

                        CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")
                        CommonClass(this, this).putString(
                            RsaConstants.RsaTypes.onGoingRsaScreenType,
                            resources.getString(R.string.rsa_live)
                        )
                        checkIfOnGoingRSAScreen()
                    }
                    FcmPushTypes.Types.newPin -> {

                        if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.isBlueScreenON) == "1") {
                            val intent = Intent()
                            intent.action = FcmPushTypes.Types.acceptRequestBroadCast
                            intent.putExtra("navigate_to", "1")
                            sendBroadcast(intent)
                        }

                        CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")
                        CommonClass(this, this).putString(
                            RsaConstants.RsaTypes.onGoingRsaScreenType,
                            resources.getString(R.string.rsa_live)
                        )
                        CommonClass(this, this).putString(
                            RsaConstants.RsaTypes.onGoingRsaLiveScreenType,
                            FcmPushTypes.Types.newPin
                        )
                        checkIfOnGoingRSAScreen()
                    }

                    FcmPushTypes.Types.otpGenerated -> {

                        if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.isBlueScreenON) == "1") {
                            val intent = Intent()
                            intent.action = FcmPushTypes.Types.acceptRequestBroadCast
                            intent.putExtra("navigate_to", "1")
                            sendBroadcast(intent)
                        }

                        CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")
                        CommonClass(this, this).putString(
                            RsaConstants.RsaTypes.onGoingRsaScreenType,
                            resources.getString(R.string.rsa_live)
                        )
                        CommonClass(this, this).putString(
                            RsaConstants.RsaTypes.onGoingRsaLiveScreenType,
                            FcmPushTypes.Types.newPin
                        )
                        checkIfOnGoingRSAScreen()
                    }
                    FcmPushTypes.Types.otpVerified -> {

                        if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.serviceNameSelected) ==
                            RsaConstants.ServiceName.flatTyre
                            ||
                            CommonClass(this, this).getString(RsaConstants.ServiceSaved.serviceNameSelected) ==
                            RsaConstants.ServiceName.burstTyre
                        ) {
                            startActivity(
                                Intent(this, WaitingScreenWhite::class.java)
                                    .putExtra("from_where", "wallet_accessing")
                            )
                        } else {
                            CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")
                            startActivity(Intent(this, GetSetGoActivity::class.java).putExtra("navigate_to", "START"))
                        }
                    }
                    FcmPushTypes.Types.moneyRequested -> {

                        CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")
                        startActivity(
                            Intent(this, WorkSummaryActivity::class.java)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    }

                    FcmPushTypes.Types.moneyPaid -> {


                        if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.serviceNameSelected) ==
                            RsaConstants.ServiceName.flatTyre
                            ||
                            CommonClass(this, this).getString(RsaConstants.ServiceSaved.serviceNameSelected) ==
                            RsaConstants.ServiceName.burstTyre
                        ) {
                            CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")
                            startActivity(Intent(this, GetSetGoActivity::class.java).putExtra("navigate_to", "Start"))
                        } else {
                            CommonClass(this, this).putString(
                                RsaConstants.RsaTypes.checkStatus,
                                FcmPushTypes.Types.inRouteRequest
                            )
                            CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")
                            CommonClass(this, this).putString(
                                RsaConstants.RsaTypes.onGoingRsaScreenType,
                                resources.getString(R.string.rsa_live)
                            )


                            if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.serviceNameSelected) ==
                                RsaConstants.ServiceName.fuelDelivery
                            ) {
                                CommonClass(this, this).putString(
                                    RsaConstants.RsaTypes.checkStatus,
                                    FcmPushTypes.Types.fuelDefaultScreen
                                )
                            }

                            checkIfOnGoingRSAScreen()
                        }
                    }
                    FcmPushTypes.Types.startedWork -> {

                        CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")
                        startActivity(
                            Intent(this, GetSetGoActivity::class.java).putExtra(
                                "navigate_to",
                                FcmPushTypes.Types.startedWork
                            )
                        )
                    }
                    FcmPushTypes.Types.workEnded -> {

                        CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")
                        startActivity(
                            Intent(this, GetSetGoActivity::class.java).putExtra(
                                "navigate_to",
                                FcmPushTypes.Types.workEnded
                            )
                        )
                    }

                    FcmPushTypes.Types.requestCancelled -> {

                        fragmentTypeForRSA = ""
                        Toast.makeText(
                            this, CommonClass(this, this)
                                .getString(RsaConstants.ServiceSaved.cancellationMessage), Toast.LENGTH_SHORT
                        ).show()

                        CommonClass(this, this).workDoneReviewSend()

                        changeFragment(HomeFragment(), resources.getString(R.string.home))
                    }
                    else -> {
                        changeFragment(HomeFragment(), resources.getString(R.string.home))
                    }
                }
            }
            else -> changeFragment(HomeFragment(), resources.getString(R.string.home))
        }
    }

    override fun onResume() {
        super.onResume()

        // checkIfOnGoingRSAScreen()

        val filter = IntentFilter(FcmPushTypes.Types.fromAPIBroadCast)
        registerReceiver(changeRSALiveScreenReceiver, filter)

        val filterStatus = IntentFilter(FcmPushTypes.Types.checkStatusPushOneTime)
        registerReceiver(changeStatusReceiver, filterStatus)

        setDataToDrawer()
    }

    private val changeRSALiveScreenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            println("GOT BROADCAST API ")

            checkAndNavigateFromPush()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            unregisterReceiver(changeRSALiveScreenReceiver)
            unregisterReceiver(changeStatusReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onRequestSuccessReport(mainPojo: MainPojo) {
        if (mainPojo.success == "true") {
            CommonClass(this, this).clearPreference()
        } else {
            Toast.makeText(this, mainPojo.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showLoader() {

    }

    override fun hideLoader() {

    }


    //////////////////////////FETCH STATUS API////////////////////////////////////

    private val changeStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            println("GOT BROADCAST API CHECK STATUS ")

            checkStatusAPI()
        }
    }

    fun checkStatusAPI() {
        val rsaLiveHandler = FetchStatusPresenter(this, this)

        if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.fobbuRequestId) != "") {
            rsaLiveHandler.getServiceOneTime(
                CommonClass(this, this).getString("x_access_token"),
                CommonClass(this, this).getString(RsaConstants.ServiceSaved.fobbuRequestId)
            )
        }
        else
        {
            println("ELSE CASE HERE  ")


            fragmentTypeForRSA = ""
            CommonClass(this, this).workDoneReviewSend()
            changeFragment(HomeFragment(), resources.getString(R.string.home))
        }
    }

    override fun onRequestSuccessReportGetService(mainPojo: MainPojo) {
        try {
            if (mainPojo.success == "true") {
                if (CommonClass(this, this).getString(RsaConstants.RsaTypes.checkStatus) !=
                    mainPojo.getData().status
                ) {
                    println("SAVED AND MOVED YEAHH IN CHECK STATUS API")

                    CommonClass(this, this).putString(
                        RsaConstants.ServiceSaved.serviceNameSelected,
                        mainPojo.getData().static_name
                    )

                    CommonClass(this, this).putString(RsaConstants.ServiceSaved.fobbuRequestId, mainPojo.getData()._id)

                    CommonClass(this, this).putString(RsaConstants.RsaTypes.checkIfOnGoingRsaRequest, "YES")

                    CommonClass(this, this).putString(RsaConstants.RsaTypes.checkStatus, mainPojo.getData().status)

                    val otp = if (mainPojo.getData().otp.contains("\\.".toRegex())) {
                        mainPojo.getData().otp.split("\\.".toRegex())[0]
                    } else
                        mainPojo.getData().otp

                    CommonClass(this, this).putString(RsaConstants.ServiceSaved.otpStart, otp)

                    CommonClass(this, this).putString(
                        RsaConstants.ServiceSaved.cancellationMessage, mainPojo.getData().reason_of_cancellation
                    )

                    if (intent.hasExtra("from_push")) {
                        intent.removeExtra("from_push")
                        println("APP CLOSED ")
                        checkAndNavigateFromPush()
                    } else {
                        ifAppIsOpen()
                    }

                } else {
                    if (intent.hasExtra("from_push")) {
                        intent.removeExtra("from_push")
                        println("APP CLOSED ")
                        checkAndNavigateFromPush()
                    } else {
                        println("NOT SAVED BECAUSE SAME ")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun ifAppIsOpen() {

        val status = CommonClass(this, this).getString(RsaConstants.RsaTypes.checkStatus)

        println("HERE IN DASHBOARD IF OPEN $status")

        when (status) {

            FcmPushTypes.Types.accept -> {

                val intent = Intent()
                intent.action = FcmPushTypes.Types.acceptRequestBroadCast
                intent.putExtra("navigate_to", "1")
                sendBroadcast(intent)
            }
            FcmPushTypes.Types.cancelledByAdmin -> {

                val intent = Intent()
                intent.action = FcmPushTypes.Types.acceptRequestBroadCast
                intent.putExtra("navigate_to", "4")
                intent.putExtra(
                    "message", CommonClass(this, this)
                        .getString(RsaConstants.ServiceSaved.cancellationMessage)
                )
                sendBroadcast(intent)
            }
            FcmPushTypes.Types.inRouteRequest -> {

                if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.isBlueScreenON) == "1") {
                    val intent = Intent()
                    intent.action = FcmPushTypes.Types.acceptRequestBroadCast
                    intent.putExtra("navigate_to", "1")
                    sendBroadcast(intent)
                }

                val intent = Intent()
                intent.action = FcmPushTypes.Types.inRouteRequestBroadCast
                intent.putExtra("navigate_to", FcmPushTypes.Types.inRouteRequest)
                sendBroadcast(intent)
            }
            FcmPushTypes.Types.newPin -> {

                if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.isBlueScreenON) == "1") {
                    val intent = Intent()
                    intent.action = FcmPushTypes.Types.acceptRequestBroadCast
                    intent.putExtra("navigate_to", "1")
                    sendBroadcast(intent)
                }

                val intent = Intent()
                intent.action = FcmPushTypes.Types.inRouteRequestBroadCast
                intent.putExtra("navigate_to", FcmPushTypes.Types.newPin)
                intent.putExtra("otp", "")
                sendBroadcast(intent)
            }
            FcmPushTypes.Types.otpGenerated -> {

                if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.isBlueScreenON) == "1") {
                    val intent = Intent()
                    intent.action = FcmPushTypes.Types.acceptRequestBroadCast
                    intent.putExtra("navigate_to", "1")
                    sendBroadcast(intent)
                }

                val intent = Intent()
                intent.action = FcmPushTypes.Types.inRouteRequestBroadCast
                intent.putExtra("navigate_to", FcmPushTypes.Types.newPin)
                intent.putExtra("otp", "")
                sendBroadcast(intent)
            }
            FcmPushTypes.Types.otpVerified -> {
                val intent = Intent()
                intent.action = FcmPushTypes.Types.inRouteRequestBroadCast
                intent.putExtra("navigate_to", FcmPushTypes.Types.otpVerified)
                sendBroadcast(intent)
            }
            FcmPushTypes.Types.moneyRequested -> {
                val intent = Intent()
                intent.action = FcmPushTypes.Types.moneyRequestedBroadcast
                intent.putExtra("navigate_to", FcmPushTypes.Types.moneyRequested)
                sendBroadcast(intent)
            }
            FcmPushTypes.Types.startedWork -> {
                val intent = Intent()
                intent.action = FcmPushTypes.Types.startedWorkBroadcast
                intent.putExtra("navigate_to", FcmPushTypes.Types.startedWork)
                sendBroadcast(intent)
            }
            FcmPushTypes.Types.workEnded -> {
                val intent = Intent()
                intent.action = FcmPushTypes.Types.startedWorkBroadcast
                intent.putExtra("navigate_to", FcmPushTypes.Types.workEnded)
                sendBroadcast(intent)
            }
            FcmPushTypes.Types.requestCancelled -> {
                Toast.makeText(
                    this, CommonClass(this, this)
                        .getString(RsaConstants.ServiceSaved.cancellationMessage), Toast.LENGTH_SHORT
                ).show()

                CommonClass(this, this).workDoneReviewSend()

                changeFragment(HomeFragment(), resources.getString(R.string.home))

                fragmentTypeForRSA = ""
            }
            else -> {
                if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.isNew) == "1") {
                    startActivity(
                        Intent(this, WaitingScreenBlue::class.java)
                            .putExtra("navigate_to", "0")
                    )
                }
            }
        }
    }
}