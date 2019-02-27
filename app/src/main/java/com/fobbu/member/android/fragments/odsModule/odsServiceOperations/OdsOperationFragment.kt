package com.fobbu.member.android.fragments.odsModule.odsServiceOperations

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import com.fobbu.member.android.R
import com.fobbu.member.android.activities.paymentModule.WorkSummaryActivity
import com.fobbu.member.android.fragments.odsModule.odsFragment.OdsFragment
import com.fobbu.member.android.fragments.odsModule.odsServiceOperations.adapter.OdsOperationAdapter
import com.fobbu.member.android.fragments.odsModule.odsServiceOperations.presenter.OdsRequestHandler
import com.fobbu.member.android.fragments.odsModule.odsServiceOperations.presenter.OdsRequestPresenter
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.interfaces.TopBarChanges
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_ods_operation.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class OdsOperationFragment : Fragment(),ActivityView
{
    private lateinit var myCalendar: Calendar

    private var startdateCustom = ""

    private lateinit var odsOperationAdapter:OdsOperationAdapter

    private var dateCustom = ""

    private var time=""

    private var timeSlot=""

    val mainList= ArrayList<HashMap<String,Any>>()

    private var startDate=""

    private var endDate=""

    var dataList:ArrayList<HashMap<String,Any>> = ArrayList()

    private lateinit var datePickerDialog: DatePickerDialog.OnDateSetListener

    lateinit var commonClass:CommonClass

    private lateinit var odsRequestHandler: OdsRequestHandler

    lateinit var rlLoader:RelativeLayout

    private var topBarChanges: TopBarChanges? = null

    private lateinit var subServiceList:ArrayList<Map<String,Any>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val   view= inflater.inflate(R.layout.fragment_ods_operation, container, false)

        if (isAdded)
        {
            if (view!= null)
            {
                initView(view)

                clicks(view)
            }
        }
        return  view
    }



    // function for initialising all the variables of the class
    private fun initView(view: View)
    {
        commonClass= CommonClass(activity!!,activity!!)

        commonClass.removeString("subServiceList")

        subServiceList= ArrayList()

        dataList= ArrayList()

        if (commonClass.getStringList(RsaConstants.Ods.singleServiceList).isNotEmpty())
        {
            dataList=commonClass.getStringList(RsaConstants.Ods.singleServiceList)

            val map=dataList[0]

            subServiceList=(map.getValue("sub_services") as ArrayList<Map<String,Any>>)

            println("array list:::::$subServiceList")



            for (i in subServiceList.indices)
            {
                val listOption= ArrayList<HashMap<String,Any>>()

                val hashMain = HashMap<String,Any>()

                val firstOptionList = subServiceList[i]["option"] as ArrayList<String>

                for (j in firstOptionList.indices)
                {
                    val hashMapInner = HashMap<String,Any>()
                    hashMapInner["name"]= firstOptionList[j]
                    hashMapInner["selected"]= "0"
                    hashMapInner["inner_service_name"]= subServiceList[i]["service_name"].toString()
                    listOption.add(hashMapInner)
                }

                hashMain["option"]=listOption
                hashMain["service_name"]=subServiceList[i]["service_name"].toString()
                mainList.add(hashMain)

            }

            println("MAIN  list:::::    $mainList")

            /*   val jsonObject=JSONObject(map)

                  val jsonArray=jsonObject.getJSONArray("sub_services")

                      for (j in 0 until jsonArray.length())
                      {
                          subServiceList.add(jsonArray[j])
                      }

                  println("sub service list::: $subServiceList")*/
        }

        rlLoader=view.findViewById(R.id.rlLoader)

        odsRequestHandler=OdsRequestPresenter(activity!!,this)

        setDataInView(view)

        setRecycler(view)
    }


    // managing the layout according to the service selecetd
    @SuppressLint("SetTextI18n")
    private fun setDataInView(view:View)
    {
        if (dataList[0][RsaConstants.Ods.service_image].toString()!="")
            Picasso.get().load(dataList[0][RsaConstants.Ods.service_image].toString())
                .placeholder(R.drawable.dummy_services)
                .error(R.drawable.dummy_services)
                .into(view.ivServiceImageOperation)

        else
            view.ivServiceImageOperation.setImageResource(R.drawable.dummy_services)

        if (dataList[0][RsaConstants.Ods.service_price].toString()!="")
            view.tvOperationPrice.text=
                    """${activity!!.getString(R.string.rs)} ${(dataList[0][RsaConstants.Ods.service_price]as Double).toLong()} /-"""

        when(dataList[0][RsaConstants.Ods.static_name])
        {
            RsaConstants.OdsServiceStaticName.trip_ready->
            {
                view.tvOperationLogo.text=activity!!.resources.getString(R.string.awsome_lets_do_this)

                view.tvOperationSubLogo.text=activity!!.resources.getString(R.string.to_make_to_trip_hapy_and_safe)
            }

            RsaConstants.OdsServiceStaticName.general_service->
            {
                view.tvOperationLogo.text=getString(R.string.keep_me_healthy)

                view.tvOperationSubLogo.text=getString(R.string.keep_running)
            }
            RsaConstants.OdsServiceStaticName.washing->
            {
                view.tvOperationLogo.text=getString(R.string.save_water)

                view.tvOperationSubLogo.text=getString(R.string.water_save_message)
            }
        }
    }


    // function for setting up recycler
    private fun setRecycler(view: View)
    {
        odsOperationAdapter= OdsOperationAdapter(activity!!,mainList)

        view.rvOperations.layoutManager=LinearLayoutManager(activity!!)

        view.rvOperations.adapter=odsOperationAdapter
    }


    // function for handling all the clicks of the class
    private fun clicks(view: View)
    {
        view.tvOperationDate.setOnClickListener {
            selectDateFromCalender(view)
        }

        view.tvOperationTime.setOnClickListener {
            /*selectTime(view)*/

            if (commonClass.getStringList("subServiceList").isNotEmpty())
            {
                println("SELECTED SERVICE LIST::::::   ${commonClass.getStringList("subServiceList")}")
            }
        }

        view.ivBack.setOnClickListener {
            ifTopBarChnagesNull(true)

            changeFragment(OdsFragment())
        }
    }


    // function for changing the fragment
    private fun changeFragment(fragment:Fragment)
    {
        val ft: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

        ft.replace(R.id.content_frame, fragment)

        ft.commit()
    }


    private fun ifTopBarChnagesNull(boolean: Boolean) {
        if (topBarChanges == null)
            topBarChanges = activity as TopBarChanges

        topBarChanges!!.showGoneTopBar(boolean)
    }

    private fun selectDateFromCalender(view: View)
    {
        myCalendar = Calendar.getInstance()

        datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)

            myCalendar.set(Calendar.MONTH, monthOfYear)

            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            dateCustom=updateLabel()

            view.tvOperationDate.text=dateCustom

            if (time!="")
            {
                if (dataList[0][RsaConstants.Ods.service_name].toString()=="Washing")
                    makeOdsRequest()
                else
                    startActivity(Intent(activity!!,WorkSummaryActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(RsaConstants.Ods.time,timeSlot)
                        .putExtra(RsaConstants.Ods.date,dateCustom))
            }
        }


        val datePicker = DatePickerDialog(activity!!, R.style.CustomPickerTheme, datePickerDialog, myCalendar

            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),

            myCalendar.get(Calendar.DAY_OF_MONTH))

        datePicker.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateLabel():String
    {
        val myFormat = "dd MMM yyyy" //In which you need put here

        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)

        startdateCustom=sdf.format(myCalendar.time)

        val sdfServer = SimpleDateFormat("dd-MM-yyyy")
        // sdf.timeZone = TimeZone.getTimeZone("UTC")

        startdateCustom = sdfServer.format(myCalendar.time)

        val sdfServerPOST = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")

        startDate=sdfServerPOST.format(myCalendar.time)

        myCalendar.add(Calendar.HOUR, 1)

        endDate=sdfServerPOST.format(myCalendar.time)

        return startdateCustom
    }


    private fun   selectTime(view: View)
    {
        val mcurrentTime = Calendar.getInstance()

        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)

        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog

        mTimePicker =  TimePickerDialog(activity!!,
            TimePickerDialog.OnTimeSetListener { _, p1, p2 ->

                val AM_PM = if(p1 < 12) {
                    "AM"
                } else {
                    "PM"
                }

                time= ("$p1:$p2 $AM_PM")

                timeSlot = if (p1==12)
                    ("$p1:$p2-${"1:$p2"} $AM_PM")

                else
                    ("$p1:$p2-${""+(p1+1)+":"+p2} $AM_PM")


                view.tvOperationTime.text=time

                if (dateCustom!="")
                {
                    if (dataList[0][RsaConstants.Ods.service_name].toString()=="Washing")
                        makeOdsRequest()
                    else
                        startActivity(Intent(activity!!,WorkSummaryActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra(RsaConstants.Ods.time,timeSlot)
                            .putExtra(RsaConstants.Ods.date,dateCustom))
                }

            }, hour, minute, false)//Yes 24 hour time

        mTimePicker.setTitle("Select Time")

        mTimePicker.show()
    }

    //********************** ods_request API ***********************//

    //implementing ods_request API
    private fun makeOdsRequest()
    {
        if (commonClass.checkInternetConn(activity!!))
        {
            val map=HashMap<String,Any>()

            //map["user_id"]=commonClass.getString("_id")

            map["service"]=dataList[0]["_id"].toString()

            map["latitude"]=commonClass.getString(RsaConstants.Ods.lat)

            map["longitude"]=commonClass.getString(RsaConstants.Ods.long)

            map["vehicle_number"]=commonClass.getString(RsaConstants.Ods.regNo)

            map["vehicle_type"]=commonClass.getString(RsaConstants.Ods.vehicleType)

            map["model"]=commonClass.getString(RsaConstants.Ods.model)

            map["start_date"]= startDate

            map["end_date"]=endDate

            map["address"]=commonClass.getString(RsaConstants.Ods.address)

            println("dataMap::::$map")

            odsRequestHandler.makeOdsRequest(map,commonClass.getString("x_access_token"))
        }
        else
            commonClass.showToast(activity!!.resources.getString(R.string.internet_is_unavailable))
    }
    //handling the response of    ods_request API
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        commonClass.showToast(mainPojo.message)
    }

    override fun showLoader()
    {
        rlLoader.visibility=View.VISIBLE
    }

    override fun hideLoader()
    {
        rlLoader.visibility=View.GONE
    }

}



