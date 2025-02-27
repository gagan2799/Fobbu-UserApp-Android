package com.fobbu.member.android.activities.paymentModule

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboard.DashboardActivity
import com.fobbu.member.android.activities.paymentModule.presenter.GetSetGoHandler
import com.fobbu.member.android.activities.paymentModule.presenter.GetSetGoPresenter
import com.fobbu.member.android.activities.rsaModule.RSARequestCancelActivity
import com.fobbu.member.android.fcm.FcmPushTypes
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_get_set_go.*
import kotlinx.android.synthetic.main.inflate_review_pop_up.view.*
import kotlinx.android.synthetic.main.option_menu_layout.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.lang.Exception

class GetSetGoActivity : AppCompatActivity(), ActivityView
{
    lateinit var getSetGoHandler: GetSetGoHandler

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_get_set_go)

        initView()         // function for initialising all the variables of the class

        clicks()             // function for handling all the clicks of the class
    }

    // function for initialising all the variables of the class
    private fun initView()
    {
       CommonClass(this,this).cancelNotification()

        linearLayoutHolderGet.background=resources.getDrawable(R.drawable.work_summary_drawable)

        getSetGoHandler = GetSetGoPresenter(this, this)

        // function for managing the UI as per the service's name
        manageService(CommonClass(this, this).getString(RsaConstants.ServiceSaved.serviceNameSelected))

        if (intent.hasExtra("navigate_to"))
        {
            when
            {
                intent.getStringExtra("navigate_to") == FcmPushTypes.Types.startedWork ->
                    changeLayout("Set")             // function for changing the UI of get_set_go layout

                intent.getStringExtra("navigate_to") == FcmPushTypes.Types.workEnded ->
                    changeLayout("")            // function for changing the UI of get_set_go layout

                else -> changeLayout("Get")            // function for changing the UI of get_set_go layout
            }
        }
        else
            changeLayout("Get")          // function for changing the UI of get_set_go layout

    }


    // function for handling all the clicks of the class
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun clicks()
    {

        linearlayoutNotNowGet.setOnClickListener {
            CommonClass(this, this).workDoneReviewSend()

            Handler().postDelayed(
                {
                    startActivity(Intent(this, DashboardActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                },10
            )
        }

        buttonSubmitGet.setOnClickListener {
            if (CommonClass(this, this).checkInternetConn(this))
            {
                when
                {
                    ratinBarGetSet.rating.toString() == "0.0" -> Toast.makeText(this, getString(R.string.provide_rating), Toast.LENGTH_SHORT).show()

                    editTextReviewGet.text.trim().isEmpty() -> Toast.makeText(this, getString(R.string.provide_review), Toast.LENGTH_SHORT).show()

                    else ->
                    {
                        hideKeyboard()                // function for hiding keyboard

                        postReviews()                 // implementing the provide_ratings API
                    }
                }
            }
            else
                Toast.makeText(this, getString(R.string.internet_is_unavailable), Toast.LENGTH_SHORT).show()
        }

        imageViewOptionMenuGetSetSummary.setOnClickListener {
            shareIt()         // function for opening sharing options bottom sheet
        }
    }

    // function for hiding keyboard
    private fun hideKeyboard()
    {
        val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus

        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null)
            view = View(this)

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // function for changing the UI of get_set_go layout
    @SuppressLint("NewApi")
    private fun changeLayout(s: String)
    {
        when (s)
        {
            "Get" ->
            {
                viewGetAbove.setBackgroundColor(resources.getColor(R.color.red))

                viewGetBelow.setBackgroundColor(resources.getColor(R.color.red))

                linearLayoutGetMiddle.background = resources.getDrawable(R.drawable.circular_red)

                textViewGet.setTextColor(resources.getColor(R.color.red))

                textViewGetDetails.setTextColor(resources.getColor(R.color.red))

                textViewNumberGet.visibility = View.GONE

                imageViewWhiteCheckGet.visibility = View.VISIBLE

                linearLayoutServiceGetSet.visibility = View.VISIBLE

                linearlayoutReviewGet.visibility = View.GONE
            }

            "Set" ->
            {
                viewGetAbove.setBackgroundColor(resources.getColor(R.color.red))

                viewGetBelow.setBackgroundColor(resources.getColor(R.color.red))

                viewSetAbove.setBackgroundColor(resources.getColor(R.color.red))

                viewSetBelow.setBackgroundColor(resources.getColor(R.color.red))

                textViewGet.setTextColor(resources.getColor(R.color.red))

                textViewGetDetails.setTextColor(resources.getColor(R.color.red))

                linearLayoutGetMiddle.background = resources.getDrawable(R.drawable.circular_red)

                linearLayoutSetMiddle.background = resources.getDrawable(R.drawable.circular_red)

                textViewSet.setTextColor(resources.getColor(R.color.red))

                textViewSetDetails.setTextColor(resources.getColor(R.color.red))

                textViewNumberSet.visibility = View.GONE

                imageViewWhiteCheckSet.visibility = View.VISIBLE

                linearLayoutServiceGetSet.visibility = View.VISIBLE

                linearlayoutReviewGet.visibility = View.GONE

                textViewNumberGet.visibility = View.GONE

                imageViewWhiteCheckGet.visibility = View.VISIBLE
            }

            else ->
            {
                viewGetAbove.setBackgroundColor(resources.getColor(R.color.red))

                viewGetBelow.setBackgroundColor(resources.getColor(R.color.red))

                viewSetAbove.setBackgroundColor(resources.getColor(R.color.red))

                viewSetBelow.setBackgroundColor(resources.getColor(R.color.red))

                viewGoAbove.setBackgroundColor(resources.getColor(R.color.red))

                viewGoBelow.setBackgroundColor(resources.getColor(R.color.red))

                linearLayoutGetMiddle.background = resources.getDrawable(R.drawable.circular_red)

                linearLayoutSetMiddle.background = resources.getDrawable(R.drawable.circular_red)

                linearLayoutGoMiddle.background = resources.getDrawable(R.drawable.circular_red)

                textViewGet.setTextColor(resources.getColor(R.color.red))

                textViewGetDetails.setTextColor(resources.getColor(R.color.red))

                textViewSet.setTextColor(resources.getColor(R.color.red))

                textViewSetDetails.setTextColor(resources.getColor(R.color.red))

                textViewGo.setTextColor(resources.getColor(R.color.red))

                textViewGoDetails.setTextColor(resources.getColor(R.color.red))

                textViewNumberGo.visibility = View.GONE

                imageViewWhiteCheckGo.visibility = View.VISIBLE

                textViewNumberSet.visibility = View.GONE

                imageViewWhiteCheckSet.visibility = View.VISIBLE

                textViewNumberGet.visibility = View.GONE

                imageViewWhiteCheckGet.visibility = View.VISIBLE

                Handler().postDelayed({
                    linearLayoutServiceGetSet.visibility = View.GONE

                    linearlayoutReviewGet.visibility = View.VISIBLE

                    textViewHeadingGet.text = "Share Your Experience"

                    textViewHeadingGet.setTextColor(resources.getColor(R.color.color_grey))

                    val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.fade)

                    linearlayoutReviewGet.startAnimation(animation)
                }, 500)
            }
        }
    }

    // function for opening sharing options bottom sheet
    private fun shareIt()
    {
        val intent: Intent = Intent(android.content.Intent.ACTION_SEND)

        intent.type = "text/plain"

        val shareBody: String = "Here is the share content body"

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here")

        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)

        startActivity(Intent.createChooser(intent, "Share via"))
    }

    // function for opening custom dialog
    private fun openWonderfulPopUp()
    {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        val view: View = LayoutInflater.from(this).inflate(R.layout.inflate_review_pop_up, null)

        builder.setView(view)

        val dialog: AlertDialog = builder.create()

        view.llWonderFul.setOnClickListener {
            dialog.dismiss()
        }

        view.imageViewrFacebookReview.setOnClickListener {
            dialog.dismiss()
        }

        view.imageViewrTwitterReview.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // function for managing the UI as per the service's name
    private fun manageService(string: String)
    {
        when (string)
        {
            RsaConstants.ServiceName.flatTyre ->
            {
                imageViewRequestType.setImageResource(R.drawable.mechanic_with_cap_blue)

                textViewServiceDetailsGet.text = getString(R.string.fobbu_will_set_your_vehicle)

                textViewGetDetails.text = resources.getString(R.string.work_order_payment)
            }

            RsaConstants.ServiceName.jumpStart ->
            {
                imageViewRequestType.setImageResource(R.drawable.mechanic_with_cap_blue)

                textViewServiceDetailsGet.text = getString(R.string.fobbu_will_set_your_vehicle)

                textViewGetDetails.text = getString(R.string.otp_and_payment_recevied)
            }

            RsaConstants.ServiceName.fuelDelivery ->
            {
                imageViewRequestType.setImageResource(R.drawable.fuel_station)

                textViewServiceDetailsGet.text = getString(R.string.while_fobbu_fills_fuel)

                textViewGetDetails.text = getString(R.string.otp_validate_and_upload_dl)
            }

            RsaConstants.ServiceName.burstTyre ->
            {
                imageViewRequestType.setImageResource(R.drawable.mechanic_with_cap_blue)

                textViewServiceDetailsGet.text = getString(R.string.fobbu_will_set_your_vehicle)

                textViewGetDetails.text = resources.getString(R.string.otp_and_payment_recevied)
            }

            RsaConstants.ServiceName.towing ->
            {
                linearLayoutServiceGetSet.visibility = View.GONE

                linearlayoutReviewGet.visibility = View.VISIBLE

                textViewHeadingGet.text = getString(R.string.share_your_experience)

                textViewHeadingGet.setTextColor(resources.getColor(R.color.color_grey))

                linearLayoutGo.visibility = View.GONE

                linearLayoutGet.visibility = View.GONE

                linearLayoutSet.visibility = View.GONE
            }
        }
    }

    //*************************** provide_ratings API *************************//

    // implementing the provide_ratings API
    private fun postReviews()
    {
        getSetGoHandler.postReviews(RequestBody.create(MediaType.parse("text/plain"), CommonClass(this, this).getString(RsaConstants.ServiceSaved.fobbuRequestId)), RequestBody.create(MediaType.parse("text/plain"), ratinBarGetSet.rating.toString()), RequestBody.create(MediaType.parse("text/plain"), editTextReviewGet.text.trim().toString()), CommonClass(this, this).getString("x_access_token"))
    }

    // handling the response of the provide_ratings API
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success == "true")
        {
            CommonClass(this, this).workDoneReviewSend()

            openWonderfulPopUp()       // function for opening custom dialog

            Handler().postDelayed(
                {
                    startActivity(Intent(this, DashboardActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                },1000
            )
        }
        else
            Toast.makeText(this, mainPojo.message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoader() {}

    override fun hideLoader() {}


    override fun onResume()
    {
        super.onResume()

        val filter = IntentFilter(FcmPushTypes.Types.startedWorkBroadcast)

        registerReceiver(changeRSALiveScreenReceiver, filter)
    }

    //  initialising broadcast receiver
    private val changeRSALiveScreenReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent)
        {
            if (intent.getStringExtra("navigate_to") == FcmPushTypes.Types.startedWork)
                changeLayout("Set")// function for changing the UI of get_set_go layout

            else if (intent.getStringExtra("navigate_to") == FcmPushTypes.Types.workEnded)
                changeLayout("")       // function for changing the UI of get_set_go layout
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()

        try
        {
            unregisterReceiver(changeRSALiveScreenReceiver)
        }
        catch (e: Exception) { }
    }


    override fun onBackPressed()
    {
        val intent =   Intent(Intent.ACTION_MAIN)

        intent.addCategory(Intent.CATEGORY_HOME)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        startActivity(intent)
    }
}
