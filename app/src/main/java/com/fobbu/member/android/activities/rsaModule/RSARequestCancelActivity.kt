package com.fobbu.member.android.activities.rsaModule

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboard.DashboardActivity
import com.fobbu.member.android.activities.rsaModule.adapter.RsaRecyclerAdapter
import com.fobbu.member.android.activities.rsaModule.presenter.RsaCancelRequestHandler
import com.fobbu.member.android.activities.rsaModule.presenter.RsaCancelRequestPresenter
import com.fobbu.member.android.fcm.FcmPushTypes
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_cancel_rsa.*
import kotlinx.android.synthetic.main.fragment_builder_confirm.view.*
import com.fobbu.member.android.utils.RecyclerItemClickListener
import java.lang.Exception
import java.util.HashMap


class RSARequestCancelActivity : AppCompatActivity(), ActivityView
{
    var issueList = ArrayList<HashMap<String, Any>>()

    private lateinit var rsaHandler: RsaCancelRequestHandler

    lateinit var rsaRecyclerAdapter: RsaRecyclerAdapter

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cancel_rsa)

        initView()      // function for initialising all the views and variables of the class

        clicks()     // function for handling all the clicks of the class
    }

    // function for initialising all the views and variables of the class
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initView()
    {
        window.navigationBarColor=resources.getColor(R.color.white)

        Handler().postDelayed({ window.navigationBarColor=resources.getColor(R.color.black) },1000)

        rsaHandler = RsaCancelRequestPresenter(this, this)

        cancelReasons()     // implementing reasons API
    }

    // function for handling all the clicks of the class
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun clicks()
    {
        ivBack.setOnClickListener {
            window.navigationBarColor=resources.getColor(R.color.white)

            overridePendingTransition(R.anim.fade, R.anim.slide_up)

            finish()
        }

        tvCancelRequest.setOnClickListener {
            var isSelected = false

            var reason=""

            for (i in issueList.indices)
            {
                if (issueList[i]["select"] == "1")
                {
                    isSelected = true

                    reason = issueList[i]["reason"].toString()

                    break
                }
            }
            if(isSelected)
                showPopUp(this,reason)         // function for showing custom dialog

            else
                CommonClass(this,this).showToast(resources.getString(R.string.please_select_one_issue),rlRequestCancel)
        }
    }

    override fun onResume()
    {
        super.onResume()

        val filterStatus = IntentFilter(FcmPushTypes.Types.checkStatusPushOneTime)

        registerReceiver(changeStatusReceiver, filterStatus)
    }

    // implementing broadcast receiver
    private val changeStatusReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent) {

            println("GOT BROADCAST API CHECK STATUS ")

            if(intent.hasExtra("ForCancelScreen"))
            {
                if(intent.getStringExtra("ForCancelScreen") == FcmPushTypes.Types.requestCancelled)
                {
                    CommonClass(this@RSARequestCancelActivity, this@RSARequestCancelActivity).workDoneReviewSend()

                    startActivity(Intent(this@RSARequestCancelActivity, DashboardActivity::class.java))

                    finish()
                }
            }
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()

        try
        {
            unregisterReceiver(changeStatusReceiver)
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }
    }

    // function for setting up recycler
    private fun setRecycler()
    {
        rsaRecyclerAdapter = RsaRecyclerAdapter(this, issueList)

        recyclerViewCancelRsa.setHasFixedSize(true)

        recyclerViewCancelRsa.layoutManager = GridLayoutManager(this, 3)

        recyclerViewCancelRsa.adapter = rsaRecyclerAdapter

        recyclerViewCancelRsa.addOnItemTouchListener(
            RecyclerItemClickListener(this, object : RecyclerItemClickListener.OnItemClickListener
            {
                override fun onItemClick(view: View, position: Int)
                {
                    if (issueList[position]["select"] == "0")
                    {
                        for (i in issueList.indices)
                            issueList[i]["select"] = "0"

                        issueList[position]["select"] = "1"
                    }
                    else
                    {
                        for (i in issueList.indices)
                            issueList[i]["select"] = "0"
                    }
                    rsaRecyclerAdapter.notifyDataSetChanged()
                }
            })
        )
        rsaRecyclerAdapter.notifyDataSetChanged()
    }

    // function for showing custom dialog
    @SuppressLint("InflateParams")
    private fun showPopUp(activity: Activity, string: String)
    {
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val cViewFinalPopup = inflater.inflate(R.layout.fragment_builder_confirm, null)

        val builderFinal = Dialog(activity)

        builderFinal.requestWindowFeature(Window.FEATURE_NO_TITLE)

        builderFinal.setCancelable(false)

        builderFinal.setContentView(cViewFinalPopup)

        builderFinal.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val tvCancel = cViewFinalPopup.findViewById(R.id.tvCancel) as TextView

        val tvText = cViewFinalPopup.findViewById(R.id.tvText) as TextView

        tvText.text = resources.getString(R.string.cancellation_msg)

        cViewFinalPopup.tvCancel.text = resources.getString(R.string.no).toUpperCase()

        cViewFinalPopup.tvConfirm.text = resources.getString(R.string.yes).toUpperCase()

        val tvConfirm = cViewFinalPopup.findViewById(R.id.tvConfirm) as TextView

        tvCancel.setOnClickListener {
            builderFinal.dismiss()
        }

        tvConfirm.setOnClickListener {
            builderFinal.dismiss()

           cancelRequest(string)
        }
        builderFinal.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        builderFinal.show()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBackPressed()
    {
        window.navigationBarColor=resources.getColor(R.color.white)

        overridePendingTransition(R.anim.fade, R.anim.slide_up)

        super.onBackPressed()
    }

    // *************************** reasons API  and change_status API (CANCELLATION) ********************//

    // implementing change_status API for cancellation
    private fun cancelRequest(string: String)
    {
        if (CommonClass(this,this).checkInternetConn(this))
            rsaHandler.cancelRequest(string, CommonClass(this, this).getString(RsaConstants.ServiceSaved.fobbuRequestId), CommonClass(this, this).getString("x_access_token"))

        else
            CommonClass(this,this).showToast(resources.getString(R.string.noInternet),rlRequestCancel)
    }

    // implementing reasons API
    private fun cancelReasons()
    {
        if (CommonClass(this,this).checkInternetConn(this))
            rsaHandler.cancelReasons(CommonClass(this, this).getString("x_access_token"))

        else
            CommonClass(this,this).showToast(resources.getString(R.string.internet_is_unavailable),rlRequestCancel)
    }

    // handling the response of the reasons API and change_status API (cancellation)
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success == "true")
        {
            if (mainPojo.message == "Service List")      // reasons of cancellation
            {
                issueList.clear()

                issueList = mainPojo.list

                for (i in issueList.indices)
                    issueList[i]["select"] = "0"

                setRecycler()          // function for setting up recycler
            }
            else                          // canceling current request
            {
                CommonClass(this, this).workDoneReviewSend()

                startActivity(Intent(this, CancellationSuccessMessageActivity::class.java))

                finish()
            }
        }
        else
            CommonClass(this,this).showToast("" + mainPojo.message, rlRequestCancel)
    }

    override fun showLoader() {}

    override fun hideLoader() {}
}
