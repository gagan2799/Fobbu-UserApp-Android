package com.fobbu.member.android.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fobbu.member.android.R

class HomeFragment: Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        if (view != null) {

            initialise(view)

            handleClick()
        }
        return view
    }

    private fun handleClick() {

    }

    private fun initialise(view: View?) {

    }
}