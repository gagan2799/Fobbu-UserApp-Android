package com.fobbu.member.android.activities.freebies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fobbu.member.android.R
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_freebies.*
import kotlinx.android.synthetic.main.toolbar.*
import android.content.Intent
import com.fobbu.member.android.BuildConfig


class FreebiesActivity : AppCompatActivity()
{
    lateinit var commonClass:CommonClass

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_freebies)

        initView()

        clicks()
    }

    // function for initialising all the variables of the class
    private fun initView()
    {
        commonClass= CommonClass(this,this)

        ivSearchToolbar.visibility= View.INVISIBLE
    }

    //function for handling all the clicks of the class
     fun clicks()
    {
        ivBackButton.setOnClickListener {
            finish()
        }


        tvInviteFriends.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)

                shareIntent.type = "text/plain"

                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "FOBBU")

                val shareMessage = "Invite your friends to be a part of fobbu."

                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)

                startActivity(Intent.createChooser(shareIntent, "Share"))
            }
            catch (e: Exception)
            {
                //e.toString();
            }

        }

    }
}
