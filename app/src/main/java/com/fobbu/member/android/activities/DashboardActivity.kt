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
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.rsaModule.RSARequestCancelActivity
import com.fobbu.member.android.activities.vehicleModule.AddEditVehicleActivity
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenBlue
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenWhite
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.fcm.FcmPushTypes
import com.fobbu.member.android.fragments.HomeFragment

import com.fobbu.member.android.fragments.RSALiveFragment
import com.fobbu.member.android.fragments.rsaFragmentModule.RSAFragment
import com.fobbu.member.android.interfaces.ChangeRSAFragments
import com.fobbu.member.android.interfaces.HeaderIconChanges
import com.fobbu.member.android.interfaces.TopBarChanges
import com.fobbu.member.android.utils.CommonClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.inflate_drawer.*
import kotlinx.android.synthetic.main.option_menu_layout.*

class DashboardActivity : AppCompatActivity(), HeaderIconChanges, ChangeRSAFragments, TopBarChanges {

    private var topBarChanges: TopBarChanges? = null
    var fragmentTypeForRSA = ""
    private lateinit var fragmentEarlier: Fragment
    var fragmentEarlierBool = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setDataToDrawer()
        drawerClicks()
        tabBarClicks()
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
    private fun changeTabs(string: String) {
        if (string == resources.getString(R.string.home)) {
            ivHome.setImageResource(R.drawable.meters_click_tabbar)
            ivHome.setBackgroundColor(resources.getColor(R.color.white))

            ivRSA.setImageResource(R.drawable.car_tabbar)
            ivRSA.setBackgroundColor(resources.getColor(R.color.colorPrimary))

            viewHome.visibility=View.VISIBLE
            viewRSA.visibility=View.INVISIBLE

        } else if (string == resources.getString(R.string.rsa_home) || string == resources.getString(R.string.rsa_live)) {

            ivHome.setImageResource(R.drawable.meters_tabbar)
            ivHome.setBackgroundColor(resources.getColor(R.color.colorPrimary))

            ivRSA.setImageResource(R.drawable.car_click_tabbar)
            ivRSA.setBackgroundColor(resources.getColor(R.color.white))
            viewHome.visibility=View.INVISIBLE
            viewRSA.visibility=View.VISIBLE
        }

    }

    /////SET DATA TO DRAWER IF ANY CHANGES REQUIRED
    @SuppressLint("SetTextI18n")
    private fun setDataToDrawer() {
        tvName.text = CommonClass(this, this).getString("display_name")
        tvMembership.text = resources.getString(R.string.membership_id) +
                CommonClass(this, this).getString("membership_id")
    }


    /////DRAWER CLICKS HANDLED HERE
    private fun drawerClicks() {

        ivDrawer.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        ivCrossDrawer.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
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

        if (CommonClass(this, this).getString("OnGoingRSA_Screen") == "YES") {
          //  CommonClass(this, this).putString("OnGoingRSA_Screen", "")
            fragmentTypeForRSA = CommonClass(this, this).getString("OnGoingRSA_Screen_Type")
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
    private fun changeFragment(fragment: Fragment, tag: String) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()

        if (!fragmentEarlierBool)
            transaction.add(R.id.content_frame, fragment, tag)
        else
            transaction.remove(fragmentEarlier).add(R.id.content_frame, fragment, tag)

        if (tag != resources.getString(R.string.home)) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
        fragmentEarlier = fragment
        fragmentEarlierBool=true
        changeTabs(tag)
    }

    ///LOGOUT POPUP
    private fun showLogoutPopup() {

        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(resources.getString(R.string.logout_heading))
        alertDialog.setMessage(resources.getString(R.string.are_you_sure))
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK") { _, _ ->
            alertDialog.dismiss()
            CommonClass(this, this).clearPreference()
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
        val f: Fragment = supportFragmentManager.findFragmentById(R.id.content_frame)!!
        if (f is HomeFragment) {
            rlTopDrawer.visibility = View.VISIBLE
            changeTabs(resources.getString(R.string.home))
        }
        else if(f is RSALiveFragment)
        {

        }
    }

    private fun checkAndNavigateFromPush() {

        if (intent.hasExtra("from_push")) {
            val type: String = intent.getStringExtra("from_push")
            when (type) {

                FcmPushTypes.Types.accept -> {
                    startActivity(Intent(this, WaitingScreenBlue::class.java).putExtra("navigate_to", "1"))
                }
                FcmPushTypes.Types.inRouteRequest -> {

                    CommonClass(this, this).putString("OnGoingRSA_Screen", "YES")
                    CommonClass(this, this).putString("OnGoingRSA_Screen_Type", resources.getString(R.string.rsa_live))
                    checkIfOnGoingRSAScreen()
                }
                else -> {

                }
            }
        } else
            changeFragment(HomeFragment(), resources.getString(R.string.home))
    }

    override fun onResume() {
        super.onResume()

        checkIfOnGoingRSAScreen()

        /* val filter = IntentFilter(FcmPushTypes.Types.inRouteRequestBroadCast)
         registerReceiver(changeRSALiveScreenReceiver, filter)*/
    }

    private val changeRSALiveScreenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {


        }

    }

}