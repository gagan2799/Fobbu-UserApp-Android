package com.fobbu.member.android.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.fobbu.member.android.R
import kotlinx.android.synthetic.main.activity_waiting_screen_blue.*

class WaitingScreenBlue : AppCompatActivity() {
    private var strWhich = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_waiting_screen_blue)
        handleclicks()
    }

    private fun handleclicks() {

        rlLayout.setOnClickListener {
            when (strWhich) {
                "0" -> {
                    strWhich="1"
                    tvTextOne.text=resources.getString(R.string.awesome)
                    tvTextTwo.text=resources.getString(R.string.fobbu_found)
                    tvTextThree.visibility=View.GONE
                }
                "1" -> {
                    strWhich="2"
                    tvTextOne.text=resources.getString(R.string.wonderful)
                    tvTextTwo.text=resources.getString(R.string.fobbu_confirmed_request)
                    tvTextThree.text=resources.getString(R.string.fobbu_gathering_tools)
                    tvTextThree.visibility=View.VISIBLE
                }
                "2" -> {
                    strWhich="3"

                    startActivity(Intent(this,AddEditVehicleActivity::class.java)
                        .putExtra("from_where","RSA"))
                }
            }

        }
    }

    override fun onBackPressed() {

    }
}