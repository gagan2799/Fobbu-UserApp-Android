package com.fobbu.member.android.activities.dashboardActivity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
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
import com.fobbu.member.android.activities.cancelRsaActivity.CancelRSAActivity
import com.fobbu.member.android.activities.addEditVehicleActivity.AddEditVehicleActivity
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.fragments.HomeFragment
import com.fobbu.member.android.fragments.RSAFragment.RSAFragment
import com.fobbu.member.android.fragments.RSALiveFragment
import com.fobbu.member.android.interfaces.ChangeRSAFragments
import com.fobbu.member.android.interfaces.HeaderIconChanges
import com.fobbu.member.android.interfaces.TopBarChanges
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.inflate_drawer.*
import kotlinx.android.synthetic.main.option_menu_layout.*

class DashboardActivity : AppCompatActivity(), HeaderIconChanges, ChangeRSAFragments,TopBarChanges {


    var fragmentTypeForRSA = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setDataToDrawer()
        drawerClicks()
        tabBarClicks()
        changeFragment(HomeFragment(), resources.getString(R.string.home))
}

    override fun showGoneTopBar(showDrawer: Boolean) {
        if(showDrawer)
            rlTopDrawer.visibility=View.VISIBLE
        else
            rlTopDrawer.visibility=View.GONE
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


    private fun showOptionLayout() {
        val dialog:Dialog= Dialog(this)
        dialog.setContentView(R.layout.option_menu_layout)
        var layoutParams=WindowManager.LayoutParams()
        layoutParams=dialog.window.attributes
        layoutParams.gravity=Gravity.TOP or Gravity.RIGHT
        layoutParams.x=-100
        layoutParams.y=-100
        layoutParams.windowAnimations=R.style.DialogTheme

        dialog.textViewCancelRSA.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, CancelRSAActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
            overridePendingTransition(R.anim.slide_down,R.anim.fade)
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

        } else if (string == resources.getString(R.string.rsa_home) || string == resources.getString(R.string.rsa_live)) {

            ivHome.setImageResource(R.drawable.meters_tabbar)
            ivHome.setBackgroundColor(resources.getColor(R.color.colorPrimary))

            ivRSA.setImageResource(R.drawable.car_click_tabbar)
            ivRSA.setBackgroundColor(resources.getColor(R.color.white))
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


    override fun onResume() {
        super.onResume()

        checkIfOnGoingRSAScreen()
    }

    ////CHECK IF RSA REQUEST IS ON WITH SCREENS
    private fun checkIfOnGoingRSAScreen() {

        if (CommonClass(this, this).getString("OnGoingRSA_Screen") == "YES") {
            CommonClass(this, this).putString("OnGoingRSA_Screen", "")
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
            ivCrossRSA.visibility = View.VISIBLE
        else
            ivCrossRSA.visibility = View.GONE
    }

    /////CHANGE RSA FRAGMENTS
    private fun setFragmentsFromStackForRSA( type: String) {

        fragmentTypeForRSA = type

        val fragment: Fragment = when (fragmentTypeForRSA) {
            resources.getString(R.string.rsa_home) -> RSAFragment()
            resources.getString(R.string.rsa_live) -> RSALiveFragment()
            else ->
            {
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
        transaction.add(R.id.content_frame, fragment, tag)
        if (tag != resources.getString(R.string.home)) {
            transaction.addToBackStack(null)
        }
        transaction.commit()

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
}