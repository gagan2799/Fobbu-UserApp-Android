package com.fobbu.member.android.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.profile.ProfileActivity
import com.fobbu.member.android.activities.vehicleModule.AddEditVehicleActivity
import com.fobbu.member.android.interfaces.ChangeRSAFragments
import com.fobbu.member.android.interfaces.HeaderIconChanges
import com.fobbu.member.android.interfaces.TopBarChanges
import com.fobbu.member.android.utils.CommonClass

class HomeFragment : Fragment() {

    private var changeRSAFragments: ChangeRSAFragments? = null

    private lateinit var llRSA: LinearLayout

    private var headerIconChanges: HeaderIconChanges? = null
    private var topBarChanges:TopBarChanges?=null

    private lateinit var llAddNewVehicle:LinearLayout

    private lateinit var tvMembershipIdHome:TextView
    lateinit var llProfile:LinearLayout

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

        llAddNewVehicle.setOnClickListener {
            activity!!.startActivity(Intent(activity!!, AddEditVehicleActivity::class.java))
        }

        llProfile.setOnClickListener {
            activity!!.startActivity(Intent(activity!!, ProfileActivity::class.java))
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initialise(view: View?) {

        headerIconChanges = activity as HeaderIconChanges?
        headerIconChanges!!.changeHeaderIcons(true, false, false)

        topBarChanges = activity as TopBarChanges
        topBarChanges!!.showGoneTopBar(true)

        changeRSAFragments = activity!! as ChangeRSAFragments

        llAddNewVehicle = view!!.findViewById(R.id.llAddNewVehicle)
        tvMembershipIdHome = view.findViewById(R.id.tvMembershipIdHome)

        tvMembershipIdHome.text = resources.getString(R.string.your_membership_id) + " "+
                CommonClass(activity!!,activity!!).getString("member_id")

        llRSA = view.findViewById(R.id.llRSA)

        llProfile = view.findViewById(R.id.llProfile)
    }
}