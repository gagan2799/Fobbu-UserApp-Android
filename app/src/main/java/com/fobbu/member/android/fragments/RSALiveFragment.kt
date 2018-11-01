package com.fobbu.member.android.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.WaitingScreenBlue
import com.fobbu.member.android.interfaces.HeaderIconChanges

class RSALiveFragment : Fragment() {

    private var headerIconChanges: HeaderIconChanges? = null

    private lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rsa_live, container, false)

        if (view != null) {

            initialise(view)

            handleClick()
        }
        return view
    }

    private fun handleClick() {


    }

    private fun initialise(view: View?) {

        headerIconChanges = activity as HeaderIconChanges?
        headerIconChanges!!.changeHeaderIcons(false, true, true)

        coordinatorLayout = view!!.findViewById(R.id.coordinator) as CoordinatorLayout
        initPersistentBottomsheet()
    }


    @SuppressLint("SetTextI18n", "InflateParams")
    fun showPaymentPopupFinal(name: String) {

        val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val cViewFinalPopup = inflater.inflate(R.layout.fragment_builder_confirm, null)

        val builderFinal = Dialog(activity!!)

        builderFinal.requestWindowFeature(Window.FEATURE_NO_TITLE)

        builderFinal.setCancelable(false)

        builderFinal.setContentView(cViewFinalPopup)

        builderFinal.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val tvCancel = cViewFinalPopup.findViewById(R.id.tvCancel) as TextView

        val tvText = cViewFinalPopup.findViewById(R.id.tvText) as TextView
        tvText.text = name

        val tvConfirm = cViewFinalPopup.findViewById(R.id.tvConfirm) as TextView

        tvCancel.setOnClickListener {
            builderFinal.dismiss()

        }
        tvConfirm.setOnClickListener {
            builderFinal.dismiss()

            activity!!.startActivity(Intent(activity!!, WaitingScreenBlue::class.java))
        }

        builderFinal.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        builderFinal.show()
    }


    private fun initPersistentBottomsheet() {
        val persistentbottomSheet = coordinatorLayout.findViewById<LinearLayout>(R.id.bottomsheet)
        val rlBottomSheet = persistentbottomSheet.findViewById(R.id.rlBottomSheet) as RelativeLayout
        val behavior = BottomSheetBehavior.from<View>(persistentbottomSheet)


        rlBottomSheet.setOnClickListener {
            if (behavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }

        behavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                //showing the different states
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events

            }
        })

    }


}