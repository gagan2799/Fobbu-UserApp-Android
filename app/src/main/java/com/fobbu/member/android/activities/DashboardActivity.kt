package com.fobbu.member.android.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.fobbu.member.android.R
import com.fobbu.member.android.fragments.HomeFragment
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.inflate_drawer.*

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setDataToDrawer()
        drawerClicks()
        tabBarClicks()
        changeFragment(HomeFragment(),resources.getString(R.string.home))
    }

    private fun changeFragment(fragment: Fragment, tag: String) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(R.id.content_frame, fragment, tag)
        if (tag != resources.getString(R.string.home)) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    private fun tabBarClicks() {

        llHome.setOnClickListener {

            ivHome.setImageResource(R.drawable.meters_click_tabbar)
            ivHome.setBackgroundColor(resources.getColor(R.color.white))

            ivRSA.setImageResource(R.drawable.car_tabbar)
            ivRSA.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        }

        llRSA.setOnClickListener {

            ivHome.setImageResource(R.drawable.meters_tabbar)
            ivHome.setBackgroundColor(resources.getColor(R.color.colorPrimary))

            ivRSA.setImageResource(R.drawable.car_click_tabbar)
            ivRSA.setBackgroundColor(resources.getColor(R.color.white))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToDrawer() {
        tvName.text = CommonClass(this,this).getString("display_name")
        tvMembership.text = resources.getString(R.string.membership_id)+ CommonClass(this,this).getString("membership_id")
    }

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
                //HANDLE WHERE SHOULD APP LAND IN
                showLogoutPopup()
            }, 500)
        }
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
}