package com.fobbu.member.android.fragments.odsModule.odsServiceOperations

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fobbu.member.android.R
import com.fobbu.member.android.fragments.odsModule.odsFragment.OdsFragment
import com.fobbu.member.android.fragments.odsModule.odsServiceOperations.adapter.OdsOperationAdapter
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.interfaces.TopBarChanges
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.fragment_ods_operation.view.*
import java.text.SimpleDateFormat
import java.util.*


class OdsOperationFragment : Fragment()
{
    private lateinit var myCalendar: Calendar

    private var startdateCustom = ""

    lateinit var odsOperationAdapter:OdsOperationAdapter
    
    private var dateCustom = ""

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

        manageLayout(view)

        setRecycler(view)
    }


    // managing the layout according to the service selecetd
    private fun manageLayout(view:View)
    {
        when(commonClass.getString(RsaConstants.Ods.odsService))
        {
            "Trip Ready"->
            {
                view.ivServiceImageOperation.setImageResource(R.drawable.trip_ready_inner_screen)

                view.tvOperationLogo.text=activity!!.resources.getString(R.string.awsome_lets_do_this)

                view.tvOperationSubLogo.text=activity!!.resources.getString(R.string.to_make_to_trip_hapy_and_safe)
            }

            "General Service"->
            {
                view.ivServiceImageOperation.setImageResource(R.drawable.general_service_inner_screen)

                view.tvOperationLogo.text=getString(R.string.keep_me_healthy)

                view.tvOperationSubLogo.text=getString(R.string.keep_running)
            }
            "Washing"->
            {
                view.ivServiceImageOperation.setImageResource(R.drawable.washing_inner_screen)

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
           selectDateFromCalender()
        }

        view.tvOperationTime.setOnClickListener {
            selectTime()
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

    private fun selectDateFromCalender()
    {
        myCalendar = Calendar.getInstance()

        datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)

            myCalendar.set(Calendar.MONTH, monthOfYear)

            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            dateCustom=updateLabel()

            println("date:::$dateCustom")
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


  private fun   selectTime()
  {
      val mcurrentTime = Calendar.getInstance()

      val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)

      val minute = mcurrentTime.get(Calendar.MINUTE)
    val mTimePicker: TimePickerDialog

      mTimePicker =  TimePickerDialog(activity!!,
          TimePickerDialog.OnTimeSetListener { _, p1, p2 -> println("time::: $p1:$p2") }, hour, minute, true);//Yes 24 hour time

      mTimePicker.setTitle("Select Time")

      mTimePicker.show()
  }

  }



