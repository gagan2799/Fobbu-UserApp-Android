package com.fobbu.member.android.activities.orderAndPassbookModule.passbook

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomSheetDialog
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.*
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.orderAndPassbookModule.passbook.adapter.PassbookAdapter
import com.fobbu.member.android.activities.orderAndPassbookModule.passbookDetail.PassbookDetailActivity
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.activity_my_passbook.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class MyPassbookActivity : AppCompatActivity()
{
    lateinit var searchtoolbar: Toolbar

    private lateinit var search_menu: Menu

    private lateinit var item_search: MenuItem

    lateinit var commonClass:CommonClass

    private lateinit var passbookAdapter: PassbookAdapter

    private lateinit var dialogBottom: BottomSheetDialog

    private lateinit var myCalendar: Calendar

    private var startDateCustom = ""

    private var endDateCustom = ""

    private lateinit var datePickerDialog: DatePickerDialog.OnDateSetListener

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_my_passbook)

        initView()    // function for initialising  all the variables of the class

        clicks()         // function for managing all the clicks of the class
    }

    // function for initialising  all the variables of the class
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initView()
    {
        fobbu_icon.visibility= View.VISIBLE

        setSupportActionBar(toolbar)

        setsearchtoolbar()

        commonClass= CommonClass(this,this)

        setBottomSheet(this)

        setRecycler()
    }

    // function for setting up filter bottom sheet
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    private fun setBottomSheet(context: Context)
    {
        val view = layoutInflater.inflate(R.layout.inflate_passbook_bottom_sheet, null)

        dialogBottom = BottomSheetDialog(this)

        dialogBottom.setContentView(view)

       val tvStartDate: TextView = dialogBottom.findViewById(R.id.tvStartDatePB)!!

       val tvEndDate: TextView = dialogBottom.findViewById(R.id.tvEndDatePB)!!

        val tvTodayDate:TextView= dialogBottom.findViewById(R.id.tvPassbookToday)!!

        val tvYesterday:TextView= dialogBottom.findViewById(R.id.tvPassbookYesterday)!!

        val tvPassbookWeek:TextView= dialogBottom.findViewById(R.id.tvPassbookWeek)!!

        tvStartDate.setOnClickListener {
            openDatePicker("Start", tvStartDate)            // function for opening date picker dialog
        }

        tvEndDate.setOnClickListener {
            openDatePicker("End", tvEndDate)               // function for opening date picker dialog
        }

        tvTodayDate.text=commonClass.getCurrentDate("today")
    }

    // function for setting up recycler
    private fun setRecycler()
    {
        passbookAdapter= PassbookAdapter(this)

        rvPassbook.layoutManager= LinearLayoutManager(this)

        rvPassbook.adapter=passbookAdapter
    }


    // function for managing all the clicks of the class
    private fun clicks()
    {
        ivSearchToolbar.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                circleReveal(R.id.searchtoolbar, 1, true, true)

            else
                searchtoolbar.visibility = View.VISIBLE

            item_search.expandActionView()
        }

        ivBackButton.setOnClickListener {
            finish()
        }

        ivFilterPassbook.setOnClickListener {
            dialogBottom.show()
        }

        rvPassbook.addOnItemTouchListener(RecyclerItemClickListener(this,object:RecyclerItemClickListener.OnItemClickListener
        {
            override fun onItemClick(view: View, position: Int)
            {
               startActivity(Intent(this@MyPassbookActivity, PassbookDetailActivity::class.java))
            }
        }))
    }

    // function for setting up search tool bar
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setsearchtoolbar()
    {
        searchtoolbar = findViewById(R.id.searchtoolbar)

        searchtoolbar.inflateMenu(R.menu.menu_search)

        search_menu = searchtoolbar.menu

        searchtoolbar.setNavigationOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            //searchtoolbar!!.visibility = View.VISIBLE
                circleReveal(R.id.searchtoolbar, 1, true, false)
            else
                searchtoolbar!!.visibility = View.GONE
        }

        item_search = search_menu.findItem(R.id.action_filter_search)

        MenuItemCompat.setOnActionExpandListener(item_search, object : MenuItemCompat.OnActionExpandListener
        {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean
            {
                // Do something when collapsed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.searchtoolbar, 1, true, false)

                 else
                    searchtoolbar.visibility = View.GONE

                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean
            {
                // Do something when expanded
                return true
            }
        })
        initSearchView()           // function for initialising the views and variables of the search tool bar
    }

    // function for initialising the views and variables of the search tool bar
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun initSearchView()
    {
        val searchView = search_menu.findItem(R.id.action_filter_search).actionView as SearchView

        // Enable/Disable Submit button in the keyboard

        searchView.isSubmitButtonEnabled = false

        searchView.setBackgroundResource(R.drawable.et_blue_base)

        val searchplate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate) as View

        searchplate.setBackgroundResource(R.drawable.et_white_base)

        // Change search close button image

        val closeButton = searchView.findViewById(R.id.search_close_btn) as ImageView

        closeButton.setImageResource(R.drawable.cross_white)

        // set hint and the text colors

        val txtSearch = searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_src_text) as EditText

        txtSearch.hint = "Search.."

        txtSearch.setTextColor(resources.getColor(R.color.white))

        txtSearch.setHintTextColor(resources.getColor(R.color.white))

        // set the cursor

        val searchTextView = searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_src_text) as AutoCompleteTextView

        try
        {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")

            mCursorDrawableRes.isAccessible = true

            // mCursorDrawableRes.set(searchTextView, R.drawable.search) //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean
            {
                callSearch(query)          // function for performing search operation on the list

                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean
            {
                callSearch(newText)            // function for performing search operation on the list

                return true
            }

            // function for performing search operation on the list
            fun callSearch(query: String)
            {
                Log.i("query", "" + query)
            }
        })
    }


    // function for adding circle reveal animation to the search tool bar
    @SuppressLint("PrivateResource")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun circleReveal(viewID: Int, posFromRight: Int, containsOverflow: Boolean, isShow: Boolean)
    {
        val myView = findViewById<View>(viewID)

        var width = myView.width

        if (posFromRight > 0)
            width -= posFromRight * resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) - resources.getDimensionPixelSize(
                R.dimen.abc_action_button_min_width_material
            )

        if (containsOverflow)
            width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)

        val cx = width + 9

        val cy = myView.height / 2

        val anim: Animator

        anim = if (isShow)
            ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, width.toFloat())

        else
            ViewAnimationUtils.createCircularReveal(myView, cx, cy, width.toFloat(), 0f)

        anim.duration = 220.toLong()

        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter()
        {
            override fun onAnimationEnd(animation: Animator)
            {
                if (!isShow)
                {
                    super.onAnimationEnd(animation)

                    myView.visibility = View.INVISIBLE
                }
            }
        })
        // make the view visible and start the animation
        if (isShow)
            myView.visibility = View.VISIBLE

        // start the animation
        anim.start()
    }

    // function for opening date picker dialog
    private fun openDatePicker(from: String, textview: TextView)
    {
        myCalendar = Calendar.getInstance()

        datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)

            myCalendar.set(Calendar.MONTH, monthOfYear)

            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            updateLabel(from, textview)           // function updating text view with the date that was selected by the user
        }

        val datePicker = DatePickerDialog(this, R.style.CustomPickerTheme, datePickerDialog, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH))

        datePicker.show()
    }

    // function updating text view with the date that was selected by the user
    @SuppressLint("SimpleDateFormat")
    private fun updateLabel(from: String, etDate: TextView?)
    {
        val myFormat = "dd MMM yyyy" //In which you need put here

        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)

        etDate!!.text = sdf.format(myCalendar.time)

        val sdfServer = SimpleDateFormat("yyyy-MM-dd")
        // sdf.timeZone = TimeZone.getTimeZone("UTC")

        if (from == "Start")
            startDateCustom = sdfServer.format(myCalendar.time)

         else
            endDateCustom = sdfServer.format(myCalendar.time)
    }
}


