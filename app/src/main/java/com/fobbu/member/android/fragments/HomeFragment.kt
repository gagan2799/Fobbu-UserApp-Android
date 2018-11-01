package com.fobbu.member.android.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.fobbu.member.android.R
import com.fobbu.member.android.interfaces.ChangeRSAFragments
import com.fobbu.member.android.interfaces.HeaderIconChanges

class HomeFragment : Fragment() {

    private var changeRSAFragments: ChangeRSAFragments? = null

    private lateinit var llRSA: LinearLayout

    private var headerIconChanges: HeaderIconChanges? = null

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

        llRSA.setOnClickListener {
            changeRSAFragments!!.setRSAFragments("")
        }

    }

    private fun initialise(view: View?) {

        headerIconChanges = activity as HeaderIconChanges?
        headerIconChanges!!.changeHeaderIcons(true, false, false)

        changeRSAFragments = activity!! as ChangeRSAFragments

        llRSA = view!!.findViewById(R.id.llRSA)


    }
}