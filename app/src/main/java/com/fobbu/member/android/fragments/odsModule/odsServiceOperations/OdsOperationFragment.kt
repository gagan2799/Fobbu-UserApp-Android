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

import com.fobbu.member.android.R
import com.fobbu.member.android.activities.paymentModule.WorkSummaryActivity
import com.fobbu.member.android.fragments.odsModule.odsFragment.OdsFragment
import com.fobbu.member.android.fragments.odsModule.odsServiceOperations.adapter.OdsOperationAdapter
import com.fobbu.member.android.fragments.rsaFragmentModule.RSAFragment
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.interfaces.TopBarChanges
import com.fobbu.member.android.utils.CommonClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_ods_operation.*
import kotlinx.android.synthetic.main.fragment_ods_operation.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class OdsOperationFragment : Fragment()
{
    private lateinit var myCalendar: Calendar

    private var startdateCustom = ""

    private lateinit var odsOperationAdapter:OdsOperationAdapter
    
    private var dateCustom = ""

    private var time=""

    lateinit var dataList:ArrayList<HashMap<String,Any>>

    private lateinit var datePickerDialog: DatePickerDialog.OnDateSetListener

    lateinit var commonClass:CommonClass

    private var topBarChanges: TopBarChanges? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
     val   view= inflater.inflate(R.layout.fragment_ods_operation, container, false)
        if (view!= null)
        {
            initView(view)

            clicks(view)
        }
        return  view
    }



    // function for initialising all the variables of the class
    private fun initView(view: View)
    {
        commonClass= CommonClass(activity!!,activity!!)

        dataList= ArrayList()

        if (commonClass.getStringList(RsaConstants.Ods.singleServiceList).isNotEmpty())
        {
            dataList=commonClass.getStringList(RsaConstants.Ods.singleServiceList)
        }

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
                    """${activity!!.getString(R.string.rs)} ${dataList[0][RsaConstants.Ods.service_price].toString()}"""

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
        odsOperationAdapter= OdsOperationAdapter(activity!!)

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
            selectTime(view)
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
                startActivity(Intent(activity!!,WorkSummaryActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(RsaConstants.Ods.time,time)
                    .putExtra(RsaConstants.Ods.date,dateCustom))
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

              view.tvOperationTime.text=time

              if (dateCustom!="")
                  startActivity(Intent(activity!!,WorkSummaryActivity::class.java)
                      .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                      .putExtra(RsaConstants.Ods.time,time)
                      .putExtra(RsaConstants.Ods.date,dateCustom))

          }, hour, minute, false)//Yes 24 hour time

      mTimePicker.setTitle("Select Time")

      mTimePicker.show()
  }


  }



