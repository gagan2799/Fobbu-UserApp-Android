package com.fobbu.member.android.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.fobbu.member.android.R
import kotlinx.android.synthetic.main.activity_waiting_screen_white.*

class WaitingScreenWhite : AppCompatActivity() {
    private var strWhich = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_waiting_screen_white)
        handleclicks()
    }

    private fun handleclicks() {

        strWhich = intent.getStringExtra("from_where")

        when (strWhich) {
            "building_live" -> {
                rlBuildingLiveTrack.visibility=View.VISIBLE
                rlCodeValidated.visibility=View.GONE
                rlAccessingVehicle.visibility=View.GONE
            }
            "code_valid" -> {
                rlBuildingLiveTrack.visibility=View.GONE
                rlCodeValidated.visibility=View.VISIBLE
                rlAccessingVehicle.visibility=View.GONE
            }
            "wallet_accessing" -> {
                rlAccessingVehicle.visibility=View.VISIBLE
                rlBuildingLiveTrack.visibility=View.GONE
                rlCodeValidated.visibility=View.GONE
            }
        }


        rlBuildingLiveTrack.setOnClickListener {

        }
    }

    override fun onBackPressed() {

    }
}