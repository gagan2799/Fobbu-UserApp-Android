package com.fobbu.member.android.tutorial

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fobbu.member.android.R
import pl.droidsonroids.gif.GifImageView

class TutorialFragment1 : Fragment()
{
    lateinit var gif: GifImageView

    lateinit var tvText: TextView

    lateinit var tvTextHeading: TextView

    lateinit var tvTextFullForm: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.inflate_tutorials, container, false)

        initialise(view)   // function for initialising all the variables and views of the class

        return view
    }

    // function for initialising all the variables and views of the class
    private fun initialise(view: View?)
    {
        gif = view!!.findViewById(R.id.gif)

        tvTextHeading = view.findViewById(R.id.tvTextHeading)

        tvTextHeading.text = resources.getString(R.string.rsa)

        tvTextFullForm = view.findViewById(R.id.tvTextFullForm)

        tvTextFullForm.text = resources.getString(R.string.rsa_fullform)

        tvText = view.findViewById(R.id.tvText)

        tvText.text = resources.getString(R.string.rsa_tutorial_text)

        gif.visibility = View.VISIBLE

        gif.postDelayed({ gif.setBackgroundResource(R.drawable.firstgif) }, 500)
    }

    override fun onResume()
    {
        super.onResume()

        val filter3 = IntentFilter("com.fobbu.position_start_")

        activity!!.registerReceiver(startGif, filter3)
    }

    override fun onDestroy()
    {
        super.onDestroy()

        try
        {
            activity!!.unregisterReceiver(startGif)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    // initialising startGif Broadcast Receiver
    private val startGif = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent)
        {
            val position = Integer.valueOf(intent.getStringExtra("position"))

            activity!!.runOnUiThread {
                if (position == 0)
                    gif.postDelayed({ gif.setBackgroundResource(R.drawable.firstgif) }, 500)
            }
        }
    }
}