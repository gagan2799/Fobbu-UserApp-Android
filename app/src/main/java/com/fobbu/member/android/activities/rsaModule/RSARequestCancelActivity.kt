package com.fobbu.member.android.activities.rsaModule

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.rsaModule.adapter.RsaRecyclerAdapter
import com.fobbu.member.android.activities.rsaModule.presenter.RsaCancelRequestHandler
import com.fobbu.member.android.activities.rsaModule.presenter.RsaCancelRequestPresenter
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaClassType
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_cancel_rsa.*
import kotlinx.android.synthetic.main.fragment_builder_confirm.view.*
import com.fobbu.member.android.utils.RecyclerItemClickListener
import java.util.HashMap


class RSARequestCancelActivity : AppCompatActivity(), ActivityView {


    val text: String = "Are you sure you want to cancel\n A cancelation fee or Rs$$ will be charged"
    var issueList = ArrayList<HashMap<String, Any>>()
    lateinit var rsaHandler: RsaCancelRequestHandler
    lateinit var rsaRecyclerAdapter: RsaRecyclerAdapter
    var row_index: Int = 0
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_rsa)
        //window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.navigationBarColor=resources.getColor(R.color.white)
        Handler().postDelayed(object :Runnable
        {
            override fun run() {
                window.navigationBarColor=resources.getColor(R.color.black)
            }

        },1000)


        rsaHandler = RsaCancelRequestPresenter(this, this)
        rsaHandler.cancelReasons(CommonClass(this, this).getString("x_access_token"))
        clicks()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun clicks() {

        ivBack.setOnClickListener {
            finish()
            window.navigationBarColor=resources.getColor(R.color.white)
            overridePendingTransition(R.anim.fade, R.anim.slide_up)
        }

        tvCancelRequest.setOnClickListener {

            var isSelected = false
            var reason=""

            for (i in issueList.indices) {
                if (issueList[i]["select"] == "1") {
                    isSelected = true
                    reason = issueList[i]["reason"].toString()
                    break
                }
            }

            if(isSelected)
            {
               showPopUp(this,reason)
            }
            else
            {
                Toast.makeText(this,resources.getString(R.string.please_select_one_issue)
                    ,Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setRecycler() {

        rsaRecyclerAdapter = RsaRecyclerAdapter(this, issueList)

        recyclerViewCancelRsa.setHasFixedSize(true)

        recyclerViewCancelRsa.layoutManager = GridLayoutManager(this, 3)

        recyclerViewCancelRsa.adapter = rsaRecyclerAdapter

        recyclerViewCancelRsa.addOnItemTouchListener(
            RecyclerItemClickListener(this, object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {

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

    private fun showPopUp(activity: Activity,string: String) {
        val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val cViewFinalPopup = inflater.inflate(R.layout.fragment_builder_confirm, null)

        val builderFinal = Dialog(activity!!)

        builderFinal.requestWindowFeature(Window.FEATURE_NO_TITLE)

        builderFinal.setCancelable(false)

        builderFinal.setContentView(cViewFinalPopup)

        builderFinal.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val tvCancel = cViewFinalPopup.findViewById(R.id.tvCancel) as TextView

        val tvText = cViewFinalPopup.findViewById(R.id.tvText) as TextView
        tvText.text = text
        cViewFinalPopup.tvCancel.text = "NO"
        cViewFinalPopup.tvConfirm.text = "YES"

        val tvConfirm = cViewFinalPopup.findViewById(R.id.tvConfirm) as TextView

        tvCancel.setOnClickListener {
            builderFinal.dismiss()

        }
        tvConfirm.setOnClickListener {
            builderFinal.dismiss()

            rsaHandler.cancelRequest(
                string,
                CommonClass(this, this).getString("fobbu_request_id"),
                CommonClass(this, this).getString("x_access_token")
            )


            //
        }

        builderFinal.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        builderFinal.show()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        window.navigationBarColor=resources.getColor(R.color.white)
        overridePendingTransition(R.anim.fade, R.anim.slide_up)
    }

    override fun onRequestSuccessReport(mainPojo: MainPojo) {
        if (mainPojo.success == "true") {
            if (mainPojo.message == "Service List") {
                issueList.clear()
                issueList = mainPojo.list

                for (i in issueList.indices) {
                    issueList[i]["select"] = "0"
                }

                setRecycler()
            } else {
                CommonClass(this, this).removeString("fobbu_request_id")
                CommonClass(this, this).putString(RsaClassType.RsaTypes.onGoingRsaScreen, "")
                CommonClass(this, this).putString(RsaClassType.RsaTypes.onGoingRsaScreenType, "")


                startActivity(
                    Intent(
                        this,
                        CancellationSuccessMessageActivity::class.java
                    )
                )
                finish()

            }

        } else {
            Toast.makeText(this, "" + mainPojo.message, Toast.LENGTH_SHORT).show()
        }


    }

    override fun showLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
